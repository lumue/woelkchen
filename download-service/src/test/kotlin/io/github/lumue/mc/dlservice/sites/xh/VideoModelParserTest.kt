package io.github.lumue.mc.dlservice.sites.xh


import org.junit.Assert
import org.junit.Test
import org.apache.commons.io.FileUtils
import java.io.File


class VideoModelParserTest{

    @Test
    fun testParsePage() {
        val url =javaClass.classLoader.getResource( "io/github/lumue/mc/dlservice/sites/xh/xhpage.html")
        val myFile = File(url.toURI())

        val htmlString = FileUtils.readFileToString(myFile, "UTF-8")

        val videoModel = VideoModelParser().fromHtml(htmlString)
        Assert.assertNotNull(videoModel)
    }
}