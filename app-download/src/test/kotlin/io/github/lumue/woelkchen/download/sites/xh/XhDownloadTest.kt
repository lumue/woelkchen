package io.github.lumue.woelkchen.download.sites.xh


import io.github.lumue.woelkchen.download.AbstractDownloadTest
import io.github.lumue.woelkchen.download.BasicHttpDownload
import io.github.lumue.woelkchen.download.ResolveMetadataStep

class XhDownloadTest : AbstractDownloadTest() {

    val httpClient : XhHttpClient = XhHttpClient("dirtytom74", "ddl85s")

    override val downloader: io.github.lumue.woelkchen.download.DownloadFileStep = BasicHttpDownload(httpClient)
    override val resolver: ResolveMetadataStep =  XhResolver(httpClient)
    override val urlList = listOf(
            "https://xhamster.com/videos/german-girlfriend-let-friend-fuck-another-teen-and-watch-10308118",
            "https://xhamster.com/videos/alter-notgeiler-sack-fickt-junge-blonde-10275853",
            "https://de.xhamster.com/videos/ehefotze-fickt-mit-nachbarn-10259953")



}