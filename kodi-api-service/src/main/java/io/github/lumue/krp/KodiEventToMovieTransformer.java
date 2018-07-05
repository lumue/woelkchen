package io.github.lumue.krp;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.transformer.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class KodiEventToMovieTransformer implements Transformer {
	private final KodiMovieService kodiMovieService;
	
	@Autowired
	public KodiEventToMovieTransformer(KodiMovieService kodiMovieService) {
		this.kodiMovieService = kodiMovieService;
	}
	
	@Override
	public Message<?> transform(Message<?> message) {
		
		JSONObject jsonObject= (JSONObject) message.getPayload();
		final long movieId = jsonObject.getJSONObject("params")
				.getJSONObject("data")
				.getJSONObject("item")
				.getLong("id");
		
		final Movie movie = kodiMovieService.findById(movieId);
		
		return MessageBuilder
				.withPayload(movie)
				.copyHeadersIfAbsent(message.getHeaders())
				.build();
	}
}
