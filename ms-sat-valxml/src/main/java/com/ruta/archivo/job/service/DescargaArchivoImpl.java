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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;

/**
 * Class DescargaArchivoImpl.
 *
 * @author FSW-SYE
 * @since 5 feb. 2021
 */
@Service
public class DescargaArchivoImpl implements IDescargaArchivo, Runnable {

	/** La constante LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(DescargaArchivoImpl.class);

	/** La variable que contiene informacion con respecto a: amazon S 3. */
	@Autowired
	AmazonS3 amazonS3;

	/** La variable que contiene informacion con respecto a: ruta S 3. */
	@Value("${ruta.s3}")
	private String rutaS3;

	/** La variable que contiene informacion con respecto a: r local. */
	@Value("${ruta.local}")
	private String rLocal;

	/** La variable que contiene informacion con respecto a: bucket name. */
	@Value("${bucketName}")
	private String bucketName;

	/** La variable que contiene informacion con respecto a: valida descarga. */
	private boolean validaDescarga = false;

	/**
	 * Descarga archivo repositorio.
	 *
	 * @return true, si exitoso
	 */
	@Override
	public boolean descargaArchivoRepositorio() {

		LOG.info("Inicia la descarga del archivo...");
		run();
		new Thread().start();

		return validaDescarga;
	}

	/**
	 * Run.
	 */
	@Override
	public void run() {

		HttpsURLConnection http = null;
		BufferedInputStream in = null;
		FileOutputStream fos = null;
		BufferedOutputStream bout = null;
		String nuevaRutaS3 = null;
		String urlS3 = null;
		URL url = null;
		boolean continuaProceso = false;

		try {
			nuevaRutaS3 = rutaS3 + armarArchivo();
			urlS3 = validaUrl(nuevaRutaS3);
			url = new URL(urlS3);
			LOG.info("Se cargo la URL");
			continuaProceso = true;
		} catch (Exception e) {
			LOG.error("No se puede crear la URL ");
			LOG.error(e.getMessage());
		}

		if (continuaProceso) {

			try {

				http = (HttpsURLConnection) url.openConnection();
				http.setRequestMethod("GET");
				byte[] encodedAuth = Base64.encodeBase64(url.getUserInfo().getBytes(StandardCharsets.UTF_8));
				String authHeaderValue = "Basic " + new String(encodedAuth);
				http.setRequestProperty("Authorization", authHeaderValue);
				http.setRequestProperty("Content-Type", "text/xml");
				http.getResponseCode();
				LOG.info("HTTP: " + http);

				in = new BufferedInputStream(http.getInputStream());
				continuaProceso = true;

			} catch (IOException e) {
				LOG.error("No se puede crear el BufferedInputStream para http");
				LOG.error(e.getMessage());
				continuaProceso = false;
			}

		}

		if (continuaProceso) {
			try {

				fos = new FileOutputStream(rLocal + armarArchivo());
				continuaProceso = true;
			} catch (IOException e) {
				LOG.error("No se puede crear el FileOutputStream para obtener el archivo XML");
				LOG.error(e.getMessage());
				continuaProceso = false;
			}
			finally
			{
				try {
					if (fos != null)
						fos.close();
				} catch (IOException e) {
					LOG.error(e.getMessage());
				}
			}
		}

		if (continuaProceso) {
			try {
				bout = new BufferedOutputStream(fos, 4096);
				byte[] buffer = new byte[4096];
				int read = 0;
				while ((read = in.read(buffer, 0, 4096)) >= 0) {
					bout.write(buffer, 0, read);
				}
				bout.close();
				in.close();
				LOG.info("Finaliza la descarga correctamente!!: ");
				LOG.trace(urlS3);
				validaDescarga = true;
				continuaProceso = true;
			} catch (IOException e) {
				LOG.error("No se puede crear el BufferedOutputStream de 4096");
				LOG.error(e.getMessage());
				continuaProceso = false;

			}
			finally
			{

				try {
					if (bout != null)
					  bout.close();
				} catch (IOException e) {
					LOG.error(e.getMessage());
				}
				try {
					if (in != null)
					 in.close();
				} catch (IOException e) {
					LOG.error(e.getMessage());
				}
			}
		}
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
		nombreArchivo = "TSP_SE_" + formateador.format(ahora) + ".xml";
		return nombreArchivo;

	}

	/**
	 * Valida url.
	 *
	 * @param urlValida El objeto: url valida
	 * @return Objeto string
	 */
	public String validaUrl(String urlValida) {

		String salidaUrl = "";

		if (urlValida.contains("https")) {
			salidaUrl = urlValida;
		} else {
			if (urlValida.contains("s3")) {
				salidaUrl = urlValida.replace("s3", "https");
			}
		}

		return salidaUrl;
	}

}