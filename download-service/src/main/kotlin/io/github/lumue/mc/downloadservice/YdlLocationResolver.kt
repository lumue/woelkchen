package io.github.lumue.mc.downloadservice

import io.github.lumue.ydlwrapper.download.YdlDownloadTask
import io.github.lumue.ydlwrapper.metadata.single_info_json.YdlInfoJson
import toLocationMetadata

class YdlLocationResolver : LocationMetadataResolver {

    override suspend fun resolveMetadata(l: MediaLocation): LocationMetadata {
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
               .map { i: YdlInfoJson? ->i!!.toLocationMetadata()  }
                .orElseThrow<RuntimeException?> { RuntimeException("ooops") }

    }





}
