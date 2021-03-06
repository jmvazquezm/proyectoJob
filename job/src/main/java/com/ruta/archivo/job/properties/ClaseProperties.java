/*
 * Derechos Reservados 2016 SAT.
 * Servicio de Administracion Tributaria (SAT).
 *
 * Este software contiene informacion propiedad exclusiva del SAT considerada
 * Confidencial. Queda totalmente prohibido su uso o divulgacion en forma
 * parcial o total.
 *
 */
package com.ruta.archivo.job.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Class ClaseProperties.
 *
 * @author FSW-SYE
 * @since 5 feb. 2021
 */
@Service
public class ClaseProperties implements IClaseProperties {

	/** La constante LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(ClaseProperties.class);

	/** La variable que contiene informacion con respecto a: path archivo prop. */
	private String pathArchivoProp = "";

	/**
	 * Leer propiedades.
	 *
	 * @param rutaPropiedades El objeto: ruta propiedades
	 * @return Objeto properties
	 * @throws IOException Una excepcion de I/O ha ocurrido.
	 */
	@Override
	public Properties leerPropiedades(String rutaPropiedades) throws IOException {
		Properties prop = null;

		// Asignar el valor desde propiedades del Application
		LOG.info("rutaPropiedades ");

		File fProp = new File(rutaPropiedades);
		try (FileInputStream fis = new FileInputStream(fProp.getAbsolutePath());) {
			LOG.info("Va a leer el archivo de propiedades...");

			prop = new Properties();
			prop.load(fis);

			LOG.info("Leyo el archivo de propiedades.");
		} catch (FileNotFoundException fnfEx) {
			LOG.error("No encontro el archivo de propiedades: " + fnfEx.getMessage(), fnfEx);
		} catch (IOException iox) {
			LOG.error("Error de E/S: " + iox.getMessage(), iox);
		}

		return prop;
	}

	/**
	 * Obtener el objeto: path archivo prop.
	 *
	 * @return El objeto: path archivo prop
	 */
	@Override
	public String getPathArchivoProp() {
		return pathArchivoProp;
	}

}
