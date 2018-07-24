package io.github.lumue.kodiservice.jsonrpc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class KodiApiMessage {
	
	protected final String id;
	protected final String jsonrpc;
	
	
	public KodiApiMessage(@JsonProperty("id") String id, @JsonProperty("jsonrpc") String jsonrpc) {
		this.id = id;
		this.jsonrpc = jsonrpc;
	}
	
	
	public KodiApiMessage(@JsonProperty("id") String id) {
		this.id = id;
		this.jsonrpc = "2.0";
	}
	
	public String getId() {
		return id;
	}
	
	public String getJsonrpc() {
		return jsonrpc;
	}
}
