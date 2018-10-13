package io.github.lumue.mc.dlservice

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.apache.commons.io.FileUtils
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory
import java.io.File
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

abstract class AbstractDownloadTest {

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
    fun testResolveMetadata() {
        runBlocking {
            val jobs: List<Job> = List(urlList.size) {
                launch {
                    val l = MediaLocation(urlList[it], LocalDateTime.now())
                    val metadata = resolver
                            .resolveMetadata(l)
                    logger.info(metadata.jsonString())
                }
            }
            jobs.forEach { it.join() }
        }
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
                        val metadata = resolver.resolveMetadata(l)
                        val downloadResult = downloader(metadata,
                                downloadPath,
                                progressHandler(metadata.contentMetadata.title)
                        )
                        assert(File(downloadResult.filename).exists())
                    }
        }
    }

    protected abstract val downloader: FileDownload

    protected abstract val resolver: LocationMetadataResolver
}