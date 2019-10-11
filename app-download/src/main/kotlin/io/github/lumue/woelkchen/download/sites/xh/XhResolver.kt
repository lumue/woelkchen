package io.github.lumue.woelkchen.download.sites.xh

import com.fasterxml.jackson.databind.ObjectMapper
import getContentLength
import io.github.lumue.woelkchen.download.ResolveMetadataStep
import io.github.lumue.woelkchen.download.MediaLocation
import io.github.lumue.woelkchen.download.sites.xh.XhVideoPage.InitialsJson.VideoModel
import io.github.lumue.woelkchen.shared.metadata.MovieMetadata
import io.github.lumue.woelkchen.shared.metadata.MoviepageMetadata
import io.github.lumue.woelkchen.shared.metadata.Tag
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

    override suspend fun retrieveMetadata(l: MediaLocation): MoviepageMetadata {
        logger.debug("resolving metadata for location $l")
        val videoPage = l.getPage()
        return MoviepageMetadata(
                l.url,
                videoPage.extractContentMetadata(),
                videoPage.extractDownloadMetadata()
        )
    }


    private suspend fun MediaLocation.getPage(): XhVideoPage {
        val htmlAsString = xhHttpClient.getContentAsString(url.replace("//de.", "//"))
        return xhVideoPageParser.fromHtml(htmlAsString)
    }


}

private fun XhVideoPage.extractContentMetadata(): MovieMetadata {
    val tagsFromInitials = this.initialsJson.videoModel.tags
    val tagsFromLdjson=this.ldJson.video?.tags?: mutableSetOf()
    return MovieMetadata(this.initialsJson.videoModel.title ?: "",
            description = this.initialsJson.videoModel.description,
            tags = tagsFromInitials.union(tagsFromLdjson),
            actors = this.initialsJson.videoModel.actors,
            duration = Duration.ofSeconds(this.initialsJson.videoModel.duration),
            views = this.initialsJson.videoModel.views,
            uploaded = LocalDateTime.ofEpochSecond(this.initialsJson.videoModel.created, 0, ZoneOffset.UTC),
            downloaded = LocalDateTime.now(),
            hoster = "xhamster",
            votes = this.initialsJson.videoModel.rating.likes,
            resolution = 0
    )
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


private val XhVideoPage.LdJson.Video.tags: Set<Tag>
    get() {
        return keywords!!
                .filter { k-> k.isNotEmpty() }
                .map { k -> Tag("xhamster-$k", k) }
                .toCollection(mutableSetOf())
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


fun XhVideoPage.extractDownloadMetadata(): MoviepageMetadata.DownloadMetadata {
    val streams = this.initialsJson.videoModel.sources.mp4.asSequence()
            .map { s -> extractStreamInfo(s.key) }
            .sortedByDescending { streamInfo -> streamInfo.id }


    val selectedStream = streams.firstOrNull()!!
    val selectedStreams = listOf(selectedStream)
    val additionalStreams = streams.minusElement(selectedStream).toList()
    return MoviepageMetadata.DownloadMetadata(selectedStreams, additionalStreams)
}

fun XhVideoPage.extractStreamInfo(streamId: String): MoviepageMetadata.MediaStreamMetadata {
    val url = URL(this.initialsJson.videoModel.sources.download[streamId]?.link)
    val expectedSize = url.getContentLength()
    return MoviepageMetadata.MediaStreamMetadata(streamId, url.toExternalForm(), mapOf(), MoviepageMetadata.ContentType.CONTAINER, "mp4", "mp4", expectedSize)
}


private val VideoModel.actors: Set<MovieMetadata.Actor>
    get() {
        return categories
                .filter { category -> category.name != null && category.url != null }
                .filter { category -> category.pornstar }
                .map { category -> MovieMetadata.Actor(category.url!!, category.name!!) }
                .toCollection(mutableSetOf())
    }

private val VideoModel.tags: Set<Tag>
    get() {
        return categories
                .filter { category -> category.name != null && category.url != null }
                .filter { category -> !category.pornstar }
                .map { category -> Tag(category.url!!, category.name!!) }
                .toCollection(mutableSetOf())
    }








