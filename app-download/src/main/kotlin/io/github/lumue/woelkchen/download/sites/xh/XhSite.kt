package io.github.lumue.woelkchen.download.sites.xh

import io.github.lumue.woelkchen.download.*
import java.io.FileOutputStream

class XhSite:SiteClient {

    val httpClient: XhHttpClient = XhHttpClient("dirtytom74", "ddl85s")

    val downloader: io.github.lumue.woelkchen.download.DownloadFileStep = BasicHttpDownload(httpClient)
    val resolver: ResolveMetadataStep = XhResolver(httpClient)

    override suspend fun retrieveMetadata(l: MediaLocation): LocationMetadata {
        return resolver.retrieveMetadata(l)
    }

    override suspend fun downloadContent(metadata: LocationMetadata, targetPath: String, progressHandler: ((readBytes: Long, time: Long, totalBytes: Long) -> Unit)?): FileDownloadResult {

        val downloadResult = downloader.downloadContent(metadata,
                targetPath,
                progressHandler
        )
        val out = FileOutputStream(downloadResult.filename + ".meta.json")
        LocationMetadataWriter().write(metadata, out)
        out.close()
        return downloadResult
    }
}