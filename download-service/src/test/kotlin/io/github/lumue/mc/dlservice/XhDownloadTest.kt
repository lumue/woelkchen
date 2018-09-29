package io.github.lumue.mc.dlservice


import io.github.lumue.mc.dlservice.sites.xh.XhHttpClient
import io.github.lumue.mc.dlservice.sites.xh.XhResolver
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

import org.junit.Test
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class XhDownloadTest {

    private val TESTVIDEO_URLS = listOf(
            "https://xhamster.com/videos/9742256",
            "https://xhamster.com/videos/8131635",
            "https://xhamster.com/videos/6888850")

    private val logger = LoggerFactory.getLogger(this.javaClass.name)


    @Test
    fun testResolveXhMetadata() {
        runBlocking {
            val xhHttpClient =XhHttpClient("dirtytom74", "ddl85s")
            val jobs: List<Job> = List(TESTVIDEO_URLS.size) {
                launch {
                    val l = MediaLocation(TESTVIDEO_URLS[it], LocalDateTime.now())
                    val metadata = XhResolver(xhHttpClient )
                            .resolveMetadata(l)
                    logger.info(metadata.jsonString())
                }
            }
            jobs.forEach { it.join() }
        }
    }

    @Test
    fun testDownloadXhVideo() {
        runBlocking {
            val targetPath = "./"
            val client = XhHttpClient("dirtytom74", "ddl85s")
            val progressHandler = fun(p: Long, time: Long, t: Long) {
                var seconds = TimeUnit.MILLISECONDS.toSeconds(time)
                if (seconds < 1) seconds = 1
                logger.debug("downloaded $p of $t in ${seconds}s. ${p / seconds} b/s")
            }

            TESTVIDEO_URLS
                    .map {
                        async {
                            val l = MediaLocation(it, LocalDateTime.now())
                            val metadata = XhResolver(client).resolveMetadata(l)
                            val metadataFile = FileOutputStream(targetPath + File.separator + metadata.contentMetadata.title + ".meta.json")
                            metadata.write(metadataFile)
                            metadataFile.close()
                            client.download(metadata, targetPath, progressHandler)
                        }
                    }.forEach { job -> job.join() }
        }
    }
}