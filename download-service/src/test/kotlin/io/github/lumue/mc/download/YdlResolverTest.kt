package io.github.lumue.mc.download

import io.github.lumue.mc.download.resolve.MediaLocation
import io.github.lumue.mc.download.resolve.ydl.YdlResolver
import kotlinx.coroutines.experimental.runBlocking

import org.junit.Test
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class YdlResolverTest {

    private val GETDOWN_TESTVIDEO_URL = "https://www.youtube.com/watch?v=nwP80FmSpOw"

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Test
    fun testResolveYoutubeMetadata() {

        runBlocking {

            repeat(5) {
                logger.debug("testing validation of $GETDOWN_TESTVIDEO_URL")

                val l = MediaLocation(GETDOWN_TESTVIDEO_URL, LocalDateTime.now())
                val metadata = YdlResolver()
                        .resolveMetadata(l)
                println(metadata)
            }
        }
    }


}