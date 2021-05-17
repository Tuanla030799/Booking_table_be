//package com.nuce.duantp.sunshine.JasperReports;
//
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//import freemarker.template.TemplateException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class MailService {
//    @Autowired
//    private JavaMailSender mailSender;
//
//    @Autowired
//    private Configuration configuration;
//
//    public EmailRequestDto creatEmail(String email,String subject,String name,String url){
//
//        EmailRequestDto emailRequestDto=new EmailRequestDto("sunshine87lethanhnghi@gmail.com",email,subject,name,url);
//        Map<String, String> model = new HashMap<>();
//        model.put("name", emailRequestDto.getName());
//        model.put("value", "Welcome to ASB Notebook!!");
//        String response = sendMail(emailRequestDto, model);
//        return emailRequestDto;
//    }
//    public String sendMail(EmailRequestDto request, Map<String, String> model) {
//
//        String response;
//        MimeMessage message = mailSender.createMimeMessage();
//        try {
//            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
//                    StandardCharsets.UTF_8.name());
//
//            ClassPathResource pdf = new ClassPathResource("static/"+request.getSubject()+".pdf");
//
//            Template template = configuration.getTemplate("email.ftl");
//            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
//            helper.setTo(request.getTo());
//            helper.setFrom(request.getFrom());
//            helper.setSubject(request.getSubject());
//            helper.setText(html, true);
//
//            helper.addAttachment(request.getSubject()+".pdf", pdf);
//
//            mailSender.send(message);
//            response = "Email has been sent to :" + request.getTo();
//        } catch (MessagingException | IOException | TemplateException e) {
//            response = "Email send failure to :" + request.getTo();
//        }
//        return response;
//    }
//}
