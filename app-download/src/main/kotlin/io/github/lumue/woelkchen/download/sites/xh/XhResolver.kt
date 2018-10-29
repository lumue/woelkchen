package io.github.lumue.woelkchen.download.sites.xh

import io.github.lumue.woelkchen.download.LocationMetadata
import io.github.lumue.woelkchen.download.ResolveMetadataStep
import io.github.lumue.woelkchen.download.MediaLocation
import org.slf4j.LoggerFactory


class XhResolver(val xhHttpClient:XhHttpClient) : ResolveMetadataStep {

    val videoModelParser : VideoModelParser=VideoModelParser()

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    override suspend fun resolveMetadata(l: MediaLocation): LocationMetadata {
        logger.debug("resolving metadata for location "+l)
        val videoModelJson = l.getVideoModel()
        return LocationMetadata(
                l.url,
                videoModelJson.extractContentMetadata(),
                videoModelJson.extractDownloadMetadata()
        )
    }


    private suspend fun MediaLocation.getVideoModel(): VideoModel{
        val htmlAsString = xhHttpClient.getContentAsString(url.replace("//de.","//"))
        return videoModelParser.fromHtml(htmlAsString)
    }



}
