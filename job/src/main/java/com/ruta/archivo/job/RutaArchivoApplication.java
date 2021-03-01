/*
 * Derechos Reservados 2016 SAT.
 * Servicio de Administracion Tributaria (SAT).
 *
 * Este software contiene informacion propiedad exclusiva del SAT considerada
 * Confidencial. Queda totalmente prohibido su uso o divulgacion en forma
 * parcial o total.
 *
 */
package com.ruta.archivo.job;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * Class RutaArchivoApplication.
 *
 * @since 5 feb. 2021
 */
@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
public class RutaArchivoApplication {

	/**
	 * Metodo principal.
	 *
	 * @param args los argumentos
	 */
	public static void main(String[] args) {
		SpringApplication.run(RutaArchivoApplication.class, args);
	}

	/**
	 * Post rest template.
	 *
	 * @return Objeto rest template
	 */
	@Bean
	public RestTemplate postRestTemplate() {
		return new RestTemplate();
	}

}
