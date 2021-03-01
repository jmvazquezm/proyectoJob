package com.ruta.archivo.job.peticion.servicio;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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


@Service
public class PeticionServicio{
	
	private static final Logger LOG = LoggerFactory.getLogger(PeticionServicio.class);
	
			
	@Value("${url.post.servicio}")
	private String urlPost;

	@Autowired
	RestTemplate restTemplate;
	
		
	public String postProductList() {
		LOG.info("Leyendo servicio genera lista ");
		String r = null;		
		
		LinkedMultiValueMap<String, Object> lMap = new LinkedMultiValueMap<>(); 		
		lMap.add("keyName", armarArchivo());
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		HttpEntity<LinkedMultiValueMap<String, Object>> requestBody = new HttpEntity<>(lMap, headers);
		//HttpEntity<String> requestBody = new HttpEntity<>("prueba genera lista");
		LOG.info("requestBody " + requestBody);
		try {
			LOG.info("Dentro del try ");
			LOG.info("URL_SERVICIO_GENERA " + urlPost);
			 r =restTemplate.exchange(urlPost, HttpMethod.POST, requestBody, String.class).getBody();
			 		 
					 
			 LOG.info("restTemplate " + r);

		}catch(Exception e){
			LOG.error(e.getMessage());
			e.printStackTrace();
		}

		return r;
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
