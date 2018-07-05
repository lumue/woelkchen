package io.github.lumue.krp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
@EnableWebFlux
@EnableIntegration
@ImportResource(locations = "classpath:io/github/lumue/krp/integration-context.xml")
public class ApplicationConfiguration {
	
	@Bean
	public String kodiHttpUrl(@Value("${krp.kodi.url.http}") String kodiHttpUrl){
		return kodiHttpUrl;
	}
	
	
	@Bean("kodiWebClient")
	public WebClient kodiWebClient(WebClient.Builder builder, String kodiHttpUrl){
		return builder
				.baseUrl(kodiHttpUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
		return restTemplateBuilder
				.build();
	}
	
	
	
	@Bean
	RouterFunction<ServerResponse> staticResourceRouter(){
		return RouterFunctions.resources("/**", new ClassPathResource("static/"));
	}
	
	
	
	
}
