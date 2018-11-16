package io.github.lumue.woelkchen.media

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories

@SpringBootApplication
@EnableNeo4jRepositories
@EntityScan("io.github.lumue.woelkchen.media.data")
class AppMediaApplication

fun main(args: Array<String>) {
    runApplication<AppMediaApplication>(*args)
}
