package io.github.lumue.kodiservice.jsonrpc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

public class KodiApiResponse extends KodiApiMessage {
	
	private final JsonNode result;
	
	@JsonCreator
	public KodiApiResponse(
			@JsonProperty("id") String id,
			@JsonProperty("jsonrpc") String jsonrpc,
			@JsonProperty("result") JsonNode result) {
		super(id, jsonrpc);
		this.result = result;
	}
	
	public Optional<JsonNode> getResult() {
		return Optional.ofNullable(result);
	}
	
	@Override
	public String toString() {
		return "KodiApiResponse{" +
				"id='" + getId() + '\'' +
				", jsonrpc='" + getJsonrpc() + '\'' +
				", result=" + result +
				'}';
	}
}
