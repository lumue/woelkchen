package io.github.lumue.mc.dlservice.sites.xh
import com.fasterxml.jackson.databind.ObjectMapper
import getContentLength
import io.github.lumue.mc.dlservice.resolve.LocationMetadata
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URL
import java.time.Duration

class VideoModelParser{

    val objectMapper : ObjectMapper= ObjectMapper()

    fun fromHtml(htmlAsString: String):VideoModel{
        val doc= Jsoup.parse(htmlAsString)
        return objectMapper.readValue(doc.initialsJsonString,PageInitials::class.java).videoModel
    }




    val Document.initialsJsonString: String
    get():String{
        val initialsScriptString=run {
            //2. Parses and scrapes the HTML response
            select("#initials-script").first().dataNodes().first().wholeData
        }
        val startOfInitialsJson = initialsScriptString.indexOfFirst { c -> '{' == c }
        val initialsJsonString = initialsScriptString.substring(startOfInitialsJson)
        return initialsJsonString
    }


}
fun VideoModel.extractDownloadMetadata(): LocationMetadata.DownloadMetadata {
    val streams = this.sources.mp4.asSequence()
            .map { s -> extractStreamInfo(s.key) }
            .sortedByDescending { streamInfo -> streamInfo.id }


    val selectedStream = streams.firstOrNull()!!
    val selectedStreams= listOf(selectedStream)
    val additionalStreams=streams.minusElement(selectedStream).toList()
    return LocationMetadata.DownloadMetadata(selectedStreams,additionalStreams)
}

fun VideoModel.extractStreamInfo(streamId: String): LocationMetadata.MediaStreamMetadata {
    val url =  URL(this.sources.mp4[streamId])
    val expectedSize = url.getContentLength()
    return LocationMetadata.MediaStreamMetadata(streamId, url.toExternalForm(), mapOf(), LocationMetadata.ContentType.CONTAINER,"mp4","mp4", expectedSize)
}

fun VideoModel.extractContentMetadata(): LocationMetadata.ContentMetadata {
    return LocationMetadata.ContentMetadata(this.title?:"",
            this.description,
            this.tags,
            this.actors,
            Duration.ofSeconds(this.duration),
            this.views)
}

private val VideoModel.actors: Set<LocationMetadata.ContentMetadata.Actor>
    get() {
        return categories
                .filter { category -> category.pornstar  }
                .map { category -> LocationMetadata.ContentMetadata.Actor(category.url,category.name) }
                .toCollection(mutableSetOf())
    }

private val VideoModel.tags: Set<LocationMetadata.ContentMetadata.Tag>
    get() {
        return categories
                .filter { category -> !category.pornstar  }
                .map { category -> LocationMetadata.ContentMetadata.Tag(category.url,category.name) }
                .toCollection(mutableSetOf())
    }
