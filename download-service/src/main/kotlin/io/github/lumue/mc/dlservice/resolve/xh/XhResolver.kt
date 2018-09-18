package io.github.lumue.mc.dlservice.resolve.xh

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.lumue.mc.dlservice.resolve.LocationMetadata
import io.github.lumue.mc.dlservice.resolve.LocationMetadataResolver
import io.github.lumue.mc.dlservice.resolve.MediaLocation
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection

class XhResolver : LocationMetadataResolver {

    val objectMapper: ObjectMapper = ObjectMapper()

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    override fun resolveMetadata(l: MediaLocation): LocationMetadata {
        logger.debug("resolving metadata for location "+l)
        val initialsJson = getInitialsJsonForURL(l)["videoModel"]
        return LocationMetadata(
                l.url,
                extractContentMetadata(initialsJson),
                extractDownloadMetadata(initialsJson)
        )
    }

    fun getInitialsJsonForURL(l: MediaLocation): JsonNode {
        val initialsScriptString = Jsoup.connect(l.url).get().run {
            //2. Parses and scrapes the HTML response
            select("#initials-script").first().dataNodes().first().wholeData
        }
        val startOfInitialsJson = initialsScriptString.indexOfFirst { c -> '{' == c }
        val initialsJsonString=initialsScriptString.substring(startOfInitialsJson)
        val initialsJson = objectMapper.readTree(initialsJsonString)
        return initialsJson
    }

    private fun extractDownloadMetadata(initialsJson: JsonNode): LocationMetadata.DownloadMetadata {
        val streams = initialsJson["sources"]["mp4"].asSequence()
                .map { s -> extractStreamInfo(s) }
                .sortedByDescending { streamInfo -> streamInfo.id }


        val selectedStream = streams.firstOrNull()!!
        val selectedStreams= listOf(selectedStream)
        val additionalStreams=streams.minusElement(selectedStream).toList()
        return LocationMetadata.DownloadMetadata(selectedStreams,additionalStreams)
    }

    private fun extractStreamInfo(node: JsonNode): LocationMetadata.MediaStreamMetadata {
        val url = node.textValue()
        val expectedSize = getFilesizeFromUrl(url)
        return LocationMetadata.MediaStreamMetadata(node.toString(), url, mapOf(),LocationMetadata.ContentType.CONTAINER,"mp4","mp4", expectedSize)
    }

    private fun extractContentMetadata(initialsJson: JsonNode): LocationMetadata.ContentMetadata {
        return LocationMetadata.ContentMetadata(initialsJson["title"].textValue(),initialsJson["description"].textValue())
    }
}
private fun getFilesizeFromUrl(urlstring: String): Long {
    val url: URL
    try {
        url = URL(urlstring)
    } catch (e: MalformedURLException) {
        return 0
    }

    var conn: URLConnection? = null
    try {
        conn = url.openConnection()
        if (conn is HttpURLConnection) {
            conn.requestMethod = "HEAD"
        }
        conn!!.getInputStream()
        return conn.contentLength.toLong()
    } catch (e: IOException) {
        return -1L
    } finally {
        if (conn is HttpURLConnection) {
            conn.disconnect()
        }
    }
}