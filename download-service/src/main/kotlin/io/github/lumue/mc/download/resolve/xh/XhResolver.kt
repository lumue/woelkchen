package io.github.lumue.mc.download.resolve.xh

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.lumue.mc.download.resolve.LocationMetadata
import io.github.lumue.mc.download.resolve.LocationMetadataResolver
import io.github.lumue.mc.download.resolve.MediaLocation
import org.jsoup.Jsoup

class XhResolver : LocationMetadataResolver {

    val objectMapper: ObjectMapper = ObjectMapper()

    override suspend fun resolveMetadata(l: MediaLocation): LocationMetadata {
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
         return LocationMetadata.MediaStreamMetadata(node.toString(),node.textValue(), mapOf(),LocationMetadata.ContentType.CONTAINER,"mp4","mp4",0)
    }

    private fun extractContentMetadata(initialsJson: JsonNode): LocationMetadata.ContentMetadata {
        return LocationMetadata.ContentMetadata(initialsJson["title"].textValue(),initialsJson["description"].textValue())
    }
}