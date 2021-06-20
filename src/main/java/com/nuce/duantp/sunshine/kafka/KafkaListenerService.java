//package com.nuce.duantp.sunshine.kafka;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.nuce.duantp.onesignal.OnesignalController;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.messaging.MessagingException;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Service;
//
//import java.net.URISyntaxException;
//
//
//@Service
//@Slf4j
//public class KafkaListenerService {
//    @Autowired
//    JavaMailSender javaMailSender;
//    @Autowired
//    OnesignalController onesignalController;
////    @KafkaListener(groupId = "test_notify_risk", topics = "test_sender_email", containerFactory =
////            "kafkaListenerContainerStringTransactionFactory")
////    public void checkRisk(@Payload String mess) throws MessagingException, NoSuchFieldException,
////            IllegalAccessException, URISyntaxException, JsonProcessingException {
//////        KafkaConvertResult<String > kafkaConvertResult = new KafkaConvertResult<>(String.class);
////        SimpleMailMessage message = new SimpleMailMessage();
////        message.setFrom("sunshine87lethanhnghi@gmail.com");
////        message.setTo("tranphuduan@gmail.com");
////        message.setSubject("");
////        message.setText(mess);
////        javaMailSender.send(message);
////    }
//
//    @KafkaListener(groupId = "test_notify_risk", topics = "test_sender_email", containerFactory =
//            "kafkaListenerContainerStringTransactionFactory")
//    public void checkRisk(@Payload String mess) {
//        try {
//            onesignalController.sendNotify(mess);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }
//}
