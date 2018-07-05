package io.github.lumue.krp;

import org.json.JSONObject;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class OnPlayEventFilter implements MessageSelector {
	@Override
	public boolean accept(Message<?> message) {
		JSONObject json= (JSONObject) message.getPayload();
		return "Player.OnPlay".equals(json.get("method"));
	}
}
