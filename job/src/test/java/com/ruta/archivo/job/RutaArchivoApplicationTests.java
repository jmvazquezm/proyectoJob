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

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ruta.archivo.job.service.DescargaArchivoTestImpl;

/**
 * Class RutaArchivoApplicationTests.
 *
 * @author FSW-SYE
 * @since 5 feb. 2021
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class RutaArchivoApplicationTests {

	/** La constante LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(RutaArchivoApplicationTests.class);

	/**
	 * Context loads.
	 */
	@Test
	void contextLoads() {
		
		DescargaArchivoTestImpl test = new DescargaArchivoTestImpl();
		LOG.info("Inicia TEST");
		test.descargaArchivo();
		LOG.info("Fin TEST");
	}

	

}
