package io.github.lumue.kodiservice.jsonrpc;

import javax.print.attribute.IntegerSyntax;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KodiApiRequest extends KodiApiMessage{
	
	private final String method;
	
	private final Map<String,Object> params=new HashMap<>();
	
	public KodiApiRequest(String method,Map<String,Object> params) {
		super(method+"#"+UUID.randomUUID().toString());
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
		params.put("properties",new String[]{
				"title","runtime","thumbnail","tagline","userrating","tag","dateadded","lastplayed","genre","streamdetails"
		});
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
	
}
