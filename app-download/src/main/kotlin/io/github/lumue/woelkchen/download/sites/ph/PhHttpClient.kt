package io.github.lumue.woelkchen.download.sites.ph

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.lumue.woelkchen.download.*
import org.jsoup.nodes.Element
import java.io.FileOutputStream

class PhSite : SiteClient{
    val httpClient: PhHttpClient = PhHttpClient()

    val downloader: io.github.lumue.woelkchen.download.DownloadFileStep = BasicHttpDownload(httpClient)
    val resolver: ResolveMetadataStep = PhResolver(httpClient)

    override suspend fun retrieveMetadata(l: MediaLocation): LocationMetadata {
        return resolver.retrieveMetadata(l)
    }

    override suspend fun downloadContent(metadata: LocationMetadata,
                                         targetPath: String,
                                         progressHandler: ((readBytes: Long, time: Long, totalBytes: Long) -> Unit)?)
            : FileDownloadResult {

        val downloadResult = downloader.downloadContent(metadata,
                targetPath,
                progressHandler
        )
        val out = FileOutputStream(downloadResult.filename + ".meta.json")
        LocationMetadataWriter().write(metadata, out)
        out.close()
        return downloadResult
    }

}

class PhHttpClient(
        username: String = "",
        password: String = ""
) : BasicHttpClient(
        username = username,
        password = password
)


class PhResolver(val httpClient: PhHttpClient) : ResolveMetadataStep {


    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    override suspend fun retrieveMetadata(l: MediaLocation): LocationMetadata {
        logger.debug("resolving metadata for location " + l)
        val page = PhVideoPage(loadVideoPageDocument(l))


        val contentMetadata = page.contentMetadata
            val downloadMetadata = page.downloadMetadata
            return LocationMetadata(
                    l.url,
                    contentMetadata,
                    downloadMetadata
            )
        }

    private suspend fun loadVideoPageDocument(l: MediaLocation): Document {
        val contentAsString = l.contentAsString()
        return Jsoup.parse(contentAsString)
    }

    private suspend fun MediaLocation.contentAsString(): String? {
        return this@PhResolver.httpClient.getContentAsString(url)
    }
}

private fun Document.isPhVideoPage() = select("#player").isNotEmpty()


class PhVideoPage(val document: Document){

    init {
        if(!document.isPhVideoPage())
            throw ResolveException("Document ${document.pretty} does not seem to be a pornhub videopage")
    }



    val contentMetadata by lazy { document.extractContentMetadata() }
    val downloadMetadata by lazy { document.extractDownloadMetadata() }
}

private val Document.pretty: String
    get() {
        outputSettings().prettyPrint(true)
        return html()
    }

private fun Document.extractDownloadMetadata(): LocationMetadata.DownloadMetadata {
    val playerJson=extractPlayerJson()
    val selected=playerJson.mediaDefinitions.filter { md->md.defaultQuality }
            .map {md->md.toMediaStreamMetaData()  }
    val additional=playerJson.mediaDefinitions.filter { md->!md.defaultQuality }
            .map { md->md.toMediaStreamMetaData() }
    return LocationMetadata.DownloadMetadata(selected,additional)
}

private fun PlayerJson.MediaDefinition.toMediaStreamMetaData(): LocationMetadata.MediaStreamMetadata {
   return LocationMetadata.MediaStreamMetadata(quality,videoUrl, mapOf(), LocationMetadata.ContentType.CONTAINER,"mp4","mp4", 0)
}

private fun Document.extractPlayerJson(): PlayerJson {
    val playerDiv = select("#player")
    val scriptElements = playerDiv.select("script")
    val scriptElement = scriptElements.first()
    val playerScript = scriptElement.dataNodes().first().wholeData
    val from=playerScript.indexOfFirst { c -> '{' == c}
    val to=playerScript.indexOf("};")+1
    val jsonString=playerScript.substring(from,to)
    try {
        val json = ObjectMapper().readValue(jsonString, PlayerJson::class.java)
        return json
    }
    catch (e: Throwable){
        throw ResolveException("error parsing $jsonString to json ",e)
    }
}


private fun Document.extractContentMetadata(): LocationMetadata.ContentMetadata {
    return LocationMetadata.ContentMetadata(
            title = extractTitle(),
            description = extractDescription(),
            hoster = "pornhub",
            tags = extractTags(),
            actors = extractActors()
    )
}

private fun Document.extractActors(): Set<LocationMetadata.ContentMetadata.Actor> {
    val actors = select(".pornstarsWrapper").select("a")
            .map { a -> LocationMetadata.ContentMetadata.Actor(a.attr("href").toString(), a.text()) }
    return actors.toMutableSet()
}

