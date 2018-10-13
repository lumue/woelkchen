package io.github.lumue.mc.dlservice

interface FileDownload {
    suspend operator fun  invoke(m: LocationMetadata,
                         targetPath: String,
                         progressHandler: ((readBytes: Long, time: Long, totalBytes: Long) -> Unit)?):
            FileDownloadResult
}

data class FileDownloadResult(val url: String,
                              val filename: String,
                              val expectedSize: Long,
                              val downloadedBytes: Long,
                              val time: Long)