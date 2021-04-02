package com.nuce.duantp.sunshine.security.services;


import com.nuce.duantp.format.CheckEmail;
import com.nuce.duantp.format.CheckNameCustomer;
import com.nuce.duantp.format.CheckPass;
import com.nuce.duantp.format.FormatMoney;
import com.nuce.duantp.sunshine.dto.request.LoginRequest;
import com.nuce.duantp.sunshine.dto.request.SignupRequest;
import com.nuce.duantp.sunshine.dto.response.JwtResponse;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.model.UserDetailsImpl;
import com.nuce.duantp.sunshine.model.tbl_Customer;
import com.nuce.duantp.sunshine.repository.CustomerRepo;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import com.nuce.duantp.sunshine.security.jwt.JwtUtils;
//import com.phamtan.base.email.data_structure.EmailContentData;
//import com.phamtan.base.email.request.EmailRequest;
//import com.phamtan.base.email.service.EmailService;
//import com.phamtan.base.enumeration.EmailEnum;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service

public class AuthService {

    @Autowired
    private CustomerRepo userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    AuthTokenFilter authTokenFilter;

    @Autowired
    private Configuration configuration;

//    @Autowired
//    private EmailService emailService;

    public ResponseEntity<MessageResponse> registerConsumer(@RequestBody SignupRequest signupRequest) {
        if (!CheckEmail.checkEmail(signupRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(EnumResponseStatusCode.INVALID_EMAIL_FORMAT));
        }

        if (!CheckPass.checkPassword(signupRequest.getPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(EnumResponseStatusCode.INVALID_PASSWORD_FORMAT));
        }

        if (!CheckNameCustomer.checkName(signupRequest.getFullName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(EnumResponseStatusCode.INVALID_NAME_FORMAT));
        }

        try {
            if (userRepository.existsByEmail(signupRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(EnumResponseStatusCode.EMAIL_EXISTED));
            }

            tbl_Customer customer = new tbl_Customer(signupRequest.getEmail(), passwordEncoder.encode(signupRequest.getPassword()), signupRequest.getFullName(), signupRequest.getPhoneNumber());
            userRepository.save(customer);

            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(EnumResponseStatusCode.SUCCESS));
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageResponse message = new MessageResponse(EnumResponseStatusCode.BAD_REQUEST);
            return ResponseEntity
                    .badRequest()
                    .body(message);
        }
    }

    public ResponseEntity<MessageResponse> ForgotPassword(String email, String password) {
        try {
            tbl_Customer customer = userRepository.findCustomerByEmail(email);
            customer.setPassword(passwordEncoder.encode(password));
            userRepository.save(customer);
//            Template template = configuration.getTemplate("email.ftl");
//            EmailRequest emailRequest = new EmailRequest();
//            List<EmailContentData> contents = new ArrayList<>();
//            EmailContentData nameContentData = EmailContentData.builder()
//                    .key(EmailEnum.TEXT)
//                    .name("name")
//                    .data(email)
//                    .build();
//            EmailContentData valueContentData = EmailContentData.builder()
//                    .key(EmailEnum.TEXT)
//                    .name("value")
//                    .data(password)
//                    .build();
//            contents.add(nameContentData);
//            contents.add(valueContentData);
//            emailRequest.setTo(email);
//            emailRequest.setFrom(email);
//            emailRequest.setSubject("Hello ");
//            emailRequest.setContent(contents);
//            emailService.sendMailWithAttachments(emailRequest, template);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(EnumResponseStatusCode.SUCCESS));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(EnumResponseStatusCode.BAD_REQUEST));
        }

    }

    public ResponseEntity<?> login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            Optional<tbl_Customer> customer=customerRepo.findByEmail(loginRequest.getEmail());
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return ResponseEntity.ok(new JwtResponse(jwt, "Bearer", userDetails.getEmail()));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new MessageResponse(EnumResponseStatusCode.EMAIL_PASS_NOT_CORRECT), HttpStatus.UNAUTHORIZED);
        }
    }

    public void logoutConsumer(HttpServletRequest req) {

    }
}
