package io.github.lumue.krp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
public class ReactiveController {
	
	private final PublishSubscribeChannel currentMovieChangedChannel;
	
	private final KodiMovieService kodiMovieService;
	
	@Autowired
	public ReactiveController(PublishSubscribeChannel currentMovieChangedChannel, KodiMovieService kodiMovieService) {
		this.currentMovieChangedChannel = currentMovieChangedChannel;
		this.kodiMovieService = kodiMovieService;
	}
	
	@GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Movie> eventMessages() {
		return Flux.create(sink -> {
			MessageHandler handler = message -> {
				sink.next((Movie) message.getPayload());
			};
			sink.onCancel(() -> currentMovieChangedChannel.unsubscribe(handler));
			currentMovieChangedChannel.subscribe(handler);
		});
	}
	
	@PutMapping(value = "/movies/{movieId}/rating",consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Movie> setMovieRating(@PathVariable Long movieId,@RequestBody Long rating ){
		return kodiMovieService.setRating(movieId,rating);
	}
	
	
}