private fun Document.extractTags(): Set<LocationMetadata.ContentMetadata.Tag> {
    val categories = select(".categoriesWrapper").select("a")
            .map { a -> LocationMetadata.ContentMetadata.Tag(a.attr("href").toString(), a.text()) }
    val tags=select(".tagsWrapper").select("a")
            .map { a -> LocationMetadata.ContentMetadata.Tag(a.attr("href").toString(), a.text()) }
   return  (categories+tags).filter { t-> "" != t.id }.toMutableSet()
}

private fun Document.extractTitle(): String {
    return select("meta[property=\"og:title\"]").attr("content").toString()
}

private fun Document.extractDescription(): String {
    return select("meta[property=\"og:description\"]").attr("content").toString()
}

@JsonIgnoreProperties(ignoreUnknown = true)
private data class PlayerJson(
        @JsonProperty("actionTags")
        val actionTags: String = "",
        @JsonProperty("appId")
        val appId: String = "",
        @JsonProperty("autoplay")
        val autoplay: String = "",
        @JsonProperty("autoreplay")
        val autoreplay: String = "",
        @JsonProperty("cdn")
        val cdn: String = "",
        @JsonProperty("cdnProvider")
        val cdnProvider: String = "",
        @JsonProperty("defaultQuality")
        val defaultQuality: List<Int> = listOf(),
        @JsonProperty("disable_sharebar")
        val disableSharebar: Int = 0,
        @JsonProperty("embedCode")
        val embedCode: String = "",
        @JsonProperty("errorReports")
        val errorReports: String = "",
        @JsonProperty("hidePostPauseRoll")
        val hidePostPauseRoll: String = "",
        @JsonProperty("hotspots")
        val hotspots: List<String> = listOf(),
        @JsonProperty("htmlPauseRoll")
        val htmlPauseRoll: String = "",
        @JsonProperty("htmlPostRoll")
        val htmlPostRoll: String = "",
        @JsonProperty("image_url")
        val imageUrl: String = "",
        @JsonProperty("isHD")
        val isHD: String = "",
        @JsonProperty("language")
        val language: String = "",
        @JsonProperty("link_url")
        val linkUrl: String = "",
        @JsonProperty("mediaDefinitions")
        val mediaDefinitions: List<MediaDefinition> = listOf(),
        @JsonProperty("mostviewed_url")
        val mostviewedUrl: String = "",
        @JsonProperty("mp4_seek")
        val mp4Seek: String = "",
        @JsonProperty("nextVideo")
        val nextVideo: NextVideo = NextVideo(),
        @JsonProperty("options")
        val options: String = "",
        @JsonProperty("outBufferLagThreshold")
        val outBufferLagThreshold: Int = 0,
        @JsonProperty("pauseroll_url")
        val pauserollUrl: String = "",
        @JsonProperty("postroll_url")
        val postrollUrl: String = "",
        @JsonProperty("related_url")
        val relatedUrl: String = "",
        @JsonProperty("service")
        val service: String = "",
        @JsonProperty("startLagThreshold")
        val startLagThreshold: Int = 0,
        @JsonProperty("thumbs")
        val thumbs: Thumbs = Thumbs(),
        @JsonProperty("toprated_url")
        val topratedUrl: String = "",
        @JsonProperty("vcServerUrl")
        val vcServerUrl: String = "",
        @JsonProperty("video_duration")
        val videoDuration: String = "",
        @JsonProperty("video_title")
        val videoTitle: String = "",
        @JsonProperty("video_unavailable")
        val videoUnavailable: String = "",
        @JsonProperty("video_unavailable_country")
        val videoUnavailableCountry: String = ""
) {
    data class MediaDefinition(
            @JsonProperty("defaultQuality")
            val defaultQuality: Boolean = false,
            @JsonProperty("format")
            val format: String = "",
            @JsonProperty("quality")
            val quality: String = "",
            @JsonProperty("videoUrl")
            val videoUrl: String = ""
    )

    data class NextVideo(
            @JsonProperty("duration")
            val duration: String = "",
            @JsonProperty("isHD")
            val isHD: String = "",
            @JsonProperty("nextUrl")
            val nextUrl: String = "",
            @JsonProperty("thumb")
            val thumb: String = "",
            @JsonProperty("title")
            val title: String = ""
    )

    data class Thumbs(
            @JsonProperty("cdnType")
            val cdnType: String = "",
            @JsonProperty("samplingFrequency")
            val samplingFrequency: Int = 0,
            @JsonProperty("thumbHeight")
            val thumbHeight: String = "",
            @JsonProperty("thumbWidth")
            val thumbWidth: String = "",
            @JsonProperty("type")
            val type: String = "",
            @JsonProperty("urlPattern")
            val urlPattern: String = ""
    )
}