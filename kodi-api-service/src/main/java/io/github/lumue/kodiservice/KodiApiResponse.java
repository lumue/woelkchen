package io.github.lumue.kodiservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

public class KodiApiResponse {
	
	private final String id;
	
	private final String jsonrpc;
	
	private final JsonNode result;
	
	@JsonCreator
	public KodiApiResponse(
			@JsonProperty("id") String id,
			@JsonProperty("jsonrpc") String jsonrpc,
			@JsonProperty("result") JsonNode result) {
		this.id = id;
		this.jsonrpc = jsonrpc;
		this.result = result;
	}
	
	public String getId() {
		return id;
	}
	
	public String getJsonrpc() {
		return jsonrpc;
	}
	
	public Optional<JsonNode> getResult() {
		return Optional.ofNullable(result);
	}
	
	@Override
	public String toString() {
		return "KodiApiResponse{" +
				"id='" + id + '\'' +
				", jsonrpc='" + jsonrpc + '\'' +
				", result=" + result +
				'}';
	}
}
