import io.github.lumue.mc.dlservice.resolve.MediaLocation
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.StringWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.nio.charset.Charset

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

fun CloseableHttpClient.getContentAsString( url:String):String{
    this.use {
        val get=HttpGet(url)
        execute(get).use{response->
            if(response.statusLine.statusCode!=200)
                throw RuntimeException("http error : ${response.statusLine}")
            val out = ByteArrayOutputStream()
            response.entity.writeTo(out)
            return String(out.toByteArray(), Charset.defaultCharset())
        }
    }
}