package io.github.lumue.kodiservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.lumue.kodiservice.jsonrpc.KodiApiEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.transformer.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StringToEventTransformer implements Transformer {
	
	private final ObjectMapper objectMapper;
	
	private final static Logger LOGGER= LoggerFactory.getLogger(StringToEventTransformer.class);
	
	public StringToEventTransformer(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	@Override
	public Message<?> transform(Message<?> message) {
		try{
		String jsonString= (String) message.getPayload();
		return MessageBuilder
				.withPayload(parseString(jsonString))
				.copyHeadersIfAbsent(message.getHeaders())
				.build();
		} catch (Throwable t) {
			
			return null;
		}
	}
	
	private KodiApiEvent parseString(String jsonString) {
		try {
			return objectMapper.readValue(jsonString, KodiApiEvent.class);
		} catch (IOException e) {
			throw new RuntimeException("parsing "+jsonString+" to json failed");
		}
	}
}
