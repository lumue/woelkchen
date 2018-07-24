package io.github.lumue.kodiservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import io.github.lumue.kodiservice.jsonrpc.KodiApiClient;
import io.github.lumue.kodiservice.jsonrpc.KodiApiRequest;
import io.github.lumue.kodiservice.jsonrpc.KodiApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Service
public class KodiMovieService {
	
	private final ObjectMapper jacksonObjectMapper;
	
	private final MessageChannel currentMovieChangedChannel;

	private final KodiApiClient kodiWebClient;
	
	
	@Autowired
	public KodiMovieService(KodiApiClient kodiWebClient,
	                        ObjectMapper movieMapper,
	                        MessageChannel currentMovieChangedChannel) {
		this.kodiWebClient = kodiWebClient;
		this.jacksonObjectMapper = movieMapper;
		this.currentMovieChangedChannel = currentMovieChangedChannel;
	}
	
	public Mono<Movie> findById(Long movieId) {
		return kodiWebClient.execute(KodiApiRequest.newGetMovieRequest(movieId.intValue()))
				.map(r->
						r.getResult().map(result->result.get("moviedetails")).orElseThrow(()->new RuntimeException("no moviedetails in result"))
				)
				.map(this::jsonNodeToMovie);
	}
	
	public Mono<Movie> getCurrentlyPlayingMovie() {
		return kodiWebClient.execute(KodiApiRequest.newGetPlayingItemRequest())
				.map(r->r.getResult().orElseThrow(()->new RuntimeException("no result in response")))
				.map(r->r.get("item").get("id").asLong())
				.flatMap(this::findById);
	}
	
	private Movie jsonNodeToMovie(JsonNode mdJsonObject) {
		try {
			return jacksonObjectMapper.readValue(new TreeTraversingParser(mdJsonObject), Movie.class);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public Mono<Void> setRating(Long movieId, Long rating) {
		final KodiApiRequest kodiApiRequest = KodiApiRequest.newSetUserratingRequest(movieId.intValue(), rating.intValue());
		return kodiWebClient.execute(kodiApiRequest)
				.then(findById(movieId))
				.flatMap(movie -> Mono.just(currentMovieChangedChannel.send(MessageBuilder.withPayload(movie).build())))
				.cast(Void.class);
		
	}
	
	public Flux<String> getAllTags() {
		return null;
	}
}
