package io.github.lumue.woelkchen.download

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/downloads")
open class DownloadController(
        @Autowired
        val downloadService: DownloadService
) {

    @PostMapping
    suspend open fun addDownload(@RequestBody url: List<String>) {
        url.forEach {
        downloadService.download(MediaLocation(it))
        }
    }
}