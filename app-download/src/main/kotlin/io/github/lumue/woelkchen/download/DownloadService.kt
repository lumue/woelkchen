package io.github.lumue.woelkchen.download

import io.github.lumue.woelkchen.download.sites.xh.XhHttpClient
import io.github.lumue.woelkchen.download.sites.xh.XhResolver
import org.slf4j.LoggerFactory
import org.springframework.kotlin.experimental.coroutine.annotation.Coroutine
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

@Service
open class DownloadService {


    private val logger = LoggerFactory.getLogger(DownloadService::class.java)

    val httpClient: XhHttpClient = XhHttpClient("dirtytom74", "ddl85s")

    val downloader: io.github.lumue.woelkchen.download.DownloadFileStep = BasicHttpDownload(httpClient)
    val resolver: ResolveMetadataStep = XhResolver(httpClient)

    fun progressHandler(name: String) = fun(p: Long, time: Long, t: Long) {
        var seconds = TimeUnit.MILLISECONDS.toSeconds(time)
        if (seconds < 1) seconds = 1
        logger.debug(" $p of $t in ${seconds}s. ${p / seconds} b/s of $name downloaded")
    }

    @Coroutine("downloadDispatcher")
    open suspend fun download(url: MediaLocation) {
        val metadata = resolver.resolveMetadata(url)
        val downloadResult = downloader(metadata,
                "/mnt/nasbox/media/adult/incoming",
                progressHandler(metadata.contentMetadata.title)
        )
        val out = FileOutputStream(downloadResult.filename + ".meta.json")
        LocationMetadataWriter().write(metadata, out)
        out.close()
    }

}