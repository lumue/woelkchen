package io.github.lumue.mc.dlservice.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.lumue.mc.dlservice.LocationMetadataWriter
import io.github.lumue.mc.dlservice.MediaLocation
import io.github.lumue.mc.dlservice.sites.xh.XhHttpClient
import io.github.lumue.mc.dlservice.sites.xh.XhResolver
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import kotlinx.coroutines.experimental.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.io.FileTreeWalk


val objectMapper: ObjectMapper = ObjectMapper()

val xhHttpClient: XhHttpClient = XhHttpClient("dirtytom74", "ddl85s")

val xhResolver: XhResolver = XhResolver(xhHttpClient)

val locationMetadataWriter: LocationMetadataWriter = LocationMetadataWriter(objectMapper)

val logger: Logger = LoggerFactory.getLogger("io.github.lumue.mc.dlservice.util.RefreshXhamsterMetadataTask")

val coroutineContext : CoroutineContext= newFixedThreadPoolContext(30,"load-meta-json-worker")

fun main(args: Array<String>) {


    val rootPath = File(args[0])
    if (!rootPath.exists() || !rootPath.isDirectory)
        return

    runBlocking {
        val producer = produce {
            rootPath.walkBottomUp()
                    .filter { it.isInfoJsonFile }
                    .forEach { send(it) }
            close()
        }

        val consumer = List(20) {
            async (coroutineContext){
                for (infojsonfile in producer) {
                    try {
                        logger.debug("processing $infojsonfile")
                        val json = infojsonfile.asJsonNode()
                        val filename = infojsonfile.absolutePath.replace(".info.json", ".meta.json")
                        if (json.isXhamsterInfoJson() && !File(filename).exists() ) {
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
            }
        }

        consumer.forEach { it.join() }
    }
}


private fun JsonNode.isXhamsterInfoJson(): Boolean {
    val node = webpageUrl
    if ( !node.isTextual)
        return false

    return node.textValue().contains("xhamster.com")
}

private val JsonNode.webpageUrl: JsonNode
    get() {
        return get("webpage_url")
    }

private fun File.asJsonNode(): JsonNode {
    val jsonNode = objectMapper.readTree(this) ?: throw IllegalArgumentException("${this.absolutePath} does not contain valid json to bind")
    return jsonNode
}

private val File.isInfoJsonFile: Boolean
    get() {
        return this.exists() && !this.isDirectory && this.name.endsWith("info.json")
    }
