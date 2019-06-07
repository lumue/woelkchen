package io.github.lumue.woelkchen.download.sites.xh


import io.github.lumue.woelkchen.download.AbstractDownloadTest
import io.github.lumue.woelkchen.download.BasicHttpDownload
import io.github.lumue.woelkchen.download.DownloadFileStep
import io.github.lumue.woelkchen.download.ResolveMetadataStep

class XhDownloadTest : AbstractDownloadTest() {

    val httpClient : XhHttpClient = XhHttpClient("dirtytom74", "ddl85s")

    override val downloader: DownloadFileStep = BasicHttpDownload(httpClient)
    override val resolver: ResolveMetadataStep = XhResolver(httpClient)
    override val urlList = listOf(
            "https://xhamster.com/videos/pretty-blonde-seduce-luck-black-man-11760459")

}