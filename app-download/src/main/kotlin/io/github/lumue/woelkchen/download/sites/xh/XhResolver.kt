package io.github.lumue.woelkchen.download.sites.xh

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import getContentLength
import io.github.lumue.woelkchen.download.LocationMetadata
import io.github.lumue.woelkchen.download.ResolveMetadataStep
import io.github.lumue.woelkchen.download.MediaLocation
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.RuntimeException
import java.net.URL
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset


class XhResolver(val xhHttpClient:XhHttpClient) : ResolveMetadataStep {

    private val xhVideoPageParser : XhVideoPageParser=XhVideoPageParser()

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    override suspend fun retrieveMetadata(l: MediaLocation): LocationMetadata {
        logger.debug("resolving metadata for location "+l)
        val videoModelJson = l.getVideoModel()
        return LocationMetadata(
                l.url,
                videoModelJson.extractContentMetadata(),
                videoModelJson.extractDownloadMetadata()
        )
    }


    private suspend fun MediaLocation.getVideoModel(): XhVideoPageModel{
        val htmlAsString = xhHttpClient.getContentAsString(url.replace("//de.","//"))
        return xhVideoPageParser.fromHtml(htmlAsString)
    }



}

class XhVideoPageParser{

    val objectMapper : ObjectMapper = ObjectMapper()

    val logger : Logger =LoggerFactory.getLogger(XhVideoPageParser::class.java)

