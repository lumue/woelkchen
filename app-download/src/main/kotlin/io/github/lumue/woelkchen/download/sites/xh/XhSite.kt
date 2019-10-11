package io.github.lumue.woelkchen.download.sites.xh

import io.github.lumue.woelkchen.download.*
import io.github.lumue.woelkchen.shared.metadata.MoviepageMetadata
import java.io.File

class XhSite(
        val username: String = "",
        val password: String = ""
):SiteClient {

    private val httpClient: XhHttpClient = XhHttpClient(username, password)

    private val downloader: DownloadFileStep = BasicHttpDownload(httpClient)
    private val resolver: ResolveMetadataStep = XhResolver(httpClient)

    override suspend fun downloadMetadata(l: MediaLocation): MoviepageMetadata {
        return resolver.retrieveMetadata(l)
    }

    override suspend fun downloadContent(metadata: MoviepageMetadata, targetPath: String, progressHandler: ((readBytes: Long, time: Long, totalBytes: Long) -> Unit)?): FileDownloadResult {

        val downloadResult = downloader.downloadContent(metadata,
                targetPath,
                progressHandler
        )
        metadata.writeToFile(targetPath + File.separator + (metadata.contentMetadata.title).replace("/","-") + ".meta.json")
        return downloadResult
    }
}