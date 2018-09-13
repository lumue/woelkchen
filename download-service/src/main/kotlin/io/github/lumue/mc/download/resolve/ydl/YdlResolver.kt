package io.github.lumue.mc.download.resolve.ydl

import io.github.lumue.mc.download.resolve.LocationMetadata
import io.github.lumue.mc.download.resolve.LocationMetadataResolver
import io.github.lumue.mc.download.resolve.MediaLocation
import io.github.lumue.ydlwrapper.download.YdlDownloadTask
import io.github.lumue.ydlwrapper.metadata.single_info_json.YdlInfoJson
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import kotlinx.coroutines.experimental.withContext
import org.slf4j.LoggerFactory

class YdlResolver : LocationMetadataResolver {

    private val processContext = newFixedThreadPoolContext(5, "ydl-location-resolver")

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    override suspend fun resolveMetadata(l: MediaLocation): LocationMetadata = withContext(
            processContext,
            CoroutineStart.DEFAULT,
            resolveWithYoutubeDl(l))

    fun resolveWithYoutubeDl(l: MediaLocation): suspend () -> LocationMetadata {
        return {

            logger.debug("resolving metadata for location "+l)

            val url = l.url
            val builder = YdlDownloadTask.builder()
                    .setUrl(url)
                    .setPathToYdl("/usr/local/bin/youtube-dl")

            if (url.contains("youtube.com")) {
                builder.setForceMp4(true)
            }

            val downloadTask = builder.build()
            downloadTask.prepare()
            downloadTask.ydlDownloadTaskMetadata
                    .map { i: YdlInfoJson? -> i!!.toLocationMetadata() }
                    .orElseThrow<RuntimeException?> { RuntimeException("ooops") }

        }
    }


}
