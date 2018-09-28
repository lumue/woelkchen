package io.github.lumue.mc.dlservice.sites.xh

import com.fasterxml.jackson.databind.ObjectMapper
import getContentLength
import io.github.lumue.mc.dlservice.resolve.LocationMetadata
import io.github.lumue.mc.dlservice.resolve.LocationMetadataResolver
import io.github.lumue.mc.dlservice.resolve.MediaLocation
import org.slf4j.LoggerFactory
import java.net.URL

class XhResolver(val xhHttpClient:XhHttpClient) : LocationMetadataResolver {

    val videoModelParser : VideoModelParser=VideoModelParser()

    val objectMapper:ObjectMapper= ObjectMapper()

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    override fun resolveMetadata(l: MediaLocation): LocationMetadata {
        logger.debug("resolving metadata for location "+l)
        val videoModelJson = l.getVideoModel()
        return LocationMetadata(
                l.url,
                videoModelJson.extractContentMetadata(),
                videoModelJson.extractDownloadMetadata()
        )
    }


    private fun MediaLocation.getVideoModel(): VideoModel{
        return videoModelParser.fromHtml(xhHttpClient.getContentAsString(url))
    }


    private fun VideoModel.extractDownloadMetadata(): LocationMetadata.DownloadMetadata {
        val streams = this.sources.mp4.asSequence()
                .map { s -> extractStreamInfo(s.key) }
                .sortedByDescending { streamInfo -> streamInfo.id }


        val selectedStream = streams.firstOrNull()!!
        val selectedStreams= listOf(selectedStream)
        val additionalStreams=streams.minusElement(selectedStream).toList()
        return LocationMetadata.DownloadMetadata(selectedStreams,additionalStreams)
    }

    private fun VideoModel.extractStreamInfo(streamId: String): LocationMetadata.MediaStreamMetadata {
        val url =  URL(this.sources.mp4[streamId])
        val expectedSize = url.getContentLength()
        return LocationMetadata.MediaStreamMetadata(streamId, url.toExternalForm(), mapOf(),LocationMetadata.ContentType.CONTAINER,"mp4","mp4", expectedSize)
    }

    private fun VideoModel.extractContentMetadata(): LocationMetadata.ContentMetadata {
        return LocationMetadata.ContentMetadata(this.title?:"",this.description)
    }
}
