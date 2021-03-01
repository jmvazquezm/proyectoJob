package com.ruta.archivo.job.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import com.ruta.archivo.job.service.RutaArchivoImpl;



@RestController
public class JobController {
	
	private static final Logger LOG = LoggerFactory.getLogger(JobController.class);
	
	@Autowired
	JobLauncher jobLauncher;
	
	@Autowired
	Job processJob;
	
	@Autowired
	RutaArchivoImpl rutaArchivoImpl;
	
	
	
	 //@RequestMapping("/rutaArchivo")
	 //@Scheduled(fixedRate = 5000)
	 @Scheduled(cron = "0 0/05 * * * MON-FRI")//Se ejecuta cada 5 min. L-V
	// @Scheduled(cron = "0 0 9-23 * 11 MON-FRI")//Se ejecuta cada hora en mes de Noviembre de L-V
	    public String handle()  {
		 
		 		LOG.info("INICIO DE LA TAREA NUEVAMENTE : ");
	            JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
	                    .toJobParameters();
	            try {
					jobLauncher.run(processJob, jobParameters);
				} catch (JobExecutionAlreadyRunningException|JobRestartException|JobInstanceAlreadyCompleteException|JobParametersInvalidException e) {
					e.getMessage();
					
				}
	            
	            
	 
	        return " ";
	    }
	 
	
}
