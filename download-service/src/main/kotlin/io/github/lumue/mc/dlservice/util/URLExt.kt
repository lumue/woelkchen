import io.github.lumue.mc.dlservice.resolve.MediaLocation
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection

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

fun MediaLocation.getJsoupDocument():Document{
    return Jsoup.connect(this.url).get()
}
