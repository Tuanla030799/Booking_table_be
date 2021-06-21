package com.nuce.duantp.sunshine.kafka;

import com.nuce.duantp.onesignal.OnesignalController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;


@Service
@Slf4j
public class KafkaListenerService {
    @Autowired
    OnesignalController onesignalController;
//    @KafkaListener(groupId = "test_notify_risk", topics = "test_sender_email", containerFactory =
//            "kafkaListenerContainerStringTransactionFactory")
//    public void checkRisk(@Payload String mess) throws MessagingException, NoSuchFieldException,
//            IllegalAccessException, URISyntaxException, JsonProcessingException {
////        KafkaConvertResult<String > kafkaConvertResult = new KafkaConvertResult<>(String.class);
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("sunshine87lethanhnghi@gmail.com");
//        message.setTo("tranphuduan@gmail.com");
//        message.setSubject("");
//        message.setText(mess);
//        javaMailSender.send(message);
//    }

    @KafkaListener(groupId = "test_notify", topics = "test_topic")
    public void senderBookingCancel(@Payload String bookingId) {
        try {
            onesignalController.sendNotify(bookingId);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
