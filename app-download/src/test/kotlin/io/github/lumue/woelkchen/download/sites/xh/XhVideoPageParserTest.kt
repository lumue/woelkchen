package io.github.lumue.woelkchen.download.sites.xh


import org.junit.Assert
import org.junit.Test
import org.apache.commons.io.FileUtils
import java.io.File


class XhVideoPageParserTest {

    private val htmlWithoutLdJsonString = FileUtils.readFileToString(
            File(javaClass.classLoader.getResource("io/github/lumue/woelkchen/download/sites/xh/xhpage.html")!!.toURI()),"UTF-8"
    )

    private val htmlWithLdJsonString = FileUtils.readFileToString(
            File(javaClass.classLoader.getResource("io/github/lumue/woelkchen/download/sites/xh/xh_with_ldjson.html")!!.toURI()),"UTF-8"
    )


    @Test
    fun testParsePageWithoutLdJson() {
        val videoPage = XhVideoModelParser().fromHtml(htmlWithoutLdJsonString)
        Assert.assertNotNull(videoPage)
        assert(videoPage.initialsJson.videoModel.categories[0].name.equals("Naughty America"))
    }


    @Test
    fun testParsePageWithLdJson() {
        val videoPage = XhVideoModelParser().fromHtml(htmlWithLdJsonString)
        Assert.assertNotNull(videoPage)
        assert(videoPage.ldJson.video!!.keywords!![0] == "Porn Video")
    }

}