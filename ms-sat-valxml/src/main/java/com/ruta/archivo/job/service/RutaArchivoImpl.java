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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.ruta.archivo.job.peticion.servicio.PeticionServicio;

/**
 * Class RutaArchivoImpl.
 *
 * @author FSW-SYE
 * @since 5 feb. 2021
 */
@Service
public class RutaArchivoImpl implements IRutaArchivo {
	
	/** La constante LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(RutaArchivoImpl.class);

	

	/** La variable que contiene informacion con respecto a: peticion servico. */
	@Autowired
	PeticionServicio peticionServico;
	
	/** La variable que contiene informacion con respecto a: amazon S 3. */
	@Autowired
	AmazonS3 amazonS3;

	
	/** La variable que contiene informacion con respecto a: r local. */
	@Value("${ruta.local}")
	private String rLocal;
	
	/** La variable que contiene informacion con respecto a: r local 2. */
	@Value("${ruta.local2}")
	private String rLocal2;	

		
	/** La variable que contiene informacion con respecto a: cabecero. */
	@Value("${cabecero}")
	private String cabecero;
	
	/** La variable que contiene informacion con respecto a: trust service provider list cierre. */
	@Value("${trustServiceProviderListCierre}")
	private String trustServiceProviderListCierre;
	
		
		
	//******Agregar Header a xml*****
	 
	
	/**
	 * Agregar elemento.
	 */
	@Override
	public void agregarElemento() {
		
		LOG.info("********** Inicia proceso para agregar Headers **********");
		File fileAgregar = new File(rLocal+armarArchivo());		 
		try {
			byte[] bytes = Files.readAllBytes(fileAgregar.toPath());
			String st = new String(bytes);
			String archivo = cabecero  + st  + "\n" + trustServiceProviderListCierre;						
			Path pathXMLFILE = Paths.get(rLocal2+armarArchivo());
			Files.write(pathXMLFILE, archivo.getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);

			if (fileAgregar.delete())
				LOG.info("El fichero ha sido borrado satisfactoriamente");
			else
				LOG.error("El fichero no puede ser borrado");			
		} catch (IOException e) {
			e.getMessage();			
		}
		LOG.info("********** Fin proceso para agregar Headers ********** ");	
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
		    
		    LOG.info("Prueba fecha" + formateador.format(ahora) );
		    
		    nombreArchivo= "TSP_SE_" + formateador.format(ahora) + ".xml";
		    
		    LOG.info("NOMBRE ARCHIVO " + nombreArchivo );
		    
		    return nombreArchivo;
		
	}

	/**
	 * Consumir servicio post.
	 */
	@Override
	public void consumirServicioPost() {

		String response = peticionServico.postProductList();		
		LOG.info("response " + response);

	}


}
