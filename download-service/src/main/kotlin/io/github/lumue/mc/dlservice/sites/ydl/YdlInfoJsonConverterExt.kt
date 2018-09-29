package io.github.lumue.mc.dlservice.sites.ydl

import io.github.lumue.mc.dlservice.LocationMetadata
import io.github.lumue.ydlwrapper.metadata.single_info_json.Format
import io.github.lumue.ydlwrapper.metadata.single_info_json.HttpHeaders
import io.github.lumue.ydlwrapper.metadata.single_info_json.RequestedFormat
import io.github.lumue.ydlwrapper.metadata.single_info_json.YdlInfoJson
import org.springframework.util.StringUtils
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import kotlin.streams.toList

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

private fun YdlInfoJson.toContentMetadata(): LocationMetadata.ContentMetadata {
    return LocationMetadata.ContentMetadata(title,description)
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