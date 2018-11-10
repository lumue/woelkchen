
package io.github.lumue.woelkchen.download

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@Suppress("RedundantModalityModifier")
@SpringBootApplication
open class DownloadserviceApplication

    fun main(args: Array<String>) {
        runApplication<io.github.lumue.woelkchen.download.DownloadserviceApplication>(*args)
    }


