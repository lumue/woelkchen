package io.github.lumue.kodiservice;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class OnPlayEventFilter implements MessageSelector {
	@Override
	public boolean accept(Message<?> message) {
		try {
			JsonNode json = (JsonNode) message.getPayload();
			return "Player.OnPlay".equals(json.get("method").asText());
		} catch (Throwable t) {
			return false;
		}
	}
}
