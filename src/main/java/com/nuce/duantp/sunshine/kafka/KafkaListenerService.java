package com.nuce.duantp.sunshine.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;


@Service
@Slf4j
public class KafkaListenerService {
    @Autowired
    JavaMailSender javaMailSender;

    @KafkaListener(groupId = "sender_email_group", topics = "sender_email", containerFactory =
            "kafkaListenerContainerStringTransactionFactory")
    public void checkRisk(@Payload String mess) throws MessagingException, NoSuchFieldException,
            IllegalAccessException, URISyntaxException, JsonProcessingException {
//        KafkaConvertResult<String > kafkaConvertResult = new KafkaConvertResult<>(String.class);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("sunshine87lethanhnghi@gmail.com");
        message.setTo("tranphuduan@gmail.com");
        message.setSubject("");
        message.setText(mess);
        javaMailSender.send(message);
    }


}
