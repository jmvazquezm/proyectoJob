package com.ruta.archivo.job.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;


@Service
public class DescargaArchivoImpl implements IDescargaArchivo, Runnable {
	
	private static final Logger LOG = LoggerFactory.getLogger(DescargaArchivoImpl.class);
	
	@Autowired
	EnvioCorreoImpl envioCorreoImpl;
	
	@Autowired
	AmazonS3 amazonS3;
	
		
	@Value("${ruta.s3}")
	private String rutaS3;
	
	@Value("${ruta.local}")
	private String rLocal;
	
	@Value("${bucketName}")
	private String bucketName;
	
	private boolean validaDescarga = false;

	
	
	
	@Override
	public boolean descargaArchivoRepositorio() {			
		
			LOG.info("DESCARGANDO ARCHIVO...");
			run();
			new Thread().start();		
			
		return validaDescarga;
	}
	
	@Override
	public void run() {
		
		
		//descargaS3();
		
		
		HttpURLConnection http = null;
		BufferedInputStream in = null;
		FileOutputStream fos = null;
		BufferedOutputStream bout = null;
		String nuevaRutaS3 = null;
		String urlS3 = null;
		URL url = null;
		boolean continuaProceso = false;
		
		//String rutaS3 = "https://amalpdevbuk02a.s3.amazonaws.com/fir-alianza-pacifico/";
		
			
			try {
				nuevaRutaS3 = rutaS3 + armarArchivo();
				urlS3 = validaUrl(nuevaRutaS3);
				LOG.info("nuevaRutaS3: " + nuevaRutaS3);
				LOG.info("urlS3: " + urlS3);
				
				url = new URL(urlS3);				
				LOG.info("URL: " + url);
				
				continuaProceso = true;
			}catch(Exception e) {
				LOG.error("No se puede crear la URL " + urlS3 );
				e.printStackTrace();
			}
			
			if(continuaProceso) {		

			try {
				
				
				http = (HttpURLConnection) url.openConnection();
				LOG.info("HTTP: " + http);
				
				in = new BufferedInputStream(http.getInputStream());
				LOG.info("BufferedInputStream: " );
				continuaProceso = true;
				 
			}catch(IOException e) {
				LOG.error("No se puede crear el BufferedInputStream " + http );
				e.printStackTrace();
				continuaProceso = false;
			}			
			
			}
			
			if(continuaProceso) {
			try {
				fos = new FileOutputStream(rLocal + armarArchivo());
				LOG.info("FileOutputStream: " + fos);
				continuaProceso = true;
			}catch(IOException e) {
				LOG.error("No se puede crear el FileOutputStream " + rLocal + armarArchivo() );
				e.printStackTrace();
				continuaProceso = false;
			}
		}
			
			if(continuaProceso) {
			try {
				bout = new BufferedOutputStream(fos, 2048);
				LOG.info("BufferedOutputStream: " + bout);
				
				byte[] buffer = new byte[2048];				
				int read = 0;
				while ((read = in.read(buffer, 0, 2048)) >= 0) {
					bout.write(buffer, 0, read);
				}
				LOG.info("buffer read: " + read);
				bout.close();
				in.close();
				LOG.info("FINALIZA LA DESCARGA CORRECTAMENTE!!: ");
				
				validaDescarga = true;
				continuaProceso = true;
			}catch (IOException e) {
				LOG.error("No se puede crear el BufferedOutputStream de 2048");
				e.printStackTrace();
				continuaProceso = false;
				
				
			}
		}
			
		
			if(validaDescarga== false) {
				envioCorreoImpl.sendEmail();
			}
			

		
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
	
	public String validaUrl(String urlValida) {
		
		String salidaUrl = "";
		
		
		if(urlValida.contains("https")) {
			salidaUrl = urlValida;
		}else {
			if(urlValida.contains("s3")) {
				salidaUrl = urlValida.replace("s3", "https");
			}
		}
		
		
	return salidaUrl;	
	}
	
	

	
	
	/*
	public void descargaS3() {
		
		 OutputStream out= null;
		
		//Descarga objeto S3
		  String bucket_name = bucketName;
		 //String bucket_name = "s3://bucket-prueba-archivo/carpeta-prueba/";
		  
	        String key_name = armarArchivo();
	        LOG.info("nombre del bucket " + key_name );

	        LOG.info("DESCARGANDO... DE S3 BUCKET .." + key_name + " bucket: " +  bucket_name );
	       
	        try {
	        	        	
	            S3Object o = amazonS3.getObject(bucket_name, key_name);	            
	            S3ObjectInputStream s3is = o.getObjectContent();	           
	            FileUtils.copyInputStreamToFile(s3is, new File(rLocal+key_name));           
	            
	            
	            validaDescarga= true; 
	            LOG.info("DESCARGADO!");
	        } catch (AmazonServiceException e) {
	            System.err.println(e.getErrorMessage());
	            System.exit(1);
	            e.printStackTrace();
	        } catch (FileNotFoundException e) {
	            System.err.println(e.getMessage());
	            e.printStackTrace();
	            System.exit(1);
	        } catch (IOException e) {
	            System.err.println(e.getMessage());
	            e.printStackTrace();
	            System.exit(1);
	        }
	        
	        
	        
	}
	
	*/

}
