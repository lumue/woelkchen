package io.github.lumue.woelkchen.download.sites.ydl

import io.github.lumue.woelkchen.download.*

class YdlDownloadTest : DownloadTestBase() {

    private val httpClient : BasicHttpClient=YdlHttpClient()

    override val downloader: io.github.lumue.woelkchen.download.DownloadFileStep
        get() = BasicHttpDownload(httpClient)
    override val resolver: ResolveMetadataStep
        get() = YdlResolver()

    override val urlList: List<String>
        get() = listOf()



    private val GETDOWN_TESTVIDEO_URL = "https://www.youtube.com/watch?v=nwP80FmSpOw"



}