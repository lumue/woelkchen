package io.github.lumue.mc.dlservice.sites.xh

import getContentAsString
import org.apache.http.client.CookieStore
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.HttpClientBuilder
import org.slf4j.LoggerFactory

class XhHttpClient(val username:String ="",val password:String="") {

    val cookieStore : CookieStore = BasicCookieStore()

    val httpClientBuilder: HttpClientBuilder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore)

    val loggedIn: Boolean
    get():Boolean{
        return cookieStore.names().contains("UID")
    }

    val logger=LoggerFactory.getLogger(XhHttpClient::class.java)

    fun getContentAsString(url: String): String {

        if(!username.isEmpty()&&!loggedIn)
            login()

        val httpClient = httpClientBuilder.build()

        return httpClient.getContentAsString(url)

    }

    fun login(){
        logger.debug("cookies before login: "+cookieStore.names())
        httpClientBuilder.build().use {client->
            val get= HttpGet("https://de.xhamster.com/x-api?r=%5B%7B%22name%22%3A%22authorizedUserModelFetch%22%2C%22requestData%22%3A%7B%22%24id%22%3A%22bec152e4-f9ca-47c2-adec-e058d04160e2%22%2C%22id%22%3Anull%2C%22trusted%22%3Atrue%2C%22username%22%3A%22$username%22%2C%22password%22%3A%22$password%22%2C%22remember%22%3A1%2C%22redirectURL%22%3Anull%7D%7D%5D")
            client.execute(get).use{response->
                if(response.statusLine.statusCode!=200)
                    throw RuntimeException("http error : ${response.statusLine}")
            }
        }
        logger.debug("cookies after login: "+cookieStore.names())
    }

    private fun CookieStore.names(): List<String> {
        var cookienames = mutableListOf<String>()
        cookies.forEach({
            cookienames.add(it.name)
        })
        return cookienames
    }

}
