package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.dto.request.SendEmailReq;
import com.phamtan.base.email.data_structure.EmailContentData;
import com.phamtan.base.email.request.EmailRequest;
import com.phamtan.base.email.service.EmailService;
import com.phamtan.base.enumeration.EmailEnum;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/send-email")
public class SendEmailController {
    @Autowired
    private Configuration configuration;
    @Autowired
    private EmailService emailService;
    @PostMapping("/")
    public void sendEmail(@RequestBody SendEmailReq sendEmailReq){
        try {
            Template template = configuration.getTemplate(sendEmailReq.getFileName());
            EmailRequest emailRequest = new EmailRequest();
            List<EmailContentData> contents = new ArrayList<>();
//            EmailContentData emailContentData = EmailContentData.builder()
//                    .key(EmailEnum.FILE)
//                    .name("duan.pdf")
//                    .data("D:\\Nuce\\doantonghop_cnpm\\src\\main\\resources\\static\\generateReport.pdf")
//                    .build();
//            EmailContentData imageConten = EmailContentData.builder()
//                    .key(EmailEnum.IMAGE)
//                    .name("asbnotebook")
//                    .data("D:\\Nuce\\doantonghop_cnpm\\src\\main\\resources\\static\\asbnotebook.png")
//                    .build();
            EmailContentData nameContentData = EmailContentData.builder()
                    .key(EmailEnum.TEXT)
                    .name("name")
                    .data(sendEmailReq.getName())
                    .build();
            EmailContentData valueContentData = EmailContentData.builder()
                    .key(EmailEnum.TEXT)
                    .name("value")
                    .data(sendEmailReq.getMessage())
                    .build();
//            contents.add(emailContentData);
            contents.add(nameContentData);
            contents.add(valueContentData);
//            contents.add(imageConten);

            emailRequest.setTo(sendEmailReq.getTo());
            emailRequest.setFrom(sendEmailReq.getFrom());
            emailRequest.setSubject(sendEmailReq.getSubject());
            emailRequest.setContent(contents);
            emailService.sendMailWithAttachments(emailRequest, template);
            System.out.println("gui email thanh cong");
        }catch (Exception e){
            System.out.println("Loi gui email");
        }

    }

}
