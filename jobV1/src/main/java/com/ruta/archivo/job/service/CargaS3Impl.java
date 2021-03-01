package com.ruta.archivo.job.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;





@Service
public class CargaS3Impl implements ICargaS3 {
	
	private static final Logger LOG = LoggerFactory.getLogger(CargaS3Impl.class);
	
	@Autowired
	AmazonS3 amazonS3;
	
		
	@Autowired
	RutaArchivoImpl rutaArchivoImpl;
	
	@Value("${ruta.local}")
	private String rLocal;
	
	@Value("${bucketName}")
	private String bucketName;
	
	@Value("${buketDescarga}")
	private String buketDescarga;
	
	
	
	@Value("${ruta.s3}")
	private String rutaS3;
	
	@Value("${ruta.local2}")
	private String rLocal2;
	
	
	
	@Override
	public void subirArchivoS3() {				
		
		try {
			
			
			
					
		rutaArchivoImpl.agregarElemento();	
		
			
			LOG.info("SUBIENDO OBJETO A S3 DESDE SERVIDOR ");			
			File file = new File(rLocal2+armarArchivo());
			
			LOG.info("OBTIENE DATOS DE ARCHIVO ");					
			DiskFileItem fileItem = new DiskFileItem("file", "text/plain", false, file.getName(), (int) file.length() , file.getParentFile());
			
				fileItem.getOutputStream();
			
				LOG.info("INICIA UPLOAD FILE ");		
			uploadFile(file);
			LOG.info("FINALIZA UPLOAD FILE ");
			LOG.info("FINALIZA LA CARGA DE OBJETO A S3");
			

		} catch (AmazonServiceException ase) {
			LOG.error("Se ha detectado una AmazonServiceException"
					+ "La solicitud a Amazon S3 pero fue rechazada con una respuesta de error.");
			LOG.info("Mensaje Error: " );
			 ase.getMessage();
			LOG.info("HTTP Status Code: ");
			ase.getStatusCode();
			LOG.info("AWS Error Code: ");
			ase.getErrorCode();
			LOG.info("Request ID: ");
			ase.getRequestId();
			LOG.info("Error Type: ");
			ase.getErrorType();
		} catch (AmazonClientException ace) {
			LOG.error("Se ha detectado una AmazonClientException" + "Encontro un error interno al comunicarse con S3.");
			LOG.info("Error Message: " + ace.getMessage());
			
		} catch (IOException e) {
			e.getMessage();
			
			
		}		
	}
	
	
		
	public void uploadFile(final File file) {
		LOG.info("File upload in progress.");
		try {
			uploadFileToS3Bucket(bucketName, file);			
			LOG.info("File upload is completed. " + bucketName + file);
			
			//file.delete();	// To remove the file locally created in the project folder.
		} catch (final AmazonServiceException ex) {
			LOG.info("File upload is failed." + ex.getMessage());			
			
		}
	}
	
	
	private void uploadFileToS3Bucket(final String bucketName, final File file) {
		final String uniqueFileName =  file.getName();
		//String rutaArchivoBucket = buketDescarga + uniqueFileName;
		
		LOG.info("Uploading file with name " + uniqueFileName );
		//LOG.info(uniqueFileName);
		//final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file);	
		//final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, rutaArchivoBucket, file );
		final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,  uniqueFileName, file );
		amazonS3.putObject(putObjectRequest);		
		amazonS3.setObjectAcl(bucketName, uniqueFileName, CannedAccessControlList.PublicRead);
		
		
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

}
