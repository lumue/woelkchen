package io.github.lumue.woelkchen.download

interface SiteClient {
    /**
     * retrieve metadata available at specified location
     */
    suspend fun downloadMetadata(l: MediaLocation): LocationMetadata

    /**
     * download stream to target directory
     */
    suspend fun downloadContent(
            metadata: LocationMetadata,
            targetPath: String,
            progressHandler: ((readBytes: Long, time: Long, totalBytes: Long) -> Unit)?
    ): FileDownloadResult
}