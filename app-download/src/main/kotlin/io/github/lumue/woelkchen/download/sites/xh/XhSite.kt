package io.github.lumue.woelkchen.download.sites.xh

import io.github.lumue.woelkchen.download.*
import java.io.File
import java.io.FileOutputStream

class XhSite:SiteClient {

    val httpClient: XhHttpClient = XhHttpClient("dirtytom74", "ddl85s")

    val downloader: DownloadFileStep = BasicHttpDownload(httpClient)
    val resolver: ResolveMetadataStep = XhResolver(httpClient)

    override suspend fun downloadMetadata(l: MediaLocation): LocationMetadata {
        return resolver.retrieveMetadata(l)
    }

    override suspend fun downloadContent(metadata: LocationMetadata, targetPath: String, progressHandler: ((readBytes: Long, time: Long, totalBytes: Long) -> Unit)?): FileDownloadResult {

        val downloadResult = downloader.downloadContent(metadata,
                targetPath,
                progressHandler
        )
        metadata.writeToFile(targetPath + File.separator + (metadata.contentMetadata.title).replace("/","-") + ".meta.json")
        return downloadResult
    }
}