package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.JasperReports.ReportService;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.model.tbl_Customer;
import com.nuce.duantp.sunshine.model.tbl_ResponseStatusCode;
import com.nuce.duantp.sunshine.repository.CustomerRepo;
import com.nuce.duantp.sunshine.repository.ResponseStatusCodeRepo;
import com.phamtan.base.email.data_structure.EmailContentData;
import com.phamtan.base.email.request.EmailRequest;
import com.phamtan.base.email.service.EmailService;
import com.phamtan.base.enumeration.EmailEnum;
import com.phamtan.base.onesingnal.inout.request.DataDetail;
import com.phamtan.base.onesingnal.inout.request.NotificationRequest;
import com.phamtan.base.onesingnal.service.OneSignalService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class testCon {
    //private Account account ;
    @Autowired
    private ReportService service;
    @Autowired
    private Configuration configuration;
    @Value("${phamtan.onesignal.app-id}")
    private String appId;
    @Autowired
    private OneSignalService oneSignalService;

//    @Autowired
//    MailService mailService;

    //    @GetMapping("/report")
//    public String generateReport() throws FileNotFoundException, JRException {
//        String fileName="generateReport";
//        String url=service.exportReport("pdf",fileName);
//        mailService.creatEmail("tranphuduan@gmail.com",fileName,"Duaan tp",url);
//        return url;
//    }
//
//    @Autowired
//    private MailService mailService;
    @Autowired
    private EmailService emailService;
//
//    @PostMapping("send-mail")
//    public ResponseEntity<String> sendMail(@RequestBody EmailRequestDto emailRequest) {
//        Map<String, String> model = new HashMap<>();
//        model.put("name", emailRequest.getName());
//        model.put("value", "Welcome to ASB Notebook!!");
//        String response = mailService.sendMail(emailRequest, model);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    @GetMapping("/test")
    void test() throws IOException, MessagingException, URISyntaxException {
        Template template = configuration.getTemplate("email.ftl");
        EmailRequest emailRequest = new EmailRequest();
        List<EmailContentData> contents = new ArrayList<>();
        EmailContentData emailContentData = EmailContentData.builder()
                .key(EmailEnum.FILE)
                .name("duan.pdf")
                .data("D:\\Nuce\\doantonghop\\src\\main\\resources\\static\\generateReport.pdf")
                .build();
        EmailContentData imageConten = EmailContentData.builder()
                .key(EmailEnum.IMAGE)
                .name("asbnotebook")
                .data("D:\\Nuce\\doantonghop\\src\\main\\resources\\static\\asbnotebook.png")
                .build();
        EmailContentData nameContentData = EmailContentData.builder()
                .key(EmailEnum.TEXT)
                .name("name")
                .data("do thi dieulinh")
                .build();
        EmailContentData valueContentData = EmailContentData.builder()
                .key(EmailEnum.TEXT)
                .name("value")
                .data(" value ")
                .build();
        contents.add(emailContentData);
        contents.add(nameContentData);
        contents.add(valueContentData);
        contents.add(imageConten);

        emailRequest.setTo("tranphuduan@gmail.com");
        emailRequest.setFrom("tranphuduan@gmail.com");
        emailRequest.setSubject("Hello ");
        emailRequest.setContent(contents);
        emailService.sendMailWithAttachments(emailRequest, template);

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setAppId(appId);
        notificationRequest.setChromeWebIcon("https://picsum.photos/200");
        HashMap<String, Map<String, String>> data = new HashMap<>();
//        Map<String,String> dataDetail = new HashMap<>();
//        dataDetail.put("foo","dfafaf");
//        data.put("data",dataDetail);
        Map<String, String> content1 = new HashMap<>();
        content1.put("en", "do thi dieu linh");
        notificationRequest.setContents(content1);
        DataDetail dataDetail = new DataDetail();
        dataDetail.setKey("foo");
        dataDetail.setValue("bar");
        HashMap<String, DataDetail> data1 = new HashMap<>();
        notificationRequest.setData(data1);
        oneSignalService.createNotify(notificationRequest);

    }

    @Autowired
    TestService testService;
    @Autowired
    CustomerRepo customer;
    @Autowired
    ResponseStatusCodeRepo responseStatusCodeRepo;
    @PostMapping("send-mail")
    public void sendMail(@RequestBody tbl_Customer customer) {
        testService.test(customer);
    }

    @PostMapping("test_call")
    public ResponseEntity<MessageResponse> testcall(@RequestBody String str) {
      String str1=customer.test(str);
        tbl_ResponseStatusCode responseStatusCode= responseStatusCodeRepo.findByResponseStatusCode(str1);
        MessageResponse response=new MessageResponse(EnumResponseStatusCode.valueOf(responseStatusCode.getResponseStatusCode()),
                responseStatusCode.getResponseStatusMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler()
    public String testHandler(Exception ex){
        System.out.println(ex.getMessage());
        return "internal :"+ex.getMessage();
    }
    @GetMapping("/re")
    public ResponseEntity<?> testResponse(){
        return new ResponseEntity<>(EnumResponseStatusCode.ACCOUNT_EXISTED.label, HttpStatus.OK);
    }

}
