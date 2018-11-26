package io.github.lumue.woelkchen.download.sites.ydl

import io.github.lumue.woelkchen.download.LocationMetadata
import io.github.lumue.woelkchen.download.ResolveMetadataStep
import io.github.lumue.woelkchen.download.MediaLocation
import io.github.lumue.ydlwrapper.download.YdlDownloadTask
import io.github.lumue.ydlwrapper.metadata.single_info_json.YdlInfoJson
import kotlinx.coroutines.newFixedThreadPoolContext
import org.slf4j.LoggerFactory

class YdlResolver : ResolveMetadataStep {
    private val processContext = newFixedThreadPoolContext(5, "ydl-location-resolver")

    private val logger = LoggerFactory.getLogger(this.javaClass.name)


    override suspend fun retrieveMetadata(l: MediaLocation): LocationMetadata {


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


