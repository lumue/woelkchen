
package io.github.lumue.woelkchen.download.app

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


@Suppress("RedundantModalityModifier")
@SpringBootApplication(scanBasePackages = ["io.github.lumue.woelkchen.download"])
class DownloadserviceApplication



    fun main(args: Array<String>) {
        SpringApplication.run(DownloadserviceApplication::class.java,*args)
    }


