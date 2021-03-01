/*
 * Derechos Reservados 2016 SAT.
 * Servicio de Administracion Tributaria (SAT).
 *
 * Este software contiene informacion propiedad exclusiva del SAT considerada
 * Confidencial. Queda totalmente prohibido su uso o divulgacion en forma
 * parcial o total.
 *
 */
package com.ruta.archivo.job.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;


/**
 * Class RutaArchivoConfig.
 *
 * @since 5 feb. 2021
 */
@Configuration
public class RutaArchivoConfig {

	// Access key id will be read from the application.properties file during the
	/** La variable que contiene informacion con respecto a: access key id. */
	// application intialization.
	@Value("${AWSKeyId}")
	private String accessKeyId;
	// Secret access key will be read from the application.properties file during
	/** La variable que contiene informacion con respecto a: secret access key. */
	// the application intialization.
	@Value("${AWSKey}")
	private String secretAccessKey;
	// Region will be read from the application.properties file during the
	/** La variable que contiene informacion con respecto a: region. */
	// application intialization.
	@Value("${aws.s3.region}")
	private String region;

	/**
	 * Obtener el objeto: amazon S 3 cient.
	 *
	 * @return El objeto: amazon S 3 cient
	 */
	@Bean
	public AmazonS3 getAmazonS3Cient() {
		final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
		// Get AmazonS3 client and return the s3Client object.
		return AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(region))
				.withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials)).build();
	}

}
