package io.github.lumue.kodiservice

import io.github.lumue.kodiservice.jsonrpc.KodiApiClient
import io.github.lumue.kodiservice.jsonrpc.KodiApiRequest
import io.github.lumue.kodiservice.jsonrpc.KodiApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class KodiPlayerService(
        @Autowired
        private val kodiApi: KodiApiClient
) {
    fun playNext(): Mono<KodiApiResponse>? {
        return kodiApi.execute(KodiApiRequest.playNext())
    }
}