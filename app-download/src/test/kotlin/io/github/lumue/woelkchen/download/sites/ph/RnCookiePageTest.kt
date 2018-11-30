package io.github.lumue.woelkchen.download.sites.ph

import org.apache.commons.io.FileUtils
import org.jsoup.Jsoup
import org.junit.Test
import java.io.File

import org.assertj.core.api.Assertions.*

class RnCookiePageTest {


    val htmlString = FileUtils.readFileToString(
            File(javaClass.classLoader.getResource("io/github/lumue/woelkchen/download/sites/ph/rnlscript-page.html").toURI()
            ),
            "UTF-8"
    )

    val rnlKeyScriptFromPage = FileUtils.readFileToString(
            File(javaClass.classLoader.getResource("io/github/lumue/woelkchen/download/sites/ph/rnlscript.js.sample").toURI()
            ),
            "UTF-8"
    )



    @Test
    fun `should extract expected rnkey script from page`() {
        val page = RnCookiePage(Jsoup.parse(htmlString))
        assertThat(page.rnKeyScript.source).isEqualTo(rnlKeyScriptFromPage)
    }


    @Test
    fun `should generate source of expected rnkey script`() {
        val page = RnCookiePage(Jsoup.parse(htmlString))
        assertThat(page.rnKeyScript.generatedSource).doesNotContain("document.")
    }


    @Test
    fun `should evaluate script to expected rnkey`() {
        val page = RnCookiePage(Jsoup.parse(htmlString))
        assertThat(page.rnKeyScript.calculatedKey)
                .isEqualTo("1713737*1807987:3148355175:74430974:1")
    }
}

