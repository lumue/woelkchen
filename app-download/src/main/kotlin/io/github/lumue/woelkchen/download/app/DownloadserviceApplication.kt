
package io.github.lumue.woelkchen.download.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@Suppress("RedundantModalityModifier")
@SpringBootApplication(scanBasePackages = ["io.github.lumue.woelkchen.download"])
open class DownloadserviceApplication {

    fun main(args: Array<String>) {
        runApplication<DownloadserviceApplication>(*args)
    }
}

