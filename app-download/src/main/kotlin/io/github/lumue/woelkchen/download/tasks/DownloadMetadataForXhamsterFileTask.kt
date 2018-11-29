package io.github.lumue.woelkchen.download.tasks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.github.lumue.woelkchen.download.LocationMetadataWriter
import io.github.lumue.woelkchen.download.MediaLocation
import io.github.lumue.woelkchen.download.locationMetadataFileSuffix
import io.github.lumue.woelkchen.download.sites.xh.XhHttpClient
import io.github.lumue.woelkchen.download.sites.xh.XhResolver
import kotlinx.coroutines.newFixedThreadPoolContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import kotlin.coroutines.CoroutineContext


private val objectMapper: ObjectMapper = ObjectMapper().registerModule(JavaTimeModule())

private val xhResolver: XhResolver = XhResolver(XhHttpClient())

private val locationMetadataWriter: LocationMetadataWriter = LocationMetadataWriter(objectMapper)

private val logger: Logger = LoggerFactory.getLogger("io.github.lumue.woelkchen.download.util.RefreshXhamsterMetadataTask")

private val threadpoolContext: CoroutineContext = newFixedThreadPoolContext(30, "load-meta-json-worker")

fun main(args: Array<String>) {

    val processFiles = ProcessFiles(
            context = threadpoolContext,
            fileFilter = { it.isXhamsterVideo },
            handleFile = { handleFile(it) }
    )

    processFiles("/mnt/nasbox/media/adult/")
}

suspend fun handleFile(videofile: File) {

    try {
        val dirName = videofile.parent
        logger.debug("processing $videofile")
        val videoURL = "https://xhamster.com/videos/${videofile.videoId}"
        logger.debug("video url for $videofile is $videoURL")
        val metadata = xhResolver.retrieveMetadata(MediaLocation(videoURL))
        val metadataFilename = dirName + File.separator + metadata.contentMetadata.title + locationMetadataFileSuffix
        logger.debug("writing $metadata to $metadataFilename")
        val out = FileOutputStream(metadataFilename)
        locationMetadataWriter.write(metadata, out)
        out.close()
        val newVideoFilename = dirName + File.separator + metadata.contentMetadata.title + ".mp4"
        logger.debug("moving video from $videofile to $newVideoFilename")
        Files.move(videofile.toPath(), File(newVideoFilename).toPath())
    } catch (e: Throwable) {
        val message = e.message
        if (message != null) {
            if (message.endsWith("410 Gone")) {
                logger.warn("video zu ${videofile.absolutePath} nicht mehr online verfügbar ")
            } else
                logger.error(message, e)
        }
    }

}




private val File.videoId: String
    get() {
        val parts = this.name.split("_")
        if(parts[0]=="xhamster.com")
            return parts[1]
        return parts[0]
    }

private val File.isXhamsterVideo: Boolean
    get() {
        return this.exists()
                && !this.isDirectory
                && this.name.endsWith("mp4")
                && this.name.contains("_")
                && this.videoId.isNumeric

    }
private val String.isNumeric: Boolean
    get() {return this.firstOrNull { c->!c.isDigit() }==null}
