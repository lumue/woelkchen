package io.github.lumue.mc.dlservice.download

import io.github.lumue.mc.dlservice.resolve.LocationMetadata
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import org.apache.http.HttpStatus
import org.apache.http.client.CookieStore
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicHeader
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files

class FileDownloader {

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    private val cookieStore: CookieStore =BasicCookieStore()

    suspend fun download(m: LocationMetadata.MediaStreamMetadata,
                         targetPath: String,
                         progressHandler: ((readBytes: Long, totalBytes: Long) -> Unit)?): FileDownloadResult {

        val targetfile = targetPath + File.separator + m.contentType + "." + m.filenameExtension
        logger.debug("downloading from " + m.url + " to " + targetfile)

        val job =  ApacheHttpFileDownloader().downloadFile(m.url, m.headers,targetfile, progressHandler)

        job.join()

        return FileDownloadResult()
    }

}


class ApacheHttpFileDownloader {

    fun downloadFile(url: String,
                     headers: Map<String, String>,
                     filename: String,
                     progressConsumer: ((readBytes: Long, totalBytes: Long) -> Unit)?,
                     httpClientBuilder: HttpClientBuilder=HttpClientBuilder.create()) : Job{

        var download = async{

            try {
                val closeableHttpClient = httpClientBuilder
                        .build()
                closeableHttpClient.use { httpClient ->

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
                                    var bytesRead: Int
                                    val buffer = ByteArray(BUFFER_SIZE)
                                    bytesRead = inputStream.read(buffer)
                                    while (bytesRead != -1 && isActive) {

                                        outputStream.write(buffer, 0, bytesRead)
                                        downloadedBytes = downloadedBytes + bytesRead
                                        progressConsumer?.invoke(downloadedBytes, expectedSize)
                                        bytesRead = inputStream.read(buffer)

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
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }

        return download;

    }

    companion object {

        private val LOGGER = LoggerFactory.getLogger(ApacheHttpFileDownloader::class.java)

        private val BUFFER_SIZE = 1024 * 16
    }

}


class FileDownloadResult {

}
