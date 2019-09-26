package io.github.lumue.woelkchen.download.sites.ph


import io.github.lumue.woelkchen.download.DownloadTestBase
import io.github.lumue.woelkchen.download.BasicHttpDownload
import io.github.lumue.woelkchen.download.DownloadFileStep
import io.github.lumue.woelkchen.download.ResolveMetadataStep

class PhDownloadTest : DownloadTestBase() {

    private val httpClient : PhHttpClient = PhHttpClient()

    override val downloader: DownloadFileStep = BasicHttpDownload(httpClient)
    override val resolver: ResolveMetadataStep = PhResolver(httpClient)
    override val urlList = listOf(
            "https://www.pornhub.com/view_video.php?viewkey=942834991")

}
