package io.github.lumue.woelkchen.download

interface SiteClient {


    suspend fun retrieveMetadata(l: MediaLocation): LocationMetadata
    suspend fun downloadContent(metadata: LocationMetadata, targetPath: String, progressHandler: ((readBytes: Long, time: Long, totalBytes: Long) -> Unit)?): FileDownloadResult
}