package io.github.lumue.kodiservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.lumue.kodiservice.jsonrpc.KodiApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.net.MalformedURLException;


@Configuration
@EnableWebFlux
@EnableIntegration
@ImportResource(locations = {
		"classpath:io/github/lumue/kodiservice/kodi-handler.xml",
		"classpath:io/github/lumue/kodiservice/kodi-inbound.xml"
}
)
public class ApplicationConfiguration {
	
	@Bean
	public String kodiHttpUrl(@Value("${krp.kodi.url.http}") String kodiHttpUrl){
		return kodiHttpUrl;
	}
	
	
	@Bean("kodiApiClient")
	public KodiApiClient kodiWebClient(
			@Value("${krp.kodi.url.http}") String kodiHttpUrl,
			@Value("${krp.kodi.username}") String username,
			@Value("${krp.kodi.password}") String password,
			ObjectMapper objectMapper) throws MalformedURLException {
		return new KodiApiClient(kodiHttpUrl,username,password, objectMapper);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder,
	                                 @Value("${krp.kodi.username}") String username,
	                                 @Value("${krp.kodi.password}") String password){
		return restTemplateBuilder
				.basicAuthorization(username,password)
				.build();
	}
	
	
	
	@Bean
	RouterFunction<ServerResponse> staticResourceRouter(){
		return RouterFunctions.resources("/**", new ClassPathResource("static/"));
	}
	
	
	
	
}