    fun fromHtml(htmlAsString: String):XhVideoPageModel{
        val doc= Jsoup.parse(htmlAsString)
        val initialsJsonString = doc.initialsJsonString
        return try{
            objectMapper.readValue(initialsJsonString,PageInitials::class.java).videoModel
        }catch (e: Throwable){
            val msg="error parsing $initialsJsonString to PageInitials object"
            logger.error(msg,e)
            throw RuntimeException(msg,e)
        }
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
fun XhVideoPageModel.extractDownloadMetadata(): LocationMetadata.DownloadMetadata {
    val streams = this.sources.mp4.asSequence()
            .map { s -> extractStreamInfo(s.key) }
            .sortedByDescending { streamInfo -> streamInfo.id }


    val selectedStream = streams.firstOrNull()!!
    val selectedStreams= listOf(selectedStream)
    val additionalStreams=streams.minusElement(selectedStream).toList()
    return LocationMetadata.DownloadMetadata(selectedStreams,additionalStreams)
}

fun XhVideoPageModel.extractStreamInfo(streamId: String): LocationMetadata.MediaStreamMetadata {
    val url =  URL(this.sources.download[streamId]?.link)
    val expectedSize = url.getContentLength()
    return LocationMetadata.MediaStreamMetadata(streamId, url.toExternalForm(), mapOf(), LocationMetadata.ContentType.CONTAINER,"mp4","mp4", expectedSize)
}

fun XhVideoPageModel.extractContentMetadata(): LocationMetadata.ContentMetadata {
    return LocationMetadata.ContentMetadata(this.title?:"",
            description=this.description,
            tags=this.tags,
            actors=this.actors,
            duration = Duration.ofSeconds(this.duration),
            views=this.views,
            uploaded = LocalDateTime.ofEpochSecond(this.created,0, ZoneOffset.UTC),
            downloaded = LocalDateTime.now(),
            hoster = "xhamster",
            votes=this.rating.likes
    )
}

private val XhVideoPageModel.actors: Set<LocationMetadata.ContentMetadata.Actor>
    get() {
        return categories
                .filter{category -> category.name!=null&&category.url!=null }
                .filter { category -> category.pornstar  }
                .map { category -> LocationMetadata.ContentMetadata.Actor(category.url!!, category.name!!) }
                .toCollection(mutableSetOf())
    }

private val XhVideoPageModel.tags: Set<LocationMetadata.ContentMetadata.Tag>
    get() {
        return categories
                .filter{category -> category.name!=null&&category.url!=null }
                .filter { category -> !category.pornstar  }
                .map { category -> LocationMetadata.ContentMetadata.Tag(category.url!!, category.name!!) }
                .toCollection(mutableSetOf())
    }



@JsonIgnoreProperties(ignoreUnknown = true)
data class XhVideoPageModel(
        @JsonProperty("duration") val duration: Long = 0,
        @JsonProperty("title") val title: String? = "",
        @JsonProperty("pageURL") val pageURL: String = "",
        @JsonProperty("icon") val icon: Any? = Any(),
        @JsonProperty("spriteURL") val spriteURL: String = "",
        @JsonProperty("trailerURL") val trailerURL: String = "",
        @JsonProperty("rating") val rating: Rating = Rating(),
        @JsonProperty("isVR") val isVR: Boolean = false,
        @JsonProperty("isHD") val isHD: Boolean = false,
        @JsonProperty("isUHD") val isUHD: Boolean = false,
        @JsonProperty("created") val created: Long = 0,
        @JsonProperty("modelName") val modelName: String = "",
        @JsonProperty("thumbURL") val thumbURL: String = "",
        @JsonProperty("id") val id: Int = 0,
        @JsonProperty("views") val views: Int = 0,
        @JsonProperty("comments") val comments: Int = 0,
        @JsonProperty("modified") val modified: Int = 0,
        @JsonProperty("orientation") val orientation: String = "",
        @JsonProperty("secured") val secured: Int = 0,
        @JsonProperty("status") val status: Int = 0,
        @JsonProperty("description") val description: String = "",
        @JsonProperty("mp4File") val mp4File: String = "",
        @JsonProperty("spriteCount") val spriteCount: Int = 0,
        @JsonProperty("sources") val sources: Sources = Sources(),
        @JsonProperty("dimensions") val dimensions: JsonNode?=null,
        @JsonProperty("categories") val categories: List<Category> = listOf(),
        @JsonProperty("sponsor") val sponsor: JsonNode?=null,
        @JsonProperty("reported") val reported: Boolean = false,
        @JsonProperty("editable") val editable: Boolean = false,
        @JsonProperty("subscriptionModel") val subscriptionModel: SubscriptionModel = SubscriptionModel(),
        @JsonProperty("author") val author: Author = Author()
) {




    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Category(
            @JsonProperty("name") val name: String? = "",
            @JsonProperty("url") val url: String? = "",
            @JsonProperty("pornstar") val pornstar: Boolean = false,
            @JsonProperty("id") val id: String? = "",
            @JsonProperty("description") val description: Any? = Any()
    )


    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Sources(
            @JsonProperty("download") val download: Map<String,Download> = mapOf(),
            @JsonProperty("mp4") val mp4: Map<String,String> = mapOf(),
            @JsonProperty("flv") val flv: Map<String,String>? = mapOf()
    ) {


        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Download(
                @JsonProperty("link") val link: String = "",
                @JsonProperty("size") val size: Double = 0.0
        )

    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Rating(
            @JsonProperty("modelName") val modelName: String = "",
            @JsonProperty("value") val value: Int = 0,
            @JsonProperty("entityModel") val entityModel: String = "",
            @JsonProperty("entityID") val entityID: Int = 0,
            @JsonProperty("likes") val likes: Int = 0,
            @JsonProperty("dislikes") val dislikes: Int = 0,
            @JsonProperty("state") val state: Int = 0
    )





    @JsonIgnoreProperties(ignoreUnknown = true)
    data class SubscriptionModel(
            @JsonProperty("modelName") val modelName: String = "",
            @JsonProperty("id") val id: Int = 0,
            @JsonProperty("subscribed") val subscribed: Boolean = false,
            @JsonProperty("subscribers") val subscribers: Int = 0
    )


    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Author(
            @JsonProperty("modelName") val modelName: String = "",
            @JsonProperty("id") val id: Int = 0,
            @JsonProperty("pageURL") val pageURL: String = "",
            @JsonProperty("retired") val retired: Boolean = false,
            @JsonProperty("verified") val verified: Boolean = false,
            @JsonProperty("name") val name: String = ""
    )



}

