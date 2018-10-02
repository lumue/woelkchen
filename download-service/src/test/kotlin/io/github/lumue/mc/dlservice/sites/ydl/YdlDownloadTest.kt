package io.github.lumue.mc.dlservice.sites.ydl

import io.github.lumue.mc.dlservice.AbstractDownloadTest
import io.github.lumue.mc.dlservice.FileDownloader
import io.github.lumue.mc.dlservice.LocationMetadataResolver
import io.github.lumue.mc.dlservice.MediaLocation
import io.github.lumue.mc.dlservice.sites.xh.XhDownloader
import io.github.lumue.mc.dlservice.sites.xh.XhResolver
import kotlinx.coroutines.experimental.runBlocking

import org.junit.Test
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class YdlDownloadTest : AbstractDownloadTest() {

    override val urlList: List<String>
        get() = listOf(GETDOWN_TESTVIDEO_URL)

    override fun newDownloader(): FileDownloader {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun newResolver(): LocationMetadataResolver {
        return YdlResolver()
    }

    private val GETDOWN_TESTVIDEO_URL = "https://www.youtube.com/watch?v=nwP80FmSpOw"

    private val logger = LoggerFactory.getLogger(this.javaClass.name)



}