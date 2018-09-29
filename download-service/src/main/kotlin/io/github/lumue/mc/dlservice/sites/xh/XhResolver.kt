package io.github.lumue.mc.dlservice.sites.xh

import io.github.lumue.mc.dlservice.LocationMetadata
import io.github.lumue.mc.dlservice.LocationMetadataResolver
import io.github.lumue.mc.dlservice.MediaLocation
import org.slf4j.LoggerFactory


class XhResolver(val xhHttpClient:XhHttpClient) : LocationMetadataResolver {

    val videoModelParser : VideoModelParser=VideoModelParser()

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



}
