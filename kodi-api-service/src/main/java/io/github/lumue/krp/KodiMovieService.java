package io.github.lumue.krp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Optional;

@Service
public class KodiMovieService {
	
	private final RestTemplate restTemplate;
	
	
	private final ObjectMapper jacksonObjectMapper;
	
	private final String kodiHttpUrl;
	
	private final static String GET_MOVIE_BODY="{\"jsonrpc\": \"2.0\", \"method\": \"VideoLibrary.GetMovieDetails\", \"params\": {  \"movieid\": %s ,\"properties\": [\"title\",\"runtime\",\"thumbnail\",\"tagline\",\"userrating\",\"tag\",\"dateadded\",\"lastplayed\",\"genre\",\"streamdetails\"]}, \"id\": \"MovieGetDetails\"}";
	
	private final WebClient kodiWebClient;
	
	
	@Autowired
	public KodiMovieService(WebClient kodiWebClient,
	                        RestTemplate restTemplate,
	                        ObjectMapper movieMapper,
	                        String kodiHttpUrl) {
		this.restTemplate = restTemplate;
		this.kodiWebClient = kodiWebClient;
		this.jacksonObjectMapper = movieMapper;
		this.kodiHttpUrl = kodiHttpUrl;
	}
	
	public Movie findById(long movieId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(String.format(GET_MOVIE_BODY, movieId) ,headers);
		
		final KodiApiResponse kodiApiResponse = restTemplate.postForObject(kodiHttpUrl, entity, KodiApiResponse.class);
		final JsonNode result = kodiApiResponse.getResult()
				.orElseThrow(() -> new NullPointerException("kodiApiResponse did not contain result.  "+kodiApiResponse.toString()));
		return Optional.ofNullable(result.get("moviedetails"))
				.map(this::jsonNodeToMovie)
				.orElseThrow(() -> new NullPointerException("kodiApiResponse did not contain expected result. expected moviedetails, was: "+kodiApiResponse.toString()));
		
	}
	
	private Movie jsonNodeToMovie(JsonNode mdJsonObject) {
		try {
			return jacksonObjectMapper.readValue(new TreeTraversingParser(mdJsonObject), Movie.class);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
