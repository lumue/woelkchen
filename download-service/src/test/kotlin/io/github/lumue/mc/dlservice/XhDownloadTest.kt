package io.github.lumue.mc.dlservice

import io.github.lumue.mc.dlservice.download.FileDownloader
import io.github.lumue.mc.dlservice.resolve.MediaLocation
import io.github.lumue.mc.dlservice.sites.xh.XhHttpClient
import io.github.lumue.mc.dlservice.sites.xh.XhResolver
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

import org.junit.Test
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class XhDownloadTest {

    private val TESTVIDEO_URL = "https://de.xhamster.com/videos/maximum-perversum-10168951"

    private val logger = LoggerFactory.getLogger(this.javaClass.name)


    @Test
    fun testResolveXhMetadata() {



        runBlocking {
            val jobs: List<Job> = List(5) {
                launch {
                    val l = MediaLocation(TESTVIDEO_URL, LocalDateTime.now())
                    val metadata = XhResolver(XhHttpClient())
                            .resolveMetadata(l)
                    logger.info(metadata.toString())
                }
            }
            jobs.forEach { it.join() }
        }
    }

    @Test
    fun testDownloadXhVideo(){
        val l = MediaLocation(TESTVIDEO_URL, LocalDateTime.now())
        val metadata = XhResolver(XhHttpClient())
                .resolveMetadata(l)
        runBlocking {
            val progressHandler = fun (p: Long,t:Long) {
                logger.debug("downloaded $p of $t")
            }
            FileDownloader().download(metadata.downloadMetadata.selectedStreams[0], "./", progressHandler )
        }

    }
}