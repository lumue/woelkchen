package io.github.lumue.woelkchen.download.sites.ydl

import io.github.lumue.woelkchen.download.*
import io.github.lumue.woelkchen.shared.metadata.MoviepageMetadata
import io.github.lumue.ydlwrapper.download.YdlDownloadTask
import io.github.lumue.ydlwrapper.metadata.single_info_json.YdlInfoJson
import org.slf4j.LoggerFactory
import java.io.File


class YdlSite : SiteClient {


    private val httpClient: BasicHttpClient = BasicHttpClient()

    private val downloader: DownloadFileStep = BasicHttpDownload(httpClient)
    private val resolver: ResolveMetadataStep = YdlResolver()

    override suspend fun downloadMetadata(l: MediaLocation): MoviepageMetadata {
        return resolver.retrieveMetadata(l)
    }

    override suspend fun downloadContent(metadata: MoviepageMetadata,
                                         targetPath: String,
                                         progressHandler: ((readBytes: Long, time: Long, totalBytes: Long) -> Unit)?)
            : FileDownloadResult {

        val downloadResult = downloader.downloadContent(metadata,
                targetPath,
                progressHandler
        )

        metadata.writeToFile(targetPath + File.separator + (metadata.contentMetadata.title).replace("/","-") + ".meta.json")
        return downloadResult
    }



}


class YdlResolver : ResolveMetadataStep {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)


    override suspend fun retrieveMetadata(l: MediaLocation): MoviepageMetadata {


        logger.debug("resolving metadata for location " + l)

        val url = l.url
        val builder = YdlDownloadTask.builder()
                .setUrl(url)
                .setPathToYdl("/usr/local/bin/youtube-dl")

        if (url.contains("youtube.com")) {
            builder.setForceMp4(true)
        }

        val downloadTask = builder.build()
        downloadTask.prepare()
        return downloadTask.ydlDownloadTaskMetadata
                .map { i: YdlInfoJson? -> i!!.toLocationMetadata() }
                .orElseThrow<RuntimeException?> { RuntimeException("ooops") }

    }


}