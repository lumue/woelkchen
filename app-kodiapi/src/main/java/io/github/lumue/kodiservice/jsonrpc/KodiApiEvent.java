package io.github.lumue.kodiservice.jsonrpc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class KodiApiEvent extends KodiApiRequest{
	
	@JsonCreator
	public KodiApiEvent(
			@JsonProperty("method")
					String method,
			@JsonProperty("id")
					String id ,
			@JsonProperty("jsonrpc")
					String jsonrpc,
			@JsonProperty("params")
					Map<String,Object> params) {
		super(method,id,jsonrpc,params);
	}
	
	@JsonIgnore
	public String getName(){
		return getMethod();
	}
	
	@JsonIgnore
	public Map<String,Object> getData(){
		return (Map<String, Object>) getParams().get("data");
	}
	
	@JsonIgnore
	public String getSender(){
		return (String) getParams().get("sender");
	}
	
	
}
