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

import java.io.IOException;
import java.util.Properties;

/**
 * Interface IClaseProperties.
 *
 * @author FSW-SYE
 * @since 5 feb. 2021
 */
public interface IClaseProperties {
	
	/**
	 * Leer propiedades.
	 *
	 * @param rutaPropiedades El objeto: ruta propiedades
	 * @return Objeto properties
	 * @throws IOException Una excepcion de I/O ha ocurrido.
	 */
	Properties leerPropiedades(String rutaPropiedades) throws IOException;

	/**
	 * Obtener el objeto: path archivo prop.
	 *
	 * @return El objeto: path archivo prop
	 */
	String getPathArchivoProp();

}
