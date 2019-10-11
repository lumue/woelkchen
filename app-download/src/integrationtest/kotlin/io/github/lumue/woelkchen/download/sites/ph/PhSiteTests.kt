package io.github.lumue.woelkchen.download.sites.ph


import io.github.lumue.woelkchen.download.*

class PhDownloadTest : DownloadTestBase() {

    override val siteClient : PhSite = PhSite()

    override val urlList = listOf(
            "https://www.pornhub.com/view_video.php?viewkey=942834991")

}


class PhResolveTest : ResolveTestBase() {

    override val siteClient : PhSite = PhSite()

    override val urlList = listOf(
            "https://www.pornhub.com/view_video.php?viewkey=942834991")

}