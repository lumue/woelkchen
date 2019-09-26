package io.github.lumue.woelkchen.download.sites.xh

import com.fasterxml.jackson.databind.ObjectMapper
import getContentLength
import io.github.lumue.woelkchen.download.LocationMetadata
import io.github.lumue.woelkchen.download.ResolveMetadataStep
import io.github.lumue.woelkchen.download.MediaLocation
import io.github.lumue.woelkchen.download.sites.xh.XhVideoPage.InitialsJson.VideoModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.RuntimeException
import java.net.URL
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset


class XhResolver(private val xhHttpClient: XhHttpClient) : ResolveMetadataStep {

    private val xhVideoPageParser: XhVideoModelParser = XhVideoModelParser()

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    override suspend fun retrieveMetadata(l: MediaLocation): LocationMetadata {
        logger.debug("resolving metadata for location $l")
        val videoPage = l.getPage()
        return LocationMetadata(
                l.url,
                videoPage.initialsJson.videoModel.extractContentMetadata(),
                videoPage.extractDownloadMetadata()
        )
    }


    private suspend fun MediaLocation.getPage(): XhVideoPage {
        val htmlAsString = xhHttpClient.getContentAsString(url.replace("//de.", "//"))
        return xhVideoPageParser.fromHtml(htmlAsString)
    }


}

class XhVideoModelParser {

    private val objectMapper: ObjectMapper = ObjectMapper()

    val logger: Logger = LoggerFactory.getLogger(XhVideoModelParser::class.java)

    fun fromHtml(htmlAsString: String): XhVideoPage {
        val doc = Jsoup.parse(htmlAsString)
        val initialsJsonString = doc.initialsJsonString
        val ldJsonString = doc.ldJsonString
        return try {
            val initialsJson = objectMapper.readValue(initialsJsonString, XhVideoPage.InitialsJson::class.java)
            val ldJson = objectMapper.readValue(ldJsonString, XhVideoPage.LdJson::class.java)
            XhVideoPage(ldJson, initialsJson)
        } catch (e: Throwable) {
            val msg = "error parsing json content from page object"
            logger.error(msg, e)
            throw RuntimeException(msg, e)
        }
    }


}


private val Document.initialsJsonString: String
    get():String {
        val scriptString = run {
            //2. Parses and scrapes the HTML response
            select("#initials-script") ?.first()
                    ?.dataNodes()
                    ?.first()
                    ?.wholeData
        }
                ?: return "{}"
        val startOfInitialsJson = scriptString.indexOfFirst { c -> '{' == c }
        return scriptString.substring(startOfInitialsJson)
    }

private val Document.ldJsonString: String
    get():String {
        val scriptString = run {
            //2. Parses and scrapes the HTML response
            getElementsByAttributeValue("type", "application/ld+json")
                    ?.first()
                    ?.dataNodes()
                    ?.first()
                    ?.wholeData
        }
                ?: return "{}"
        val startOfJson = scriptString.indexOfFirst { c -> '{' == c }
        return scriptString.substring(startOfJson)
    }


fun XhVideoPage.extractDownloadMetadata(): LocationMetadata.DownloadMetadata {
    val streams = this.initialsJson.videoModel.sources.mp4.asSequence()
            .map { s -> extractStreamInfo(s.key) }
            .sortedByDescending { streamInfo -> streamInfo.id }


    val selectedStream = streams.firstOrNull()!!
    val selectedStreams = listOf(selectedStream)
    val additionalStreams = streams.minusElement(selectedStream).toList()
    return LocationMetadata.DownloadMetadata(selectedStreams, additionalStreams)
}

fun XhVideoPage.extractStreamInfo(streamId: String): LocationMetadata.MediaStreamMetadata {
    val url = URL(this.initialsJson.videoModel.sources.download[streamId]?.link)
    val expectedSize = url.getContentLength()
    return LocationMetadata.MediaStreamMetadata(streamId, url.toExternalForm(), mapOf(), LocationMetadata.ContentType.CONTAINER, "mp4", "mp4", expectedSize)
}

fun VideoModel.extractContentMetadata(): LocationMetadata.ContentMetadata {
    return LocationMetadata.ContentMetadata(this.title ?: "",
            description = this.description,
            tags = this.tags,
            actors = this.actors,
            duration = Duration.ofSeconds(this.duration),
            views = this.views,
            uploaded = LocalDateTime.ofEpochSecond(this.created, 0, ZoneOffset.UTC),
            downloaded = LocalDateTime.now(),
            hoster = "xhamster",
            votes = this.rating.likes
    )
}

private val VideoModel.actors: Set<LocationMetadata.ContentMetadata.Actor>
    get() {
        return categories
                .filter { category -> category.name != null && category.url != null }
                .filter { category -> category.pornstar }
                .map { category -> LocationMetadata.ContentMetadata.Actor(category.url!!, category.name!!) }
                .toCollection(mutableSetOf())
    }

private val VideoModel.tags: Set<LocationMetadata.ContentMetadata.Tag>
    get() {
        return categories
                .filter { category -> category.name != null && category.url != null }
                .filter { category -> !category.pornstar }
                .map { category -> LocationMetadata.ContentMetadata.Tag(category.url!!, category.name!!) }
                .toCollection(mutableSetOf())
    }








