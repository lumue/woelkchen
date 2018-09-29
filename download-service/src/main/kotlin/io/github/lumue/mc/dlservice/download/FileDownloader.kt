package io.github.lumue.mc.dlservice.download

import download
import io.github.lumue.mc.dlservice.LocationMetadata
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
                         progressHandler: ((readBytes: Long,time:Long, totalBytes: Long) -> Unit)?): FileDownloadResult {

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
                     progressConsumer: ((readBytes: Long,time:Long ,totalBytes: Long) -> Unit)?,
                     httpClientBuilder: HttpClientBuilder=HttpClientBuilder.create()) : Job{

        var download = async{

            try {
                val closeableHttpClient = httpClientBuilder
                        .build()
                        .download(url, headers, filename, progressConsumer)

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
