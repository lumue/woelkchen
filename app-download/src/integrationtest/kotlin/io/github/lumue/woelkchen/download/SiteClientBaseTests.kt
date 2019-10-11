package io.github.lumue.woelkchen.download

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.FileUtils
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

abstract class ResolveTestBase : SiteClientTestBase(){
    protected abstract val urlList: List<String>
    private val logger = LoggerFactory.getLogger(this.javaClass.name)
    @Test
    fun testResolveMetadata() {
        runBlocking {
            val jobs: List<Job> = List(urlList.size) {
                launch {
                    val l = MediaLocation(urlList[it], LocalDateTime.now())
                    val metadata = siteClient.downloadMetadata(l)
                    logger.info(metadata.jsonString())
                }
            }
            jobs.forEach { it.join() }
        }
    }

}


abstract class DownloadTestBase : SiteClientTestBase() {

    protected abstract val urlList: List<String>

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    private val downloadPath = "./work/downloads"

    @Before
    fun setup() {

        val workPath = File(downloadPath)

        if (workPath.exists())
            FileUtils.deleteDirectory(workPath)

        workPath.mkdirs()


    }




    @Test
    fun testDownload() {
        runBlocking {
            fun progressHandler(name: String) = fun(p: Long, time: Long, t: Long) {
                var seconds = TimeUnit.MILLISECONDS.toSeconds(time)
                if (seconds < 1) seconds = 1
                logger.debug(" $p of $t in ${seconds}s. ${p / seconds} b/s of $name downloaded")
            }

            urlList
                    .forEach {
                        val l = MediaLocation(it, LocalDateTime.now())
                        val metadata = siteClient.downloadMetadata(l)
                        val downloadResult = siteClient.downloadContent(metadata,
                                downloadPath,
                                progressHandler(metadata.contentMetadata.title)
                        )
                        assert(File(downloadResult.filename).exists())
                        val out = FileOutputStream(downloadResult.filename + ".meta.json")
                        LocationMetadataWriter().write(metadata, out)
                        out.close()
                    }
        }
    }


}

abstract class SiteClientTestBase {
    abstract val siteClient: SiteClient
}
