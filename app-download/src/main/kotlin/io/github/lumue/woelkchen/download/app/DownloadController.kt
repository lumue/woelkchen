package io.github.lumue.woelkchen.download.app

import io.github.lumue.woelkchen.download.DownloadExecution
import io.github.lumue.woelkchen.download.DownloadService
import io.github.lumue.woelkchen.download.FileDownloadResult
import io.github.lumue.woelkchen.download.MediaLocation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/downloads")
class DownloadController(
        @Autowired
        val downloadService: DownloadService
) {

    @PostMapping
    fun addDownload(@RequestBody url: List<String>) :List<DownloadExecution> {
        return url.map {downloadService.download(MediaLocation(it))}
    }

    @GetMapping
    fun allDownloads(): Collection<DownloadExecution> {
        return downloadService.executions.values
    }

    @GetMapping("/results")
    fun allResults(): Collection<FileDownloadResult> {
        return downloadService.results.values
    }
}