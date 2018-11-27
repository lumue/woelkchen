import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import org.apache.http.Header
import org.apache.http.HttpStatus
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.message.BasicHeader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.nio.charset.Charset
import java.nio.file.Files
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.system.measureTimeMillis

fun URL.getContentLength(): Long {


    var conn: URLConnection? = null
    return try {
        conn = openConnection()
        if (conn is HttpURLConnection) {
            conn.requestMethod = "HEAD"
        }
        conn!!.getInputStream()
        conn.contentLength.toLong()
    } catch (e: IOException) {
        -1L
    } finally {
        if (conn is HttpURLConnection) {
            conn.disconnect()
        }
    }
}


suspend fun CloseableHttpClient.getContentAsString(url: String, additionalHeaders: Map<String, String> = mapOf()): String {
    return suspendCancellableCoroutine {
        try {
            val result=
            this.use { httpClient ->
                val get = HttpGet(url)
                get.addHeaders(additionalHeaders)
                httpClient.execute(get).use{ response ->
                    if (response.statusLine.statusCode != 200)
                        throw RuntimeException("http error : ${response.statusLine}")
                    val out = ByteArrayOutputStream()
                    response.entity.writeTo(out)
                     String(out.toByteArray(), Charset.defaultCharset())
                }
            }
            it.resume(result)
        }
        catch (e:Throwable){
            it.resumeWithException(e)
        }
    }
}

fun HttpGet.addHeaders(headers: Map<String,String>){
    headers.entries.stream()
            .map { h -> BasicHeader(h.key, h.value) }
            .forEach { h -> addHeader(h) }
}

@InternalCoroutinesApi
suspend fun CloseableHttpClient.download(url: String,
                                         headers: Map<String, String>,
                                         filename: String,
                                         progressConsumer: ((readBytes: Long, time: Long, totalBytes: Long) -> Unit)?) : io.github.lumue.woelkchen.download.FileDownloadResult {
    use { httpClient ->

        val get = HttpGet(url)
        get.addHeaders(headers)


        val file = File(filename)
        if (file.exists()) {
            get.resumeAt=Files.size(file.toPath())
        }


        return suspendCancellableCoroutine {
            try {
                httpClient.execute(get).use { response ->

                    val status = response.statusLine.statusCode
                    if (status in 200..299) {
                        val entity = response.entity
                        val expectedSize = entity.contentLength
                        var append = false
                        var downloadedBytes = get.resumeAt;
                        if (get.resumeAt > 0L) {
                            if (status != HttpStatus.SC_PARTIAL_CONTENT) {
                                Files.deleteIfExists(File(filename).toPath())
                                downloadedBytes=0L
                            } else {
                                append = true
                            }
                        }


                        var time=0L
                        FileOutputStream(filename, append).use { outputStream ->
                            entity.content.use { inputStream ->
                                var bytesRead = 0
                                val buffer = ByteArray(4096)
                                time += measureTimeMillis { bytesRead = inputStream.read(buffer) }
                                downloadedBytes += bytesRead;
                                progressConsumer?.invoke(downloadedBytes, time, expectedSize)
                                while (bytesRead != -1 && isActive) {
                                    time += measureTimeMillis {
                                        outputStream.write(buffer, 0, bytesRead)
                                        bytesRead = inputStream.read(buffer)
                                    }
                                    downloadedBytes += bytesRead
                                    progressConsumer?.invoke(downloadedBytes, time, expectedSize)
                                }
                            }
                        }
                        it.resume(io.github.lumue.woelkchen.download.FileDownloadResult(url, filename, expectedSize, downloadedBytes, time))
                    } else {
                        if (status == HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE)
                            Files.deleteIfExists(File(filename).toPath())
                        it.resumeWithException(RuntimeException("Unexpected response status: $status"))
                    }
                }
            } catch (e: Throwable) {
                it.resumeWithException(e)
            }
        }
    }
}

private var HttpGet.resumeAt: Long
    get() {
        val rangeHeader = findResumeAtRangeHeader()
        return if(rangeHeader!=null) {
            rangeHeader.value
                    .removeSuffix("-")
                    .removePrefix("bytes=")
                    .toLong()
        }
        else
            0L
    }
    set(value) {
        val header = findResumeAtRangeHeader()
        if(header !=null)
            removeHeader(header)
        if(value>0)
            addHeader("Range", "bytes=$value-")
    }

private fun HttpGet.findResumeAtRangeHeader(): Header? {
    return getHeaders("Range")
            .filter { header -> header.value.startsWith("bytes=") && header.value.endsWith("-") }
            .firstOrNull()
}
