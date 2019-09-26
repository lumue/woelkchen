package io.github.lumue.woelkchen.download

import kotlinx.coroutines.*
import org.apache.http.Header
import org.apache.http.HttpStatus
import org.apache.http.client.CookieStore
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.cookie.BasicClientCookie
import org.apache.http.message.BasicHeader
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.Charset
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.system.measureTimeMillis

open class BasicHttpClient(val password: String = "",
                           val username: String = "",
                           private val cookieStore: CookieStore = BasicCookieStore(),
                           val performLoginHttpCall: (p: String, u: String, hc: CloseableHttpClient) -> Any=fun(_: String, _: String, _: CloseableHttpClient) {},
                           val hasAuthenticatedUserCall: (cs: CookieStore)->Boolean= fun (_: CookieStore) : Boolean{
    return true
}

) {
    val logger = LoggerFactory.getLogger(BasicHttpClient::class.java)!!


    protected val httpClientBuilder: HttpClientBuilder = HttpClientBuilder.create()
            .setDefaultCookieStore(cookieStore)

    val loggedIn: Boolean
        get():Boolean {
            return hasAuthenticatedUserCall(cookieStore)
        }



    val loggingIn: AtomicBoolean = AtomicBoolean(false)
    suspend fun getContentAsString(url: String,additionalHeaders: Map<String,String> = mapOf()): String {

        if (!username.isEmpty() && !loggedIn)
            login()


        val httpClient = httpClientBuilder.build()

        return httpClient.getContentAsString(url,additionalHeaders )

    }

    suspend fun login() {

        while (!loggingIn.compareAndSet(false, true)) {
            logger.debug("login already started. waiting...")
            delay(TimeUnit.SECONDS.toMillis(1))
        }

        if (!loggedIn) {
            (suspendCancellableCoroutine<Any> {
                try {
                    performLoginHttpCall(username, password, httpClientBuilder.build())
                    it.resume(Any())
                } catch (e: Throwable) {
                    logger.error(e.message, e)
                    it.resumeWithException(e)
                } finally {
                    loggingIn.set(false)
                }
            })

        } else {
            logger.debug("already logged in")
            loggingIn.set(false)
        }
    }


    @InternalCoroutinesApi
    suspend fun download(m: LocationMetadata,
                         targetPath: String,
                         progressHandler: ((readBytes: Long, time: Long, totalBytes: Long) -> Unit)?): FileDownloadResult {

        if (!username.isEmpty() && !loggedIn)
            login()


        val targetfile = targetPath + File.separator + (m.contentMetadata.title).replace("/","-") + "." + m.downloadMetadata.selectedStreams[0].filenameExtension
        logger.debug("downloading from " + m.downloadMetadata.selectedStreams[0].url + " to " + targetfile)


        return httpClientBuilder.build().download(m.downloadMetadata.selectedStreams[0].url, m.downloadMetadata.selectedStreams[0].headers, targetfile, progressHandler)
    }

    fun addCookie( name: String, value: String, domain: String) {
        val basicClientCookie = BasicClientCookie(name, value)
        basicClientCookie.domain=domain
        cookieStore.addCookie(basicClientCookie)
    }


    @InternalCoroutinesApi
    suspend fun CloseableHttpClient.download(url: String,
                                             headers: Map<String, String>,
                                             filename: String,
                                             progressConsumer: ((readBytes: Long, time: Long, totalBytes: Long) -> Unit)?) : FileDownloadResult {
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
                            var downloadedBytes = get.resumeAt
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
                                    downloadedBytes += bytesRead
                                    progressConsumer?.invoke(downloadedBytes, time, expectedSize)
                                    while (bytesRead != -1 && NonCancellable.isActive) {
                                        time += measureTimeMillis {
                                            outputStream.write(buffer, 0, bytesRead)
                                            bytesRead = inputStream.read(buffer)
                                        }
                                        downloadedBytes += bytesRead
                                        progressConsumer?.invoke(downloadedBytes, time, expectedSize)
                                    }
                                }
                            }
                            it.resume(FileDownloadResult(url, filename, expectedSize, downloadedBytes, time))
                        } else {
                            val msg = "Unexpected response status: $status on $get"
                            logger.error(msg)
                            if (status == HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE)
                                Files.deleteIfExists(File(filename).toPath())
                            it.resumeWithException(RuntimeException(msg))
                        }
                    }
                } catch (e: Throwable) {
                    logger.error("unexpected error downloading $url to $filename ",e)
                    it.resumeWithException(e)
                }
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
                logger.error("unexpected error getting content from $url as String ",e)
                it.resumeWithException(e)
            }
        }
    }



}

fun HttpGet.addHeaders(headers: Map<String,String>){
    headers.entries.stream()
            .map { h -> BasicHeader(h.key, h.value) }
            .forEach { h -> addHeader(h) }
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

fun CookieStore.names(): List<String> {
    val cookienames = mutableListOf<String>()
    cookies.forEach({
        cookienames.add(it.name)
    })
    return cookienames
}

