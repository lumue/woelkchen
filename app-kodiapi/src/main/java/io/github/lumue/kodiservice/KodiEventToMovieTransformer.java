package io.github.lumue.kodiservice;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.lumue.kodiservice.jsonrpc.KodiApiEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.transformer.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KodiEventToMovieTransformer implements Transformer {
	private static final Logger LOGGER = LoggerFactory.getLogger(KodiEventToMovieTransformer.class);
	private final KodiMovieService kodiMovieService;
	
	@Autowired
	public KodiEventToMovieTransformer(KodiMovieService kodiMovieService) {
		this.kodiMovieService = kodiMovieService;
	}
	
	@Override
	public Message<?> transform(Message<?> message) {
		try {
			KodiApiEvent event= (KodiApiEvent) message.getPayload();
			final Map<String,Object> item = (Map<String, Object>) event.getData()
					.get("item");
			final long movieId = new Long((Integer) item.get("id"));
			
			final Movie movie = kodiMovieService.findById(movieId).block();
			
			assert movie != null;
			return MessageBuilder
					.withPayload(movie)
					.copyHeadersIfAbsent(message.getHeaders())
					.build();
		}
		catch (Throwable t){
			LOGGER.error("error getting movie",t);
			return null;
		}
	}
}
