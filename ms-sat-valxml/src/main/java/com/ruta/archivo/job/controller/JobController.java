/*
 * Derechos Reservados 2016 SAT.
 * Servicio de Administracion Tributaria (SAT).
 *
 * Este software contiene informacion propiedad exclusiva del SAT considerada
 * Confidencial. Queda totalmente prohibido su uso o divulgacion en forma
 * parcial o total.
 *
 */
package com.ruta.archivo.job.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import com.ruta.archivo.job.constantes.Constantes;
import com.ruta.archivo.job.service.CargaS3Impl;
import com.ruta.archivo.job.service.DescargaArchivoImpl;
import com.ruta.archivo.job.service.EnvioCorreoImpl;
import com.ruta.archivo.job.service.RutaArchivoImpl;

/**
 * Class JobController.
 *
 * @since 5 feb. 2021
 */
@RestController
public class JobController {

	/** La constante LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(JobController.class);

	/** La variable que contiene informacion con respecto a: ruta archivo impl. */
	@Autowired
	RutaArchivoImpl rutaArchivoImpl;

	/** La variable que contiene informacion con respecto a: carga S 3 impl. */
	@Autowired
	CargaS3Impl cargaS3Impl;

	/** La variable que contiene informacion con respecto a: descarga archivo impl. */
	@Autowired
	DescargaArchivoImpl descargaArchivoImpl;

	/** La variable que contiene informacion con respecto a: envio correo impl. */
	@Autowired
	EnvioCorreoImpl envioCorreoImpl;


	/**
	 * Descarga xml.
	 */
	@Scheduled(cron = "0 0/05 * * * MON-FRI")
	public void descargaXml() {
		long startTime = System.currentTimeMillis();
	
		LOG.info("\n\n***** INICIO DE LA TAREA NUEVAMENTE : ");
		
		if (descargaArchivoImpl.descargaArchivoRepositorio()) {

			cargaS3Impl.subirArchivoS3();

			rutaArchivoImpl.consumirServicioPost();
		} else {
			LOG.info("No existe archivo a procesar");
			envioCorreoImpl.sendEmail();
		}

		long totalTime = System.currentTimeMillis() - startTime;
		LOG.info("{}***** EL JOB FINALIZÓ ---- Tiempo total de ejecucion: {} ms*****{}",
				Constantes.BREAK_LINE, totalTime, Constantes.BREAK_LINE);

	}

}
