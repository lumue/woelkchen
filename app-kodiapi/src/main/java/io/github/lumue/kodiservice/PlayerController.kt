package io.github.lumue.kodiservice

import io.github.lumue.kodiservice.jsonrpc.KodiApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@CrossOrigin
class PlayerController(
        @Autowired
        private val playerService: KodiPlayerService
) {

    @GetMapping("/player/next")
    fun  playNext() : Mono<String>? {
        return playerService.playNext()?.map { t: KodiApiResponse? -> t.toString() }
    }
}