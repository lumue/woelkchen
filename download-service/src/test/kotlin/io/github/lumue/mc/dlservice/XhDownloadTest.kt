package io.github.lumue.mc.dlservice

import io.github.lumue.mc.dlservice.download.FileDownloader
import io.github.lumue.mc.dlservice.resolve.MediaLocation
import io.github.lumue.mc.dlservice.resolve.xh.XhResolver
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

import org.junit.Test
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class XhDownloadTest {

    private val TESTVIDEO_URL = "https://de.xhamster.com/videos/nina-elle-s-big-fat-bombs-in-your-face-naughty-america-8653702"

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Test
    fun testResolveXhMetadata() {



        runBlocking {
            val l = MediaLocation(TESTVIDEO_URL, LocalDateTime.now())
            val jobs: List<Job> = List(5) {
                launch {
                    val l = MediaLocation(TESTVIDEO_URL, LocalDateTime.now())
                    val metadata = XhResolver()
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
        val metadata = XhResolver()
                .resolveMetadata(l)
        runBlocking {
            FileDownloader().download(metadata.downloadMetadata.selectedStreams[0], "./", null)
        }

    }
}