package io.github.lumue.krp;

import org.json.JSONObject;
import org.springframework.integration.transformer.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class StringToJsonTransformer implements Transformer {
	@Override
	public Message<?> transform(Message<?> message) {
		String jsonString= (String) message.getPayload();
		return MessageBuilder
				.withPayload(new JSONObject(jsonString))
				.copyHeadersIfAbsent(message.getHeaders())
				.build();
	}
}
