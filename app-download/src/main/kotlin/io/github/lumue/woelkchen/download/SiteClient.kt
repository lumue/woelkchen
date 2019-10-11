package io.github.lumue.woelkchen.download

import io.github.lumue.woelkchen.shared.metadata.MoviepageMetadata

interface SiteClient {
    /**
     * retrieve metadata available at specified location
     */
    suspend fun downloadMetadata(l: MediaLocation): MoviepageMetadata

    /**
     * download stream to target directory
     */
    suspend fun downloadContent(
            metadata: MoviepageMetadata,
            targetPath: String,
            progressHandler: ((readBytes: Long, time: Long, totalBytes: Long) -> Unit)?
    ): FileDownloadResult
}