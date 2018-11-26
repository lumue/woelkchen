package io.github.lumue.woelkchen.download

import kotlinx.coroutines.InternalCoroutinesApi

class BasicHttpDownload(private val httpClient: BasicHttpClient): io.github.lumue.woelkchen.download.DownloadFileStep {
    @InternalCoroutinesApi
    override suspend fun downloadContent(m: LocationMetadata,
                                         targetPath: String,
                                         progressHandler: ((readBytes: Long, time: Long, totalBytes: Long) -> Unit)?)
            : io.github.lumue.woelkchen.download.FileDownloadResult {
        return httpClient.download(m,targetPath,progressHandler)
    }

}