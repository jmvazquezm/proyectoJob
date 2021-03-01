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
import java.text.SimpleDateFormat;
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

/**
 * Class CargaS3Impl.
 *
 * @author FSW-SYE
 * @since 5 feb. 2021
 */
@Service
public class CargaS3Impl implements ICargaS3 {

	/** La constante LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(CargaS3Impl.class);

	/** La variable que contiene informacion con respecto a: amazon S 3. */
	@Autowired
	AmazonS3 amazonS3;

	/** La variable que contiene informacion con respecto a: ruta archivo impl. */
	@Autowired
	RutaArchivoImpl rutaArchivoImpl;

	/** La variable que contiene informacion con respecto a: r local. */
	@Value("${ruta.local}")
	private String rLocal;

	/** La variable que contiene informacion con respecto a: bucket name. */
	@Value("${bucketName}")
	private String bucketName;

	/** La variable que contiene informacion con respecto a: buket descarga. */
	@Value("${buketDescarga}")
	private String buketDescarga;

	/** La variable que contiene informacion con respecto a: ruta S 3. */
	@Value("${ruta.s3}")
	private String rutaS3;

	/** La variable que contiene informacion con respecto a: r local 2. */
	@Value("${ruta.local2}")
	private String rLocal2;

	/**
	 * Subir archivo S 3.
	 */
	@Override
	public void subirArchivoS3() {

		try {

			rutaArchivoImpl.agregarElemento();

			LOG.info("Inicia Subir objeto a S3 desde el servidor ");
			File file = new File(rLocal2 + armarArchivo());

			LOG.info("Obtiene datos del archivo ");
			DiskFileItem fileItem = new DiskFileItem("file", "text/plain", false, file.getName(), (int) file.length(),
					file.getParentFile());

			fileItem.getOutputStream();

			LOG.info("Inicia upload File ");
			uploadFile(file);
			LOG.info("Finaliza upload File ");
			LOG.info("Finaliza la carga de objeto a S3");

			if (file.delete())
				LOG.info("El archivo ha sido borrado satisfactoriamente");
			else
				LOG.error("El archivo no puede ser borrado");

		} catch (AmazonServiceException ase) {
			LOG.error("Se ha detectado un AmazonServiceException "
					+ "La solicitud a Amazon S3 fue rechazada con una respuesta de error.");
			LOG.info("Mensaje Error: ");
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
			LOG.error("Se ha detectado un AmazonClientException " + "Encontro un error interno al comunicarse con S3.");
			LOG.info("Error Message: " + ace.getMessage());

		} catch (IOException e) {
			e.getMessage();

		}
	}

	/**
	 * Upload file.
	 *
	 * @param file El objeto: file
	 */
	public void uploadFile(final File file) {
		LOG.info("File upload in progress.");
		try {
			uploadFileToS3Bucket(bucketName, file);
			LOG.info("File upload is completed. " + bucketName + file);
			// file.delete(); // To remove the file locally created in the project folder.
		} catch (final AmazonServiceException ex) {
			LOG.info("File upload is failed." + ex.getMessage());

		}
	}

	/**
	 * Upload file to S 3 bucket.
	 *
	 * @param bucketName El objeto: bucket name
	 * @param file El objeto: file
	 */
	private void uploadFileToS3Bucket(final String bucketName, final File file) {
		final String uniqueFileName = file.getName();

		LOG.info("Uploading file with name " + uniqueFileName);
		final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file);
		amazonS3.putObject(putObjectRequest);
		amazonS3.setObjectAcl(bucketName, uniqueFileName, CannedAccessControlList.PublicRead);

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

		LOG.info("Prueba fecha" + formateador.format(ahora));

		nombreArchivo = "TSP_SE_" + formateador.format(ahora) + ".xml";

		LOG.info("NOMBRE ARCHIVO " + nombreArchivo);

		return nombreArchivo;

	}

}
