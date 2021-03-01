/*
 * Derechos Reservados 2016 SAT.
 * Servicio de Administracion Tributaria (SAT).
 *
 * Este software contiene informacion propiedad exclusiva del SAT considerada
 * Confidencial. Queda totalmente prohibido su uso o divulgacion en forma
 * parcial o total.
 *
 */
package com.ruta.archivo.job.peticion.servicio;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Class PeticionServicio.
 *
 * @author FSW-SYE
 * @since 5 feb. 2021
 */
@Service
public class PeticionServicio {

	/** La constante LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(PeticionServicio.class);

	/** La variable que contiene informacion con respecto a: url post. */
	@Value("${url.post.servicio}")
	private String urlPost;

	/** La variable que contiene informacion con respecto a: rest template. */
	@Autowired
	RestTemplate restTemplate;

	/**
	 * Post product list.
	 *
	 * @return Objeto string
	 */
	public String postProductList() {
		LOG.info("Leyendo servicio genera lista ");
		String r = null;

		LinkedMultiValueMap<String, Object> lMap = new LinkedMultiValueMap<>();
		lMap.add("keyName", armarArchivo());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<LinkedMultiValueMap<String, Object>> requestBody = new HttpEntity<>(lMap, headers);
		LOG.info("requestBody " + requestBody);
		try {
			LOG.debug("Dentro del try ");
			LOG.info("URL_SERVICIO_GENERA " + urlPost);
			r = restTemplate.exchange(urlPost, HttpMethod.POST, requestBody, String.class).getBody();

			LOG.debug("restTemplate " + r);

		} catch (Exception e) {
			LOG.error(e.getMessage());
			//e.printStackTrace();
		}

		return r;
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

		LOG.debug("Prueba fecha" + formateador.format(ahora));

		nombreArchivo = "TSP_SE_" + formateador.format(ahora) + ".xml";

		LOG.info("NOMBRE ARCHIVO " + nombreArchivo);

		return nombreArchivo;

	}

}