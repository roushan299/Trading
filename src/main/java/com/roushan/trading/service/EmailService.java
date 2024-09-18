package com.roushan.trading.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

   @Autowired
    private JavaMailSender javaMailSender;

    public void sendVerificationOtpMail(String email, String otp) {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, "utf-8");
        String subject = "Verify Otp";
        String text = "Your verification code is "+otp;

        try {
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text);
            mimeMessageHelper.setTo(email);
            javaMailSender.send(mimeMailMessage);
        }catch (MessagingException me){
            throw new MailSendException(me.getMessage());
        }

    }

}
