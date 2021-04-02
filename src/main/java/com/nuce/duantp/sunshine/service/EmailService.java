package com.nuce.duantp.sunshine.service;//package sunshine.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.stereotype.Service;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//
//@Service
//@EnableAsync
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    @Async
//    public void sendOtpMessage(String to, String subject, String message) throws MessagingException {
//
//        MimeMessage msg = javaMailSender.createMimeMessage();
//
//        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
//
//        helper.setTo(to);
//
//        helper.setSubject(subject);
//
//        helper.setText(message, true);
//
//        javaMailSender.send(msg);
//        System.out.println("email sended..........");
//    }
//
//}
