package io.github.lumue.woelkchen.download.sites.xh

import io.github.lumue.woelkchen.download.BasicHttpClient
import io.github.lumue.woelkchen.download.addHeaders
import io.github.lumue.woelkchen.download.names
import org.apache.http.client.CookieStore
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient

private val xhLoginRequestHeaders: Map<String, String> = mapOf(
        "accept" to "*/*",
        "accept-encoding" to "gzip, deflate, br",
        "accept-language" to "de-DE,de;q=0.9,en-US;q=0.8,en;q=0.7,es;q=0.6",
        "cache-control" to "no-cache",
        "content-type" to "text/plain",
        "pragma" to "no-cache",
        "referer" to "https://de.xhamster.com/",
        "user-agent" to "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36",
        "x-requested-with" to " XMLHttpRequest"
)

class XhHttpClient(
        username: String = "",
        password: String = ""
) : BasicHttpClient(
        username = username,
        password = password,
        performLoginHttpCall = fun(u: String,
                                   p: String,
                                   hc: CloseableHttpClient) {
            hc.use { client ->
                val get = HttpGet("https://de.xhamster.com/x-api?r=%5B%7B%22name%22%3A%22authorizedUserModelFetch%22%2C%22requestData%22%3A%7B%22%24id%22%3A%22bec152e4-f9ca-47c2-adec-e058d04160e2%22%2C%22id%22%3Anull%2C%22trusted%22%3Atrue%2C%22username%22%3A%22$u%22%2C%22password%22%3A%22$p%22%2C%22remember%22%3A1%2C%22redirectURL%22%3Anull%7D%7D%5D")
                get.addHeaders(xhLoginRequestHeaders)
                client.execute(get).use { response ->
                    if (response.statusLine.statusCode != 200)
                        throw RuntimeException("http error : ${response.statusLine}")
                }
            }
        },
        hasAuthenticatedUserCall = fun(cookieStore: CookieStore): Boolean {
            return cookieStore.names().contains("UID")
        }
)








