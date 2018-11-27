package io.github.lumue.woelkchen.download

import download
import getContentAsString
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import org.apache.http.client.CookieStore
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.cookie.BasicClientCookie
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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


        val targetfile = targetPath + File.separator + m.contentMetadata.title + "." + m.downloadMetadata.selectedStreams[0].filenameExtension
        logger.debug("downloading from " + m.downloadMetadata.selectedStreams[0].url + " to " + targetfile)


        return httpClientBuilder.build().download(m.downloadMetadata.selectedStreams[0].url, m.downloadMetadata.selectedStreams[0].headers, targetfile, progressHandler)
    }

    fun addCookie( name: String, value: String, domain: String) {
        val basicClientCookie = BasicClientCookie(name, value)
        basicClientCookie.domain=domain
        cookieStore.addCookie(basicClientCookie)
    }

}

fun CookieStore.names(): List<String> {
    var cookienames = mutableListOf<String>()
    cookies.forEach({
        cookienames.add(it.name)
    })
    return cookienames
}
