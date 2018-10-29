package io.github.lumue.woelkchen.download.sites.xh

import io.github.lumue.woelkchen.download.BasicHttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient

val loginToXh = fun(u: String,
                    p: String,
                    hc: CloseableHttpClient) {
    hc.use { client ->
        val get = HttpGet("https://de.xhamster.com/x-api?r=%5B%7B%22name%22%3A%22authorizedUserModelFetch%22%2C%22requestData%22%3A%7B%22%24id%22%3A%22bec152e4-f9ca-47c2-adec-e058d04160e2%22%2C%22id%22%3Anull%2C%22trusted%22%3Atrue%2C%22username%22%3A%22$u%22%2C%22password%22%3A%22$p%22%2C%22remember%22%3A1%2C%22redirectURL%22%3Anull%7D%7D%5D")
        client.execute(get).use { response ->
            if (response.statusLine.statusCode != 200)
                throw RuntimeException("http error : ${response.statusLine}")
        }
    }
}


class XhHttpClient(
        username: String = "",
        password: String = ""
) : BasicHttpClient(
        username = username,
        password = password,
        performLoginHttpCall = loginToXh
)








