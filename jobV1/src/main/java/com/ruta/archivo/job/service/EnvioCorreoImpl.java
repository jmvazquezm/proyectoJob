package com.ruta.archivo.job.service;

import java.io.File;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ruta.archivo.job.constantes.Constantes;


@Service
public class EnvioCorreoImpl implements IEnvioCorreo {
	
	private static final Logger LOG = LoggerFactory.getLogger(EnvioCorreoImpl.class);
	
	
	@Value("${email.host}")
	private String host;

	@Value("${email.port}")
	private String port;

	@Value("${user}")
	private String user;

	@Value("${clave}")
	private String clave;

	@Value("${email.cuerpo.mensaje}")
	private String cuerpoMensaje;

	
	@Value("${destinatario_1}")
	private String destinatario1;
	
	@Value("${destinatario_2}")
	private String destinatario2;
	
	@Value("${destinatario_3}")
	private String destinatario3;
	
	@Value("${destinatario_4}")
	private String destinatario4;
	
	@Value("${destinatario_5}")
	private String destinatario5;
	
	@Value("${destinatario_6}")
	private String destinatario6;
	
	@Value("${destinatario_7}")
	private String destinatario7;
	
	@Value("${destinatario_8}")
	private String destinatario8;
	
	@Value("${img.correo}")
	private String imgCorreo;
	
	@Value("${nomImg}")
	private String nomImg;
	
	
	
	@Override
	public void sendEmail() {		

		// Configuración para enviar correo

		Properties properties = new Properties();

		properties.put(Constantes.JAVAX_MAIL_PROP_SMTP_HOST, host);
		properties.put(Constantes.JAVAX_MAIL_PROP_SMTP_PORT, port);

		properties.put(Constantes.JAVAX_MAIL_PROP_SMTP_TLS, "false");
		properties.put(Constantes.JAVAX_MAIL_PROP_SMTP_AUT, "false");
		properties.put(Constantes.JAVAX_MAIL_PROP_SMTP_USER, user);
		properties.put(Constantes.JAVAX_MAIL_PROP_SMTP_PASS, clave);
		
		//properties.put(Constantes.JAVAX_MAIL_PROP_SMTP_TRUST, "*");

		// Obtener la sesion

		Session session = Session.getInstance(properties, null);		
		int aviso = 0;
		try {
			// Crear el cuerpo del mensaje

			MimeMessage mimeMessage = new MimeMessage(session);

			// Agregar quien envia el correo
			mimeMessage.setFrom(new InternetAddress(user, "Alianza_pacifico@sat.gob.mx"));

			// Los destinatarios
			InternetAddress[] internetAddresses = { new InternetAddress(destinatario1),
					new InternetAddress(destinatario2),
					new InternetAddress(destinatario3),
					new InternetAddress(destinatario4),
					new InternetAddress(destinatario5),
					new InternetAddress(destinatario6),
					new InternetAddress(destinatario7),
					new InternetAddress(destinatario8)};

			// Agregar los destinatarios al mensaje
			mimeMessage.setRecipients(Message.RecipientType.TO, internetAddresses);

			// Agregar el asunto al correo
			//mimeMessage.setSubject("Estado de publicación de la Lista de Confianza.","UTF-8");
			mimeMessage.setSubject(MimeUtility.encodeText("Estado de publicaci\u00f3n de la Lista de Confianza", "utf-8", "B"));
			
			

			// Crear la parte del mensaje
			MimeBodyPart mimeBodyPart = new MimeBodyPart();			
			mimeBodyPart.setText(cuerpoMensaje, "UTF-8", "html");				
			
						
			// Url del directorio donde esta la imagen
			  
			   File directorio = new File(imgCorreo);			  
			   
			   Multipart multipart = new MimeMultipart();
			   
			   MimeBodyPart imagePart = new MimeBodyPart();
			   
			     imagePart.setHeader("Content-ID", "<"
			       + nomImg + ">");
			     
			     imagePart.setDisposition(MimeBodyPart.INLINE);
			     imagePart.attachFile(imgCorreo);
			     multipart.addBodyPart(imagePart);			     
			
			     multipart.addBodyPart(mimeBodyPart);			

			// Agregar el multipart al cuerpo del mensaje
			mimeMessage.setContent(multipart);			
			

			// Enviar el mensaje
			LOG.info("ENVIANDO EMAIL...");
			Transport transport = session.getTransport("smtp");			
			transport.connect(user, clave);			
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());			

			transport.close();

		} catch (Exception e) {			
			LOG.error("ERROR al enviar el email" );
			e.printStackTrace();
			
			aviso = 1;
		}
		if (aviso == 0) {
			LOG.info("CORREO ENVIADO CORRECTAMENTE");
		}

	}
	

}
