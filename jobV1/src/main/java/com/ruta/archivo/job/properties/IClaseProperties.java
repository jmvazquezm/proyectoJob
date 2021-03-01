package com.ruta.archivo.job.properties;

import java.io.IOException;
import java.util.Properties;

public interface IClaseProperties {
	
	Properties leerPropiedades(String rutaPropiedades) throws IOException;

	String getPathArchivoProp();

}
