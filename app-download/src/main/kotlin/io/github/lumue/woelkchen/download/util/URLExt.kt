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

