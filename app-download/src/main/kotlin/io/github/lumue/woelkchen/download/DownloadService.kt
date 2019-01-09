package io.github.lumue.woelkchen.download

import io.github.lumue.woelkchen.download.sites.ph.PhSite
import io.github.lumue.woelkchen.download.sites.xh.XhSite
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class DownloadService {


    private val logger = LoggerFactory.getLogger(DownloadService::class.java)

    private val clients: Map<String, SiteClient> = mapOf("xhamster" to XhSite(), "pornhub" to PhSite())

    val executions: MutableMap<MediaLocation, DownloadExecution> = mutableMapOf()

    val results: MutableMap<MediaLocation, FileDownloadResult> = mutableMapOf()

    @Value("\${woelkchen.download.path.download}")
    val downloadPath: String = "/mnt/nasbox/media/adult/incoming"


    fun progressHandler(execution: DownloadExecution) = fun(p: Long, time: Long, t: Long) {
        var seconds = TimeUnit.MILLISECONDS.toSeconds(time)
        if (seconds < 1) seconds = 1
        execution.updateProgress(p, t, time)
        logger.debug(" $p of $t in ${seconds}s. ${p / seconds} b/s of ${execution.url} downloaded")
    }


    fun download(url: MediaLocation): DownloadExecution {
        val client = clients[url.extractSiteKey()]!!
        return scheduleDownload(url, client)
    }



    private fun scheduleDownload(url: MediaLocation, client: SiteClient): DownloadExecution {

        val downloadExecution = DownloadExecution(url)
        executions.put(url, downloadExecution)

        GlobalScope.async {
            try {
                val metadata = this.async { client.downloadMetadata(url) }
                val fileDownloadResult = this.async {
                    client.downloadContent(
                            metadata.await(),
                            downloadPath,
                            progressHandler(downloadExecution)
                    )
                }
                results.put(url, fileDownloadResult.await())
            } finally {
                executions.remove(url)
            }
        }

        return downloadExecution
    }


}

private fun MediaLocation.extractSiteKey(): String {
    if (url.contains("pornhub"))
        return "pornhub"
    return "xhamster"
}
