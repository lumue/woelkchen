package io.github.lumue.woelkchen.download.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.github.lumue.woelkchen.download.LocationMetadataWriter
import io.github.lumue.woelkchen.download.MediaLocation
import io.github.lumue.woelkchen.download.sites.xh.XhHttpClient
import io.github.lumue.woelkchen.download.sites.xh.XhResolver
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.experimental.CoroutineContext


private val objectMapper: ObjectMapper = ObjectMapper().registerModule(JavaTimeModule())

private val xhResolver: XhResolver = XhResolver(XhHttpClient())

private val locationMetadataWriter: LocationMetadataWriter = LocationMetadataWriter(objectMapper)

private val logger: Logger = LoggerFactory.getLogger("io.github.lumue.woelkchen.download.util.RefreshXhamsterMetadataTask")

private val threadpoolContext: CoroutineContext = newFixedThreadPoolContext(30, "load-meta-json-worker")

fun main(args: Array<String>) {

        val processFiles = ProcessFiles(
                context = threadpoolContext,
                fileFilter = { it.isInfoJsonFile },
                handleFile = { refreshXhamsterMetadata(it) }
        )

        processFiles("mnt/media/adult")
    }



suspend fun refreshXhamsterMetadata(infojsonfile: File) {
    try {
        logger.debug("processing $infojsonfile")
        val json = infojsonfile.asJsonNode()
        val filename = infojsonfile.absolutePath.replace(".info.json", ".meta.json")
        if (json.isXhamsterInfoJson() ) {
            val metadata = xhResolver.resolveMetadata(MediaLocation(json.webpageUrl.textValue()))
            logger.debug("writing $metadata to $filename")
            val out = FileOutputStream(filename)
            locationMetadataWriter.write(metadata, out)
            out.close()
        }
    } catch (e: Throwable) {
        val message = e.message
        if (message != null) {
            if (message.endsWith("410 Gone")) {
                logger.warn("video zu ${infojsonfile.absolutePath} nicht mehr online verfügbar ")
            } else
                logger.error(message, e)
        }
    }

}


private fun JsonNode.isXhamsterInfoJson(): Boolean {
    val node = webpageUrl
    if (!node.isTextual)
        return false

    return node.textValue().contains("xhamster.com")
}

private val JsonNode.webpageUrl: JsonNode
    get() {
        return get("webpage_url")
    }

private fun File.asJsonNode(): JsonNode {
    val jsonNode = objectMapper.readTree(this)
            ?: throw IllegalArgumentException("${this.absolutePath} does not contain valid json to bind")
    return jsonNode
}

private val File.isInfoJsonFile: Boolean
    get() {
        return this.exists() && !this.isDirectory && this.name.endsWith("info.json")
    }
