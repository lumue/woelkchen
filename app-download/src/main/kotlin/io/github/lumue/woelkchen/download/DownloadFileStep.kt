package io.github.lumue.woelkchen.download

import io.github.lumue.woelkchen.shared.metadata.MoviepageMetadata

interface DownloadFileStep {
    suspend fun  downloadContent(m: MoviepageMetadata,
                                 targetPath: String,
                                 progressHandler: ((readBytes: Long, time: Long, totalBytes: Long) -> Unit)?):
            io.github.lumue.woelkchen.download.FileDownloadResult
}

data class FileDownloadResult(val url: String,
                              val filename: String,
                              val expectedSize: Long,
                              val downloadedBytes: Long,
                              val time: Long)