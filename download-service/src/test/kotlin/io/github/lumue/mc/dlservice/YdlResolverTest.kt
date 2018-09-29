package io.github.lumue.mc.dlservice

import io.github.lumue.mc.dlservice.sites.ydl.YdlResolver
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
            val l = MediaLocation(GETDOWN_TESTVIDEO_URL, LocalDateTime.now())

            val metadata = YdlResolver()
                    .resolveMetadata(l)
            logger.info(metadata.toString())
        }
    }


}