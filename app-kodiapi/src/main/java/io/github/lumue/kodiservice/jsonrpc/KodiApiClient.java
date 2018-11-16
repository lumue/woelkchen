package io.github.lumue.kodiservice.jsonrpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.net.URL;

public class KodiApiClient {
	
	private final static Logger LOGGER= LoggerFactory.getLogger(KodiApiClient.class);
	
	private final URL url;
	
	private final String username;
	
	private final String password;
	
	private final WebClient webClient;
	
	private final ObjectMapper objectMapper;
	
	public KodiApiClient(String url, String username, String password, ObjectMapper objectMapper) throws MalformedURLException {
		
		this.url = new URL(url);
		this.username = username;
		this.password = password;
		this.objectMapper = objectMapper;
		
		
		webClient=WebClient.builder()
				.baseUrl(url)
				.defaultHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
				.defaultHeader("Accept",MediaType.APPLICATION_JSON_UTF8_VALUE)
				.filter(logRequest())
				.filter(ExchangeFilterFunctions.basicAuthentication(username,password))
				.build();
	}
	
	
	public Mono<KodiApiResponse> execute(KodiApiRequest request){
		final String requestBody = requestToJson(request);
		LOGGER.info("sending request body "+requestBody+ " to kodi api at "+this.url.toString());
		return this.webClient
				.post()
				.body(BodyInserters.fromObject(requestBody))
				.retrieve()
				.bodyToMono(KodiApiResponse.class);
				
	}
	
	private String requestToJson(KodiApiRequest request) {
		final String requestBody;
		try {
			requestBody = objectMapper.writeValueAsString(request);
			return requestBody;
		} catch (JsonProcessingException e) {
			throw new RuntimeException("error transforming request to json ",e);
		}
		
	}
	
	private static ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
			LOGGER.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
			clientRequest.headers().forEach((name, values) -> values.forEach(value -> LOGGER.debug("{}={}", name, value)));
			return Mono.just(clientRequest);
		});
	}
	
	
	
}
