package io.github.lumue.krp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin
public class ReactiveController {
	
	private final PublishSubscribeChannel currentMovieChangedChannel;
	
	@Autowired
	public ReactiveController(PublishSubscribeChannel currentMovieChangedChannel) {
		this.currentMovieChangedChannel = currentMovieChangedChannel;
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
	
	
}
