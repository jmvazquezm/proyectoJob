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

/**
 * Interface IDescargaArchivo.
 *
 * @author FSW-SYE
 * @since 5 feb. 2021
 */
public interface IDescargaArchivo {
	
	/**
	 * Descarga archivo repositorio.
	 *
	 * @return true, si exitoso
	 */
	public boolean descargaArchivoRepositorio();

}
