package com.kosuri.stores;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class EmailVerification {

    private static final String EMAIL_HOST = "smtp.gmail.com";
    private static final String EMAIL_USERNAME = "mekanik@kosuriers.com";
    private static final String EMAIL_PASSWORD = "BabuMekanik24@";
    private static final String FROM_EMAIL = "mrmason.in@kosuriers.com";
    
    
    public static void main(String[] args) {
        sendEmailVerification("mekanik@kosuriers.com");
    }
    public static String sendEmailVerification(String recipientEmail) {
        // Generate a random 6-digit OTP
        String otp = generateOTP();

        // Assuming you have the necessary email server configuration
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", EMAIL_HOST);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587"); // Use port 587 for TLS

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject("Email Verification OTP");

            // Set the content of your email, including the OTP
            message.setText("Your OTP for email verification is: " + otp);

            // Send the message
             Transport.send(message);
            System.out.println("Email sent successfully.");
            
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
        return otp;
    }

    public static String generateOTP() {
        // Generate a random 6-digit OTP
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        return String.valueOf(otpValue);
    }
}
