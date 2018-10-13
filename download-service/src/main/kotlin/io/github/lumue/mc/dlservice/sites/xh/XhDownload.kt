package io.github.lumue.mc.dlservice.sites.xh

import io.github.lumue.mc.dlservice.FileDownloadResult
import io.github.lumue.mc.dlservice.FileDownload
import io.github.lumue.mc.dlservice.LocationMetadata

class XhDownload(private val httpClient: XhHttpClient): FileDownload{
    override suspend fun invoke(m: LocationMetadata,
                                targetPath: String,
                                progressHandler: ((readBytes: Long, time: Long, totalBytes: Long) -> Unit)?)
            : FileDownloadResult {
        return httpClient.download(m,targetPath,progressHandler)
    }

}