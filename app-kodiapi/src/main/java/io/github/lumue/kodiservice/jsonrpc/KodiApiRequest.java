package io.github.lumue.kodiservice.jsonrpc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

import javax.print.attribute.IntegerSyntax;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KodiApiRequest extends KodiApiMessage{
	
	public static final String[] MOVIE_PROPERTIES_ALL = {
			"title", "runtime", "thumbnail", "tagline", "userrating", "tag", "dateadded", "lastplayed", "genre", "streamdetails"
	};
	private final String method;
	
	private final Map<String,Object> params=new HashMap<>();
	
	public KodiApiRequest(String method,Map<String,Object> params) {
		super(method+"#"+UUID.randomUUID().toString());
		this.method = method;
		this.params.putAll(params);
	}
	
	@JsonCreator
	public KodiApiRequest(
			@JsonProperty("method")
			String method,
			@JsonProperty("id")
			String id ,
			@JsonProperty("jsonrpc")
			String jsonrpc,
			@JsonProperty("params")
			Map<String,Object> params) {
		super(id,jsonrpc);
		this.method = method;
		this.params.putAll(params);
	}
	
	public Map<String, Object> getParams() {
		return params;
	}
	
	public String getMethod() {
		return method;
	}
	
	public static KodiApiRequest newGetMovieRequest(Integer movieId){
		Map<String,Object> params=new HashMap<>();
		params.put("movieid",movieId);
		params.put("properties", MOVIE_PROPERTIES_ALL);
		return new KodiApiRequest("VideoLibrary.GetMovieDetails",params);
	}
	
	public static KodiApiRequest newGetPlayingItemRequest(){
		Map<String,Object> params=new HashMap<>();
		params.put("playerid", 1L);
		return new KodiApiRequest("Player.GetItem",params);
	}
	
	public static KodiApiRequest newSetUserratingRequest(Integer movieid, int userrating){
		Map<String,Object> params=new HashMap<>();
		params.put("movieid", movieid);
		params.put("userrating",userrating);
		return new KodiApiRequest("VideoLibrary.SetMovieDetails",params);
	}
	
	
	public static KodiApiRequest newGetTagsRequest(){
		Map<String,Object> params=new HashMap<>();
		params.put("type", "movie");
		return new KodiApiRequest("VideoLibrary.GetTags",params);
	}
	
	public static KodiApiRequest newGetMoviesRequest(){
		Map<String,Object> params=new HashMap<>();
		params.put("properties", MOVIE_PROPERTIES_ALL);
		return new KodiApiRequest("VideoLibrary.GetMovies",params);
	}
	
	@Nullable
	public static KodiApiRequest playNext() {
		Map<String,Object> params=new HashMap<>();
		params.put("playerid", 1L);
		params.put("to", "next");
		return new KodiApiRequest("Player.GoTo",params);
	}
}
