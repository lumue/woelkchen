package io.github.lumue.mc.dlservice.sites.xh

import io.github.lumue.mc.dlservice.FileDownloadResult
import io.github.lumue.mc.dlservice.FileDownloader
import io.github.lumue.mc.dlservice.LocationMetadata
import io.github.lumue.mc.dlservice.util.xhHttpClient

class XhDownloader(val httpClient: XhHttpClient): FileDownloader{
    override suspend fun download(m: LocationMetadata,
                                  targetPath: String,
                                  progressHandler: ((readBytes: Long, time: Long, totalBytes: Long) -> Unit)?)
            : FileDownloadResult {
        return xhHttpClient.download(m,targetPath,progressHandler)
    }
}