package io.github.lumue.woelkchen.download.sites.ydl

import io.github.lumue.woelkchen.download.*

private val GETDOWN_TESTVIDEO_URL = "https://www.youtube.com/watch?v=nwP80FmSpOw"



class YdlDownloadTest : DownloadTestBase() {


    override val urlList: List<String>
        get() = listOf(GETDOWN_TESTVIDEO_URL)

    override val siteClient=YdlSite()

}

class YdlResolveTest : ResolveTestBase() {

    override val urlList: List<String>
        get() = listOf(GETDOWN_TESTVIDEO_URL)


    override val siteClient=YdlSite()


}