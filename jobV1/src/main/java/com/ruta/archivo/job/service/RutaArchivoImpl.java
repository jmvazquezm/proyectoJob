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

@Service
public class RutaArchivoImpl implements IRutaArchivo {
	
	private static final Logger LOG = LoggerFactory.getLogger(RutaArchivoImpl.class);

	

	@Autowired
	PeticionServicio peticionServico;
	
	@Autowired
	AmazonS3 amazonS3;

	
	@Value("${ruta.local}")
	private String rLocal;
	
	@Value("${ruta.local2}")
	private String rLocal2;	

		
	@Value("${cabecero}")
	private String cabecero;
	
	@Value("${trustServiceProviderListCierre}")
	private String trustServiceProviderListCierre;
	
		
		
	//******Agregar Header a xml*****
	 
	
	@Override
	public void agregarElemento() {
		
		LOG.info("INICIA PROCESO PARA AGREGAR HEADERS ");
		File fileAgregar = new File(rLocal+armarArchivo());		 
		try {
			byte[] bytes = Files.readAllBytes(fileAgregar.toPath());
			String st = new String(bytes);
			String archivo = cabecero  + st  + "\n" + trustServiceProviderListCierre;						
			Path pathXMLFILE = Paths.get(rLocal2+armarArchivo());
			Files.write(pathXMLFILE, archivo.getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);			
		} catch (IOException e) {
			e.getMessage();			
		}
		LOG.info("FIN PROCESO PARA AGREGAR HEADERS ");	
	}
	
	
	
	public String armarArchivo() {
		
		 Date ahora = new Date();
		 String nombreArchivo;
		    SimpleDateFormat formateador = new SimpleDateFormat("ddMMyyyy");
		    
		    LOG.info("Prueba fecha" + formateador.format(ahora) );
		    
		    nombreArchivo= "TSP_SE_" + formateador.format(ahora) + ".xml";
		    
		    LOG.info("NOMBRE ARCHIVO " + nombreArchivo );
		    
		    return nombreArchivo;
		
	}

	@Override
	public void consumirServicioPost() {

		String response = peticionServico.postProductList();		
		
		LOG.info("response " + response);
	}


}
