package io.github.lumue.kodiservice.jsonrpc;

import com.fasterxml.jackson.annotation.JsonCreator;

public class KodiApiEvent extends KodiApiMessage{
	@JsonCreator
	public KodiApiEvent(String id, String jsonrpc) {
		super(id, jsonrpc);
	}
}
