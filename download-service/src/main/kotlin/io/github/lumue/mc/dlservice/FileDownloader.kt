package io.github.lumue.mc.dlservice

interface FileDownloader {
    suspend fun download(m: LocationMetadata,
                         targetPath: String,
                         progressHandler: ((readBytes: Long, time: Long, totalBytes: Long) -> Unit)?): FileDownloadResult
}

data class FileDownloadResult(val url: String,
                              val filename: String,
                              val expectedSize: Long,
                              val downloadedBytes: Long,
                              val time: Long)