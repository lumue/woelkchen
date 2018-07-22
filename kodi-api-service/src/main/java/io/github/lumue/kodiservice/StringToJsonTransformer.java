package io.github.lumue.kodiservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.transformer.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StringToJsonTransformer implements Transformer {
	
	private final ObjectMapper objectMapper;
	
	private final static Logger LOGGER= LoggerFactory.getLogger(StringToJsonTransformer.class);
	
	public StringToJsonTransformer(ObjectMapper objectMapper) {
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
	
	private JsonNode parseString(String jsonString) {
		try {
			return objectMapper.readTree(jsonString);
		} catch (IOException e) {
			throw new RuntimeException("parsing "+jsonString+" to json failed");
		}
	}
}
