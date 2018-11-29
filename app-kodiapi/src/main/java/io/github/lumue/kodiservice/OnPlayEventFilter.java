package io.github.lumue.kodiservice;

import io.github.lumue.kodiservice.jsonrpc.KodiApiEvent;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class OnPlayEventFilter implements MessageSelector {
	@Override
	public boolean accept(Message<?> message) {
		try {
			KodiApiEvent event= (KodiApiEvent) message.getPayload();
			return "Player.OnPlay".equals(event.getName());
		} catch (Throwable t) {
			return false;
		}
	}
}
