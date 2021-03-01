/*
 * Derechos Reservados 2016 SAT.
 * Servicio de Administracion Tributaria (SAT).
 *
 * Este software contiene informacion propiedad exclusiva del SAT considerada
 * Confidencial. Queda totalmente prohibido su uso o divulgacion en forma
 * parcial o total.
 *
 */
package com.ruta.archivo.job.service;


import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Class DescargaArchivoTestImpl.
 *
 * @author FSW-SYE
 * @since 5 feb. 2021
 */
@Service
public class DescargaArchivoTestImpl implements IDescargaArchivoTest {

	/** La constante LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(DescargaArchivoTestImpl.class);

	

	/**
	 * Descarga archivo repositorio.
	 *
	 * @return true, si exitoso
	 */
	@Override
	public void descargaArchivo() {
		
		LOG.info("Se esta consumiendo SAT CLOUD ");
		LOG.info("Se descago: "+armarArchivo());

	}



	/**
	 * Armar archivo.
	 *
	 * @return Objeto string
	 */
	public String armarArchivo() {

		Date ahora = new Date();
		String nombreArchivo;
		SimpleDateFormat formateador = new SimpleDateFormat("ddMMyyyy");
		nombreArchivo = "TSP_SE_" + formateador.format(ahora) + ".xml";
		return nombreArchivo;

	}

}