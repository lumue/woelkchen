package io.github.lumue.woelkchen.download.sites.ydl

import io.github.lumue.woelkchen.download.BasicHttpClient


class YdlHttpClient(
        username: String = "",
        password: String = ""
) : BasicHttpClient(
        username = username,
        password = password
)
