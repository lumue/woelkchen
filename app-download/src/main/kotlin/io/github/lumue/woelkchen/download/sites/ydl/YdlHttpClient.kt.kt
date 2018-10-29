package io.github.lumue.woelkchen.download.sites.ydl

import io.github.lumue.woelkchen.download.BasicHttpClient
import org.apache.http.impl.client.CloseableHttpClient


val loginToYdl = fun(_: String,_: String,_: CloseableHttpClient) {}


class YdlHttpClient(
        username: String = "",
        password: String = ""
) : BasicHttpClient(
        username = username,
        password = password,
        performLoginHttpCall = loginToYdl
)
