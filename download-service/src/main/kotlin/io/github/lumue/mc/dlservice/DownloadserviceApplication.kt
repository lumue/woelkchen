package io.github.lumue.mc.dlservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kotlin.experimental.coroutine.EnableCoroutine

@SpringBootApplication
@EnableCoroutine
class DownloadserviceApplication {

    fun main(args: Array<String>) {
        runApplication<DownloadserviceApplication>(*args)
    }
}