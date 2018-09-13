package io.github.lumue.mc.download

import io.github.lumue.mc.download.resolve.MediaLocation
import io.github.lumue.mc.download.resolve.xh.XhResolver
import io.github.lumue.mc.download.resolve.ydl.YdlResolver
import kotlinx.coroutines.experimental.runBlocking

import org.junit.Test
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class XhResolverTest {

    private val TESTVIDEO_URL = "https://de.xhamster.com/videos/nina-elle-s-big-fat-bombs-in-your-face-naughty-america-8653702"

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Test
    fun testResolveXhMetadata() {

        runBlocking {

            repeat(5) {
                logger.debug("testing validation of $TESTVIDEO_URL")

                val l = MediaLocation(TESTVIDEO_URL, LocalDateTime.now())
                val metadata = XhResolver()
                        .resolveMetadata(l)
                println(metadata)
            }
        }
    }


}