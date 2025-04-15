package org.nilesh.blogproject.services;

import org.nilesh.blogproject.utils.email.emaildetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public boolean sendSimpleEmail(emaildetails emaildetails) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);  // Uses email from application.properties
            message.setTo(emaildetails.getRecipent());
            message.setText(emaildetails.getMsgBody());
            message.setSubject(emaildetails.getSubject());
            javaMailSender.send(message);
            return true;
            
        } catch (Exception e) {
           return false;
        }
        
       
        
    }
}
