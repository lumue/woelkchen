package io.github.lumue.mc.dlservice.sites.xh


import io.github.lumue.mc.dlservice.AbstractDownloadTest
import io.github.lumue.mc.dlservice.FileDownload
import io.github.lumue.mc.dlservice.LocationMetadataResolver

class XhDownloadTest : AbstractDownloadTest() {

    val httpClient : XhHttpClient = XhHttpClient("dirtytom74", "ddl85s")

    override val downloader: FileDownload
        get() = XhDownload(httpClient)
    override val resolver: LocationMetadataResolver
        get() =  XhResolver(httpClient)
    override val urlList = listOf(
            "https://xhamster.com/videos/9742256",
            "https://xhamster.com/videos/8131635",
            "https://xhamster.com/videos/6888850")



}