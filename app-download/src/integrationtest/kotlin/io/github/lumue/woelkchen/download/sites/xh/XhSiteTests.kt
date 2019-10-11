package io.github.lumue.woelkchen.download.sites.xh


import io.github.lumue.woelkchen.download.*
import org.junit.Before

class XhDownloadTest : DownloadTestBase() {

    override val siteClient : SiteClient = XhSite("woelkchentest", "password123")

    override val urlList = listOf(
            "https://xhamster.com/videos/pretty-blonde-seduce-luck-black-man-11760459")

}

class XhResolveTest : ResolveTestBase() {

    override val siteClient : SiteClient = XhSite("woelkchentest", "password123")

    override val urlList = listOf(
            "https://xhamster.com/videos/pretty-blonde-seduce-luck-black-man-11760459")

}

class XhLoginTest : AbstractLoginTest() {

    @Before
    fun initClient() {
        client = XhHttpClient("woelkchentest", "password123")
    }


}