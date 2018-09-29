import io.github.lumue.mc.dlservice.MediaLocation
import io.github.lumue.mc.dlservice.download.ApacheHttpFileDownloader
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import org.apache.http.HttpStatus
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.message.BasicHeader
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.nio.charset.Charset
import java.nio.file.Files
import kotlin.system.measureTimeMillis

public fun URL.getContentLength(): Long {


    var conn: URLConnection? = null
    try {
        conn = openConnection()
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

fun MediaLocation.getJsoupDocument(): Document {
    return Jsoup.connect(this.url).get()
}

fun CloseableHttpClient.getContentAsString(url: String): String {
    this.use {
        val get = HttpGet(url)
        execute(get).use { response ->
            if (response.statusLine.statusCode != 200)
                throw RuntimeException("http error : ${response.statusLine}")
            val out = ByteArrayOutputStream()
            response.entity.writeTo(out)
            return String(out.toByteArray(), Charset.defaultCharset())
        }
    }
}

fun CloseableHttpClient.download(url: String,
                                 headers: Map<String, String>,
                                 filename: String,
                                 progressConsumer: ((readBytes: Long,time:Long ,totalBytes: Long) -> Unit)?) {
    this.use { httpClient ->

        val get = HttpGet(url)


        var resumeAt: Long = 0

        val file = File(filename)
        if (file.exists()) {
            resumeAt = Files.size(file.toPath())
        }

        headers.entries.stream()
                .map { h -> BasicHeader(h.key, h.value) }
                .forEach { h -> get.addHeader(h) }

        if (resumeAt > 0L) {
            get.addHeader("Range", "bytes=$resumeAt-")
        }



        httpClient.execute(get).use { response ->
            val status = response.getStatusLine().getStatusCode()
            if (status >= 200 && status < 300) {
                val entity = response.getEntity()
                val expectedSize = entity.contentLength
                var append = false

                if (resumeAt > 0L) {
                    if (status != HttpStatus.SC_PARTIAL_CONTENT) {
                        Files.deleteIfExists(File(filename).toPath())
                    } else {
                        append = true
                    }
                }

                var downloadedBytes = resumeAt;
                FileOutputStream(filename, append).use { outputStream ->
                    entity.getContent().use { inputStream ->
                        var bytesRead=0
                        val buffer = ByteArray(4096)
                        var time= measureTimeMillis { bytesRead = inputStream.read(buffer)}
                        downloadedBytes=downloadedBytes+bytesRead;
                        progressConsumer?.invoke(downloadedBytes, time,expectedSize)
                        while (bytesRead != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                            time+= measureTimeMillis { bytesRead = inputStream.read(buffer)}
                            downloadedBytes = downloadedBytes + bytesRead
                            progressConsumer?.invoke(downloadedBytes, time,expectedSize)
                        }
                    }
                }
            } else {
                if (status == HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE)
                    Files.deleteIfExists(File(filename).toPath())
                throw RuntimeException("Unexpected response status: $status")
            }
        }
    }
}
