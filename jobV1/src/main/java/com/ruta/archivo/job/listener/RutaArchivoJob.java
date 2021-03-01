package com.ruta.archivo.job.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;

import com.ruta.archivo.job.service.CargaS3Impl;
import com.ruta.archivo.job.service.DescargaArchivoImpl;
import com.ruta.archivo.job.service.EnvioCorreoImpl;
import com.ruta.archivo.job.service.RutaArchivoImpl;

public class RutaArchivoJob extends JobExecutionListenerSupport   {

	private static final Logger LOG = LoggerFactory.getLogger(RutaArchivoJob.class);
	
	@Autowired
	RutaArchivoImpl rutaArchivoImpl;
	
	@Autowired
	CargaS3Impl cargaS3Impl;
	
	@Autowired
	DescargaArchivoImpl descargaArchivoImpl;
	
	@Autowired
	EnvioCorreoImpl envioCorreoImpl;
		

	@Override	
	public void afterJob(JobExecution jobExecution) {

		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {			
			
			
			/*
			if(descargaArchivoImpl.descargaArchivoRepositorio()) {
				
				cargaS3Impl.subirArchivoS3();
				
				//rutaArchivoImpl.consumirServicioPost();
				
			}else {
				LOG.info("No existe archivo a procesar");
			}
			
			*/
			
			envioCorreoImpl.sendEmail();
			//rutaArchivoImpl.consumirServicioPost();
			
			
			
			

		} 			

		else if (jobExecution.getStatus() == BatchStatus.FAILED) {

			LOG.info("¡EL JOB FINALIZÓ CON ERROR!");
		}
	}
}

	
	

	


