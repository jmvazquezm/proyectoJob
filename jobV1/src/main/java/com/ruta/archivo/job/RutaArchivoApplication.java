package com.ruta.archivo.job;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
public class RutaArchivoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RutaArchivoApplication.class, args);
	}
	
	@Bean
	public RestTemplate postRestTemplate() {
	      return new RestTemplate();
	   }

}
