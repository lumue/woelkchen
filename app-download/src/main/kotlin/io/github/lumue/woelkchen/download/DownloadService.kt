package io.github.lumue.woelkchen.download

import io.github.lumue.woelkchen.download.sites.ph.PhSite
import io.github.lumue.woelkchen.download.sites.xh.XhSite
import org.slf4j.LoggerFactory
import org.springframework.kotlin.experimental.coroutine.annotation.Coroutine
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
open class DownloadService {


    private val logger = LoggerFactory.getLogger(DownloadService::class.java)

    private val clients: Map<String,SiteClient> =mapOf("xhamster" to XhSite(),"pornhub" to PhSite())


    fun progressHandler(name: String) = fun(p: Long, time: Long, t: Long) {
        var seconds = TimeUnit.MILLISECONDS.toSeconds(time)
        if (seconds < 1) seconds = 1
        logger.debug(" $p of $t in ${seconds}s. ${p / seconds} b/s of $name downloaded")
    }

    @Coroutine("downloadDispatcher")
    open suspend fun download(url: MediaLocation) {
        val client=clients[url.extractSiteKey()]!!
        val metadata= client.retrieveMetadata(url)
        client.downloadContent(metadata,"/mnt/nasbox/media/adult/incoming",progressHandler(metadata.contentMetadata.title))
    }

}

private fun MediaLocation.extractSiteKey(): String {
    if(url.contains("pornhub"))
        return "pornhub"
    return "xhamster"
}
