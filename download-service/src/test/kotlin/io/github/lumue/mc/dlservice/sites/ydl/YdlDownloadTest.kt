package io.github.lumue.mc.dlservice.sites.ydl

import io.github.lumue.mc.dlservice.AbstractDownloadTest
import io.github.lumue.mc.dlservice.FileDownload
import io.github.lumue.mc.dlservice.LocationMetadataResolver

class YdlDownloadTest : AbstractDownloadTest() {
    override val downloader: FileDownload
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val resolver: LocationMetadataResolver
        get() = YdlResolver()

    override val urlList: List<String>
        get() = listOf(GETDOWN_TESTVIDEO_URL)



    private val GETDOWN_TESTVIDEO_URL = "https://www.youtube.com/watch?v=nwP80FmSpOw"



}