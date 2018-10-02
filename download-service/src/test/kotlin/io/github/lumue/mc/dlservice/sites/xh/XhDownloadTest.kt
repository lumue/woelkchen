package io.github.lumue.mc.dlservice.sites.xh


import io.github.lumue.mc.dlservice.AbstractDownloadTest

class XhDownloadTest : AbstractDownloadTest() {

    override val urlList = listOf(
            "https://xhamster.com/videos/9742256",
            "https://xhamster.com/videos/8131635",
            "https://xhamster.com/videos/6888850")


    override fun newDownloader() = XhDownloader(httpClient)


    override fun newResolver()  = XhResolver(httpClient)

    private val httpClient: XhHttpClient
        get() {
            val xhHttpClient = XhHttpClient("dirtytom74", "ddl85s")
            return xhHttpClient
        }
}