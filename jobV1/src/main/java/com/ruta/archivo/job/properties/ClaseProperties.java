package com.ruta.archivo.job.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class ClaseProperties implements IClaseProperties {
	
	private static final Logger LOG = LoggerFactory.getLogger(ClaseProperties.class);
	
	
	private String pathArchivoProp = "";
	
	@Override
	public Properties leerPropiedades(String rutaPropiedades) throws IOException {
		Properties prop = null;

		// Asignar el valor desde propiedades del Application
		LOG.info("rutaPropiedades " );

		

		File fProp = new File(rutaPropiedades);
		try (FileInputStream fis = new FileInputStream(fProp.getAbsolutePath());) {
			LOG.info("Va a leer el archivo de propiedades...");

			prop = new Properties();
			prop.load(fis);

			

			LOG.info("Leyo el archivo de propiedades.");
		} catch (FileNotFoundException fnfEx) {LOG.error("No encontro el archivo de propiedades: " + fnfEx.getMessage(), fnfEx);} catch (IOException iox) {LOG.error("Error de E/S: " + iox.getMessage(), iox);}

		return prop;
	}
	

	@Override
	public String getPathArchivoProp() {
		return pathArchivoProp;
	}

	
}
