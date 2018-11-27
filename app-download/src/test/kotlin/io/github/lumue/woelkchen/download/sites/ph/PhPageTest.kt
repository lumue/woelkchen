package io.github.lumue.woelkchen.download.sites.ph

import org.apache.commons.io.FileUtils
import org.jsoup.Jsoup
import org.junit.Assert
import org.junit.Test
import java.io.File

import org.assertj.core.api.Assertions.*

class PhPageTest {


    val htmlString = FileUtils.readFileToString(
            File(javaClass.classLoader.getResource("io/github/lumue/woelkchen/download/sites/ph/video-page.html").toURI()
            ),
            "UTF-8"
    )

    @Test
    fun `should extract content metadata from page`(){
        val page = PhVideoPage(Jsoup.parse(htmlString))
        Assert.assertNotNull(page.contentMetadata)
    }


    @Test
    fun `should extract expected title from page`(){
        val page = PhVideoPage(Jsoup.parse(htmlString))
        assertThat(page.contentMetadata.title)
                .isEqualTo("Schwiegermutter spontan morgends in der Dusche vernascht - Perfekte Milf")
    }

    @Test
    fun `should extract expected description from page`() {
        val page = PhVideoPage(Jsoup.parse(htmlString))
        assertThat(page.contentMetadata.description)
                .isEqualTo("Watch Schwiegermutter spontan morgends in der Dusche vernascht - Perfekte Milf on Pornhub.com, the best hardcore porn site. Pornhub is home to the widest selection of free Blonde sex videos full of the hottest pornstars. If you're craving mom XXX movies you'll find them here.")
    }

    @Test
    fun `should extract expected tags from page`() {
        val page = PhVideoPage(Jsoup.parse(htmlString))
        val expected= listOf( "Amateur", "Blonde", "Hardcore", "MILF", "Anal", "Popular With Women", "German", "Step Fantasy", "HD",
                "mom","mother","milf fucks young guy","bathroom fuck","german milf amateur","german blonde milf","step mom shower",
                "stepmom fucks son","hot morning fuck","milf fucks step son")

        assertThat(page.contentMetadata.tags.map { t->t.name }).containsAll(expected)
    }


    @Test
    fun `should extract expected actors from page`() {
        val page = PhVideoPage(Jsoup.parse(htmlString))
        val expected= listOf( "Candy Samira")

        assertThat(page.contentMetadata.actors.map { t->t.name }).containsAll(expected)
    }

    @Test
    fun `should extract expected download url from page`() {
        val page = PhVideoPage(Jsoup.parse(htmlString))
        val expected=  "https://cv.phncdn.com/videos/201604/18/74203371/720P_1500K_74203371.mp4?utFp2B8Xx1LPyxtaRjSQiJr5AkRwv_V3JZQuU-Oq7HDNOkvybpfawnUoyJrLds62wLG9zPF-fc3_wpAeL6pcw02T0tBBgYYBkfqc1uwKJsA-HioeCOfetfXiImeR4lqaivwshft10UOx6UaSsTPWWOFfhskNeEai2LWS6rpmhYNbVka1gNP2Q0uhs-JrRcJzdBxs_1C5"
        assertThat(page.downloadMetadata.selectedStreams.first().url).isEqualTo(expected)
    }
}