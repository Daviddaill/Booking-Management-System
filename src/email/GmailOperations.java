/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package email;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import java.io.File;
import static email.GmailOauth.*;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class GmailOperations {

    public static void sendMessage(Gmail service, String userId, MimeMessage email)
            throws MessagingException, IOException {
        Message message = createMessageWithEmail(email);
        message = service.users().messages().send(userId, message).execute();

        System.out.println("Message id: " + message.getId());
        System.out.println(message.toPrettyString());
    }

    public static Message createMessageWithEmail(MimeMessage email) throws MessagingException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        email.writeTo(baos);
        String encodedEmail = Base64.encodeBase64URLSafeString(baos.toByteArray());
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    public static MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException, IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from)); //me
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to)); //
        email.setSubject(subject);

        email.setText(bodyText);

        return email;
    }

    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to Email address of the receiver.
     * @param from Email address of the sender, the mailbox account.
     * @param subject Subject of the email.
     * @param bodyText Body text of the email.
     * @param file Path to the file to be attached.
     * @return MimeMessage to be used to send email.
     * @throws MessagingException
     */
    public static MimeMessage createEmailWithAttachment(String to,
            String from,
            String subject,
            String bodyText,
            File file)
            throws MessagingException, IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(bodyText, "text/plain");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file);

        mimeBodyPart.setDataHandler(new DataHandler(source));
        mimeBodyPart.setFileName(file.getName());

        multipart.addBodyPart(mimeBodyPart);
        email.setContent(multipart);

        return email;
    }

    public static MimeMessage createEmailWithAttachment(String to,
            String from,
            String subject,
            String bodyText,
            File file, File file2)
            throws MessagingException, IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(bodyText, "text/plain");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file);

        mimeBodyPart.setDataHandler(new DataHandler(source));
        mimeBodyPart.setFileName(file.getName());

        multipart.addBodyPart(mimeBodyPart);

        mimeBodyPart = new MimeBodyPart();
        DataSource source2 = new FileDataSource(file2);

        mimeBodyPart.setDataHandler(new DataHandler(source2));
        mimeBodyPart.setFileName(file.getName());

        multipart.addBodyPart(mimeBodyPart);

        email.setContent(multipart);

        return email;
    }

    public static MimeMessage createEmailWithAttachment(String to,
            String from,
            String subject,
            String bodyText,
            File file, File file2, File file3)
            throws MessagingException, IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(bodyText, "text/plain");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file);

        mimeBodyPart.setDataHandler(new DataHandler(source));
        mimeBodyPart.setFileName(file.getName());

        multipart.addBodyPart(mimeBodyPart);

        mimeBodyPart = new MimeBodyPart();
        DataSource source2 = new FileDataSource(file2);

        mimeBodyPart.setDataHandler(new DataHandler(source2));
        mimeBodyPart.setFileName(file.getName());

        multipart.addBodyPart(mimeBodyPart);

        mimeBodyPart = new MimeBodyPart();
        DataSource source3 = new FileDataSource(file3);

        mimeBodyPart.setDataHandler(new DataHandler(source3));
        mimeBodyPart.setFileName(file.getName());

        multipart.addBodyPart(mimeBodyPart);

        email.setContent(multipart);

        return email;
    }

    /*public static void sendEmail() throws IOException, GeneralSecurityException, MessagingException {
		
		Gmail service = GmailService();
		MimeMessage Mimemessage = createEmail("dailleredavid@gmail.com","me","This my demo test subject","This is my body text");
	
		Message message = createMessageWithEmail(Mimemessage);
		
		message = service.users().messages().send("me", message).execute();
		
		System.out.println("Message id: " + message.getId());
		System.out.println(message.toPrettyString());
	}

	

	public static void main(String[] args) throws IOException, GeneralSecurityException, MessagingException {
		
		sendEmail();
		
	}
        
     */
    public static void send(String to, String from, String subject, String bodyText) throws MessagingException, IOException, GeneralSecurityException {

        MimeMessage mm = createEmail(to,
                from,
                subject,
                bodyText);

        sendMessage(GmailService(), from, mm);

    }

    public static void send(String to, String from, String subject, String bodyText, File file) throws MessagingException, IOException, GeneralSecurityException {

        MimeMessage mm = createEmailWithAttachment(to,
                from,
                subject,
                bodyText, file);

        sendMessage(GmailService(), from, mm);

    }

    public static void send(String to, String from, String subject, String bodyText, File file1, File file2) throws MessagingException, IOException, GeneralSecurityException {

        MimeMessage mm = createEmailWithAttachment(to,
                from,
                subject,
                bodyText, file1, file2);

        sendMessage(GmailService(), from, mm);

    }

    public static void send(String to, String from, String subject, String bodyText, File file1, File file2, File file3) throws MessagingException, IOException, GeneralSecurityException {

        MimeMessage mm = createEmailWithAttachment(to,
                from,
                subject,
                bodyText, file1, file2, file3);

        sendMessage(GmailService(), from, mm);

    }

}
