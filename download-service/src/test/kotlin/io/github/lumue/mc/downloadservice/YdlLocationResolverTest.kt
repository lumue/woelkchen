package io.github.lumue.mc.downloadservice

import kotlinx.coroutines.experimental.runBlocking

import org.junit.Test
import java.time.LocalDateTime

class YdlLocationResolverTest {

    private val GETDOWN_TESTVIDEO_URL = "https://www.youtube.com/watch?v=nwP80FmSpOw"


    @Test
    fun testResolveYoutubeMetadata() {

        runBlocking {
            val l = MediaLocation(GETDOWN_TESTVIDEO_URL, LocalDateTime.now())
            val metadata = YdlLocationResolver()
                    .resolveMetadata(l)
            println( metadata)
        }
    }


}