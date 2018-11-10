package io.github.lumue.woelkchen.download

class DownloadJob(
        val resolveMetadataStep: io.github.lumue.woelkchen.download.ResolveMetadataStep,
        val downloadFileStep: io.github.lumue.woelkchen.download.DownloadFileStep,
        val writeMetadataToFile: Boolean=true,
        val workDir:String=".") {

    var downloadResult: io.github.lumue.woelkchen.download.FileDownloadResult?= null



}