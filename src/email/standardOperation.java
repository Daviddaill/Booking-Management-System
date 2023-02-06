/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package email;

import java.io.File;
import java.sql.ResultSet;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;
import project.select;

/**
 *
 * @author daviddaillere
 */
public class standardOperation {

    static String port = "";
    static String host = "";
    static String password = "";
    static String from = "";
    
    public static void runTest(String thost, String tport, String tpassword, String tfrom) {

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", thost);
        properties.put("mail.smtp.port", tport);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(tfrom, tpassword);

            }

        });

        // Used to debug SMTP issues
        //session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(tfrom));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("contact@gerer-mon-gite.com"));

            // Set Subject: header field
            message.setSubject("running email test");

            // Now set the actual message
            message.setText("this client is running an email test: "+tfrom);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            JOptionPane.showMessageDialog(null, "Test réussi :)");
            
        } catch (MessagingException mex) {
           JOptionPane.showMessageDialog(null, "Test échoué: vérifier vos paramètres");
            mex.printStackTrace();
        }

    }
    
    public static void newUser( String user) {

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", "gerer-mon-gite.com");
        properties.put("mail.smtp.port", 465);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("contact@gerer-mon-gite.com", "Youarebeautiful_69");

            }

        });

        // Used to debug SMTP issues
        //session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress("contact@gerer-mon-gite.com"));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("contact@gerer-mon-gite.com"));

            // Set Subject: header field
            message.setSubject("nouvel utilisateur");

            // Now set the actual message
            message.setText("nouvelle utilisateur: "+user);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            
        } catch (MessagingException mex) {
           
            mex.printStackTrace();
        }

    }


    public static void sendMessage(String db, String to, String subject, String text) {

        // Recipient's email ID needs to be mentioned.
        ResultSet rs = select.getData(db, "select * from myInfo where ID= 1");

        try {
            while (rs.next()) {
                port = rs.getString("port");
                host = rs.getString("host");
                password = rs.getString("password");
                from = rs.getString("email");

            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(from, password);

            }

        });

        // Used to debug SMTP issues
        //session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(text);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
             JOptionPane success = new JOptionPane();
              success.showMessageDialog(null, "Email envoyé"); 
            
        } catch (MessagingException mex) {
           JOptionPane.showMessageDialog(null, "Email non reçu!");
            mex.printStackTrace();
        }

    }

    public static void sendMessage(String db, String to, String subject, String text, File file) {

        // Recipient's email ID needs to be mentioned.
        ResultSet rs = select.getData(db, "select * from myInfo where ID= 1");

        try {
            while (rs.next()) {
                port = rs.getString("port");
                host = rs.getString("host");
                password = rs.getString("password");
                from = rs.getString("email");

            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(from, password);

            }

        });

        // Used to debug SMTP issues
        //session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(text);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(text, "text/plain");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            mimeBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(file);

            mimeBodyPart.setDataHandler(new DataHandler(source));
            mimeBodyPart.setFileName(file.getName());

            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
             JOptionPane success = new JOptionPane();
              success.showMessageDialog(null, "Email envoyé"); 
        } catch (MessagingException mex) {
            JOptionPane.showMessageDialog(null, "Email non reçu!");
            mex.printStackTrace();
        }

    }

    public static void sendMessage(String db, String to, String subject, String text, File file, File file2) {

        // Recipient's email ID needs to be mentioned.
        ResultSet rs = select.getData(db, "select * from myInfo where ID= 1");

        try {
            while (rs.next()) {
                port = rs.getString("port");
                host = rs.getString("host");
                password = rs.getString("password");
                from = rs.getString("email");

            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(from, password);

            }

        });

        // Used to debug SMTP issues
        //session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(text);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(text, "text/plain");

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

            message.setContent(multipart);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
             JOptionPane success = new JOptionPane();
              success.showMessageDialog(null, "Email envoyé"); 
            
        } catch (MessagingException mex) {
           JOptionPane.showMessageDialog(null, "Email non reçu!");
            mex.printStackTrace();
        }

    }
    
    public static void sendMessage(String db, String to, String subject, String text, File file, File file2, File file3) {

        // Recipient's email ID needs to be mentioned.
        ResultSet rs = select.getData(db, "select * from myInfo where ID= 1");

        try {
            while (rs.next()) {
                port = rs.getString("port");
                host = rs.getString("host");
                password = rs.getString("password");
                from = rs.getString("email");

            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(from, password);

            }

        });

        // Used to debug SMTP issues
        //session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(text);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(text, "text/plain");

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

            message.setContent(multipart);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
             JOptionPane success = new JOptionPane();
              success.showMessageDialog(null, "Email envoyé"); 
           
        } catch (MessagingException mex) {
           JOptionPane.showMessageDialog(null, "Email non reçu!");
            mex.printStackTrace();
        }

    }

}
