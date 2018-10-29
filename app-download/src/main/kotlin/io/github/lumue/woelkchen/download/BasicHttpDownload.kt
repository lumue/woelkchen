package io.github.lumue.woelkchen.download

class BasicHttpDownload(private val httpClient: BasicHttpClient): io.github.lumue.woelkchen.download.DownloadFileStep {
    override suspend fun invoke(m: LocationMetadata,
                                targetPath: String,
                                progressHandler: ((readBytes: Long, time: Long, totalBytes: Long) -> Unit)?)
            : io.github.lumue.woelkchen.download.FileDownloadResult {
        return httpClient.download(m,targetPath,progressHandler)
    }

}