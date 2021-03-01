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

/**
 * Class EnvioCorreoImpl.
 *
 * @author FSW-SYE
 * @since 5 feb. 2021
 */
@Service
public class EnvioCorreoImpl implements IEnvioCorreo {

	/** La constante LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(EnvioCorreoImpl.class);

	/** La variable que contiene informacion con respecto a: host. */
	@Value("${email.host}")
	private String host;

	/** La variable que contiene informacion con respecto a: port. */
	@Value("${email.port}")
	private String port;

	/** La variable que contiene informacion con respecto a: user. */
	@Value("${user}")
	private String user;

	/** La variable que contiene informacion con respecto a: clave. */
	@Value("${clave}")
	private String clave;

	/** La variable que contiene informacion con respecto a: cuerpo mensaje. */
	@Value("${email.cuerpo.mensaje}")
	private String cuerpoMensaje;

	/** La variable que contiene informacion con respecto a: destinatario 1. */
	@Value("${destinatario_1}")
	private String destinatario1;

	/** La variable que contiene informacion con respecto a: concopia 1. */
	@Value("${concopia_1}")
	private String concopia1;

	@Value("${remitente_1}")
	private String remitente1;

	/** La variable que contiene informacion con respecto a: img correo. */
	@Value("${img.correo}")
	private String imgCorreo;

	/** La variable que contiene informacion con respecto a: nom img. */
	@Value("${nomImg}")
	private String nomImg;

	/**
	 * Send email.
	 */
	@Override
	public void sendEmail() {

		// Configuracion para enviar correo

		Properties properties = new Properties();

		properties.put(Constantes.JAVAX_MAIL_PROP_SMTP_HOST, host);
		properties.put(Constantes.JAVAX_MAIL_PROP_SMTP_PORT, port);
		properties.put(Constantes.JAVAX_MAIL_PROP_SMTP_TLS, "false");
		properties.put(Constantes.JAVAX_MAIL_PROP_SMTP_AUT, "true");
		properties.put(Constantes.JAVAX_MAIL_PROP_SMTP_USER, user);
		properties.put(Constantes.JAVAX_MAIL_PROP_SMTP_PASS, clave);

		// Obtener la sesion

		Session session = Session.getInstance(properties, null);
		int aviso = 0;
		try {
			// Crear el cuerpo del mensaje

			MimeMessage mimeMessage = new MimeMessage(session);

			// Agregar quien envia el correo
			mimeMessage.setFrom(new InternetAddress(user, remitente1));

			// Agregar los destinatarios al mensaje

			mimeMessage.setRecipients(Message.RecipientType.TO, destinatario1);
			mimeMessage.setRecipients(Message.RecipientType.CC, concopia1);

			// Agregar el asunto al correo

			mimeMessage.setSubject(
			MimeUtility.encodeText("Estado de publicaci\u00f3n de la Lista de Confianza", "utf-8", "B"));

			// Crear la parte del mensaje
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setText(cuerpoMensaje, "UTF-8", "html");

			// Url del directorio donde esta la imagen

			File directorio = new File(imgCorreo);

			Multipart multipart = new MimeMultipart();

			MimeBodyPart imagePart = new MimeBodyPart();

			imagePart.setHeader("Content-ID", "<" + nomImg + ">");

			imagePart.setDisposition(MimeBodyPart.INLINE);
			imagePart.attachFile(imgCorreo);
			multipart.addBodyPart(imagePart);

			multipart.addBodyPart(mimeBodyPart);

			// Agregar el multipart al cuerpo del mensaje
			mimeMessage.setContent(multipart);

			// Enviar el mensaje

			LOG.info("Enviando Email...");
			Transport transport = session.getTransport("smtp");
			transport.connect(host, user, clave);
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());

			transport.close();

		} catch (Exception e) {
			LOG.error("ERROR al enviar el emai");
			LOG.error("ERROR: "+ e.getMessage());
			//e.printStackTrace();
			aviso = 1;
		}
		if (aviso == 0) {
			LOG.info("Correo enviado correctamente");
		}

	}

}