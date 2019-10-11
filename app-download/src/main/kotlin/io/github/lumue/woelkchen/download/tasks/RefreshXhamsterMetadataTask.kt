package io.github.lumue.woelkchen.download.tasks

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.github.lumue.woelkchen.download.LocationMetadataWriter
import io.github.lumue.woelkchen.download.MediaLocation
import io.github.lumue.woelkchen.download.sites.ph.PhHttpClient
import io.github.lumue.woelkchen.download.sites.ph.PhResolver
import io.github.lumue.woelkchen.download.sites.xh.XhHttpClient
import io.github.lumue.woelkchen.download.sites.xh.XhResolver
import io.github.lumue.woelkchen.download.sites.ydl.isPornhubInfoJson
import io.github.lumue.woelkchen.download.sites.ydl.webpageUrl
import kotlinx.coroutines.newFixedThreadPoolContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.CoroutineContext


private val objectMapper: ObjectMapper = ObjectMapper().registerModule(JavaTimeModule())

private val resolver: XhResolver = XhResolver(XhHttpClient())

private val locationMetadataWriter: LocationMetadataWriter = LocationMetadataWriter(objectMapper)

private val logger: Logger = LoggerFactory.getLogger("io.github.lumue.woelkchen.download.util.RefreshXhamsterMetadataTask")

private val threadpoolContext: CoroutineContext = newFixedThreadPoolContext(30, "load-meta-json-worker")

fun main(args: Array<String>) {

    val processFiles = ProcessFiles(
            context = threadpoolContext,
            fileFilter = { it.isInfoJsonFile||it.isMetaJsonFile },
            handleFile = { refreshMetadata(it) }
    )

    processFiles("/mnt/debian-sdb/video/")
}


suspend fun refreshMetadata(metadatafile: File) {
    try {
        logger.debug("processing $metadatafile")
        val json = metadatafile.asJsonNode()
        val filename = metadatafile.absolutePath.replace(".info.json", ".meta.json")
        val metadata = resolver.retrieveMetadata(MediaLocation(json.webpageUrl.textValue()))
        logger.debug("writing $metadata to $filename")
        val out = FileOutputStream(filename)
        locationMetadataWriter.write(metadata, out)
        out.close()
    } catch (e: Throwable) {
        val message = e.message
        if (message != null && message.endsWith("410 Gone")) {
                logger.warn("video zu ${metadatafile.absolutePath} nicht mehr online verfügbar ")
        }
        else
            logger.error("error processing ${metadatafile.name}", e)
    }
}


private fun File.asJsonNode(): JsonNode {
    val jsonNode = objectMapper.readTree(this)
            ?: throw IllegalArgumentException("${this.absolutePath} does not contain valid json to bind")
    return jsonNode
}

private val File.isInfoJsonFile: Boolean
    get() {
        return this.exists()
                && !this.isDirectory
                && this.name.endsWith("info.json")
                && asJsonNode().isPornhubInfoJson()
    }


private val File.isMetaJsonFile: Boolean
    get() {
        return this.exists()
                && !this.isDirectory
                && this.name.endsWith("meta.json")
    }