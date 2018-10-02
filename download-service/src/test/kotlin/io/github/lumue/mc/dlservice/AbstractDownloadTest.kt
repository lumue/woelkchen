package io.github.lumue.mc.dlservice

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

abstract class AbstractDownloadTest {

    protected abstract val urlList: List<String>

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Test
    fun testResolveMetadata() {
        runBlocking {
            val jobs: List<Job> = List(urlList.size) {
                launch {
                    val l = MediaLocation(urlList[it], LocalDateTime.now())
                    val metadata = newResolver()
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
            val targetPath = "./"
            val progressHandler = fun(p: Long, time: Long, t: Long) {
                var seconds = TimeUnit.MILLISECONDS.toSeconds(time)
                if (seconds < 1) seconds = 1
                logger.debug("downloaded $p of $t in ${seconds}s. ${p / seconds} b/s")
            }

            urlList
                    .map {
                        async {
                            val l = MediaLocation(it, LocalDateTime.now())
                            val metadata = newResolver().resolveMetadata(l)
                          newDownloader().download(metadata, targetPath, progressHandler)
                        }
                    }.forEach { job -> job.join() }
        }
    }

    protected abstract fun newDownloader(): FileDownloader

    protected abstract fun newResolver(): LocationMetadataResolver
}