package io.github.lumue.woelkchen.download.sites.ydl

import com.fasterxml.jackson.databind.JsonNode
import io.github.lumue.woelkchen.download.LocationMetadata
import io.github.lumue.ydlwrapper.metadata.single_info_json.Format
import io.github.lumue.ydlwrapper.metadata.single_info_json.HttpHeaders
import io.github.lumue.ydlwrapper.metadata.single_info_json.RequestedFormat
import io.github.lumue.ydlwrapper.metadata.single_info_json.YdlInfoJson
import org.springframework.util.StringUtils
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.swing.text.DateFormatter
import kotlin.streams.toList

const val infoJsonFileSuffix = ".info.json"

fun YdlInfoJson.toLocationMetadata(): LocationMetadata {
    return LocationMetadata(this.webpageUrl, this.toContentMetadata(), this.toDownloadMetadata())
}

private fun YdlInfoJson.toDownloadMetadata(): LocationMetadata.DownloadMetadata {
    val availableStreams = this.formats
            .map { format -> format.toStreamMetadata() }
            .toMutableList()

    val isMergedFormats = requestedFormats != null && requestedFormats.size > 1
    val selectedStreams = (
            if (isMergedFormats)
                this.requestedFormats.stream()
                        .map { rf->rf.toStreamMetadata()}
            else
                formats.stream()
                        .filter{ format -> format.formatId == formatId }
                        .map { fo->fo.toStreamMetadata() }
            ).toList()

    return LocationMetadata.DownloadMetadata(selectedStreams,availableStreams)
}

private fun RequestedFormat.toStreamMetadata(): LocationMetadata.MediaStreamMetadata {
    var type = LocationMetadata.ContentType.CONTAINER
    var codec = this.ext
    if (!StringUtils.isEmpty(vcodec) && !StringUtils.isEmpty(acodec)) {
        if ("none" == acodec) {
            type = LocationMetadata.ContentType.VIDEO
            codec = vcodec
        } else if ("none" == vcodec) {
            type = LocationMetadata.ContentType.AUDIO
            codec = acodec
        }
    }

    return LocationMetadata.MediaStreamMetadata(
            this.formatId,
            this.url,
            this.httpHeaders.toMap(),
            type,
            codec,
            ext,
            filesize.toLong())
}

private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

private fun YdlInfoJson.toContentMetadata(): LocationMetadata.ContentMetadata {
    return LocationMetadata.ContentMetadata(title, description,
            downloaded = LocalDateTime.now(),
            uploaded = LocalDateTime.of(LocalDate.parse(this.uploadDate, dateTimeFormatter), LocalTime.MIDNIGHT),
            hoster = "",
            votes = 0,
            duration = Duration.ofSeconds(this.duration.toLong()))
}

private fun HttpHeaders.toMap(): Map<String, String> {
    val map = HashMap<String, String>()
    map["Accept"] = this.accept
    map["AcceptCharset"] = this.acceptCharset
    map["AcceptEncoding"] = this.acceptEncoding
    map["AcceptLanguage"] = this.acceptLanguage
    this.additionalProperties.entries.forEach { e -> map[e.key] = e.value.toString() }
    map["UserAgent"] = this.userAgent
    return map
}

private fun Format.toStreamMetadata(): LocationMetadata.MediaStreamMetadata {

    var type = LocationMetadata.ContentType.CONTAINER
    var codec = this.ext
    if (!StringUtils.isEmpty(vcodec) && !StringUtils.isEmpty(acodec)) {
        if ("none" == acodec) {
            type = LocationMetadata.ContentType.VIDEO
            codec = vcodec
        } else if ("none" == vcodec) {
            type = LocationMetadata.ContentType.AUDIO
            codec = acodec
        }
    }

    val expectedSize = filesize?.toLong() ?: getFilesizeFromUrl(url)
    return LocationMetadata.MediaStreamMetadata(
            this.formatId,
            this.url,
            this.httpHeaders.toMap(),
            type,
            codec,
            ext,
            expectedSize)


}


private fun getFilesizeFromUrl(urlstring: String): Long {
    val url: URL
    try {
        url = URL(urlstring)
    } catch (e: MalformedURLException) {
        return 0
    }

    var conn: URLConnection? = null
    try {
        conn = url.openConnection()
        if (conn is HttpURLConnection) {
            conn.requestMethod = "HEAD"
        }
        conn!!.getInputStream()
        return conn.contentLength.toLong()
    } catch (e: IOException) {
        return -1L
    } finally {
        if (conn is HttpURLConnection) {
            conn.disconnect()
        }
    }
}

val File.isInfoJson: Boolean
    get() {
        return this.exists()
                && !this.isDirectory
                && this.name.endsWith(infoJsonFileSuffix)
    }


fun JsonNode.isXhamsterInfoJson(): Boolean {
    val node = webpageUrl
    if (!node.isTextual)
        return false

    return node.textValue().contains("pornhub")
}


fun JsonNode.isPornhubInfoJson(): Boolean {
    val node = webpageUrl
    if (!node.isTextual)
        return false

    return node.textValue().contains("pornhub")
}

 val JsonNode.webpageUrl: JsonNode
    get() {
        return get("webpage_url")
    }
