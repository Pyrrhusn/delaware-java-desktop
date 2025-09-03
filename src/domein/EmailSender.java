package domein;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * EmailSender klasse, wordt gebruikt om emails te sturen
 */
public class EmailSender {

	/**
	 * Stuurt een betalingsherinnering als mail naar de klant die tot een bepaalde
	 * bestelling hoort
	 * 
	 * @param bestelling De bestelling waarvoor een betalingsherinnering gestuurd
	 *                   moet worden
	 */
	public static void stuurHerinnering(IBestelling bestelling) {
		String to = bestelling.getKlant().getBedrijf().getContactGegevens().get("email");

		String subject = String.format("BetalingsHerinnering: %s (%s) ", bestelling.berekenBedrag(),
				bestelling.getOrderID());

		String body = String.format("Beste u hebt nog een openstaande rekening van: %f van bedrijf %s met orderId: %s",
				bestelling.berekenBedrag(), bestelling.getLeverancier().getUsername(), bestelling.getOrderID());

		sendEmail(to, subject, body);
	}

	/**
	 * Stuurt emails
	 * 
	 * @param to      De ontvanger van de email
	 * @param subject De titel van de email
	 * @param body    De inhoud van de email
	 */
	public static void sendEmail(String to, String subject, String body) {

		String from = "delawaregroupone@outlook.com";

		final String username = "delawaregroupone@outlook.com";
		final String password = "testww1!";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.outlook.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);

			message.setFrom(new InternetAddress(from));

			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			message.setSubject(subject);

			message.setText(body);

			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
