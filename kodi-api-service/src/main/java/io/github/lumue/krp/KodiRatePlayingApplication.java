package io.github.lumue.krp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
public class KodiRatePlayingApplication {

	public static void main(String[] args) {
		SpringApplication.run(KodiRatePlayingApplication.class, args);
	}
}
