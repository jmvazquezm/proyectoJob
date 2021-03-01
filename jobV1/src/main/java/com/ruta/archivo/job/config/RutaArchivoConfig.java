package com.ruta.archivo.job.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.ruta.archivo.job.listener.RutaArchivoJob;
import com.ruta.archivo.job.step.Processor;
import com.ruta.archivo.job.step.Reader;
import com.ruta.archivo.job.step.Writer;



@Configuration
public class RutaArchivoConfig {
	
	

	


	
	
	
	
	// Access key id will be read from the application.properties file during the application intialization.
		@Value("${AWSKeyId}")
		private String accessKeyId;
		// Secret access key will be read from the application.properties file during the application intialization.
		@Value("${AWSKey}")
		private String secretAccessKey;
		// Region will be read from the application.properties file  during the application intialization.
		@Value("${aws.s3.region}")
		private String region;

		@Bean
		public AmazonS3 getAmazonS3Cient() {
			final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
			// Get AmazonS3 client and return the s3Client object.
			return AmazonS3ClientBuilder
					.standard()
					.withRegion(Regions.fromName(region))
					.withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
					.build();
		}
		
		
	
	
	
	@Bean
	public Job processJob(JobBuilderFactory jobBuilderFactory) {
		return jobBuilderFactory.get("processJob")
				.incrementer(new RunIdIncrementer()).listener(listener())
				.flow(orderStep1(null)).end().build();
	}
	
	@Bean
	public Step orderStep1(StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("orderStep1").<String, String> chunk(1)
				.reader(new Reader()).processor(new Processor())
				.writer(new Writer()).build();
	}

	@Bean
	public JobExecutionListener listener() {
		return new RutaArchivoJob();
	}


}
