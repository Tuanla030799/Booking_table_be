package com.nuce.duantp.sunshine.security.services;


import com.nuce.duantp.sunshine.config.format.*;
import com.nuce.duantp.sunshine.dto.request.LoginRequest;
import com.nuce.duantp.sunshine.dto.request.SignupRequest;
import com.nuce.duantp.sunshine.dto.response.JwtResponse;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.dto.enums.EnumResponseStatusCode;

import com.nuce.duantp.sunshine.dto.model.TokenLiving;
import com.nuce.duantp.sunshine.dto.model.UserDetailsImpl;
import com.nuce.duantp.sunshine.dto.model.tbl_Customer;
import com.nuce.duantp.sunshine.repository.CustomerRepo;
import com.nuce.duantp.sunshine.repository.TokenLivingRepo;
import com.nuce.duantp.sunshine.security.jwt.JwtUtils;
//import com.phamtan.base.email.data_structure.EmailContentData;
//import com.phamtan.base.email.request.EmailRequest;
//import com.phamtan.base.email.service.EmailService;
//import com.phamtan.base.enumeration.EmailEnum;
//import com.phamtan.base.email.data_structure.EmailContentData;
//import com.phamtan.base.email.request.EmailRequest;
//import com.phamtan.base.email.service.EmailService;
//import com.phamtan.base.enumeration.EmailEnum;
import com.nuce.duantp.sunshine.service.ImageService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AuthService {
    private final CustomerRepo userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final  AuthenticationManager authenticationManager;
    private final CustomerRepo customerRepo;
//    private final  AuthTokenFilter authTokenFilter;
//    private final Configuration configuration;
    private final TokenLivingRepo tokenLivingRepo;
    private final ImageService imageService;

    public ResponseEntity<MessageResponse> registerConsumer(@RequestBody SignupRequest signupRequest) {
        if (!CheckEmail.checkFormatEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse(EnumResponseStatusCode.INVALID_EMAIL_FORMAT));
        }

        if (!CheckPass.checkFormatPassword(signupRequest.getPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse(EnumResponseStatusCode.INVALID_PASSWORD_FORMAT));
        }

        if (!CheckPhoneNumber.checkFormatPhone(signupRequest.getPhoneNumber())) {
            return ResponseEntity.badRequest().body(new MessageResponse(EnumResponseStatusCode.INVALID_PHONE_FORMAT));
        }

        if (!CheckNameCustomer.checkFormatName(signupRequest.getFullName())) {
            return ResponseEntity.badRequest().body(new MessageResponse(EnumResponseStatusCode.INVALID_NAME_FORMAT));
        }

        try {
            if (userRepository.existsByEmail(signupRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new MessageResponse(EnumResponseStatusCode.EMAIL_EXISTED));
            }
            if (userRepository.existsByPhoneNumber(signupRequest.getPhoneNumber())) {
                return ResponseEntity.badRequest().body(new MessageResponse(EnumResponseStatusCode.PHONE_NUMBER_EXISTED));
            }
            tbl_Customer customer=new tbl_Customer(signupRequest,passwordEncoder.encode(signupRequest.getPassword()));
//            Image image = new Image();
//            image.setName(customer.getImage());
//            image.setDescription(customer.getFullName());
//            image.setImagePath("/Avatar/" + customer.getImage() + ".jpg");
//            image.setType("AVATAR");
//            image.setSpecifyType("specifyType");
//            image.setIdParent("idParent");
//            imageService.createImage(image, signupRequest.getFile());
//            customer.setImage(image.getUrl());
            userRepository.save(customer);

            return ResponseEntity.ok().body(new MessageResponse(EnumResponseStatusCode.SUCCESS));
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageResponse message = new MessageResponse(EnumResponseStatusCode.BAD_REQUEST);
            return ResponseEntity.badRequest().body(message);
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
//
//            EmailContentData nameContentData = EmailContentData.builder().key(EmailEnum.TEXT).name("name").data("Mat Khau").build();
//            EmailContentData valueContentData = EmailContentData.builder().key(EmailEnum.TEXT).name("value").data(password).build();
//
//            contents.add(nameContentData);
//            contents.add(valueContentData);
//
//
//            emailRequest.setTo(email);
//            emailRequest.setFrom(email);
//            emailRequest.setSubject("Hello ");
//            emailRequest.setContent(contents);
//            emailService.sendMailWithAttachments(emailRequest, template);
            return ResponseEntity.ok().body(new MessageResponse(EnumResponseStatusCode.SUCCESS));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new MessageResponse(EnumResponseStatusCode.BAD_REQUEST));
        }

    }

    public ResponseEntity<?> login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            Optional<tbl_Customer> customer = customerRepo.findByEmail(loginRequest.getEmail());
            if(customer.get().getAccStatus()==0){
                return new ResponseEntity<>(new MessageResponse(EnumResponseStatusCode.LOCK_ACC), HttpStatus.BAD_REQUEST);
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            TokenLiving tokenLiving = tokenLivingRepo.findByEmail(loginRequest.getEmail());
            if (tokenLiving == null) {
                TokenLiving tokenLiving1 = new TokenLiving(loginRequest.getEmail(), jwt);
                tokenLivingRepo.save(tokenLiving1);
            } else {
                tokenLiving.setToken(jwt);
                tokenLivingRepo.save(tokenLiving);
            }

            String role = customer.get().getRole().toString();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return ResponseEntity.ok(new JwtResponse(jwt, "Bearer", userDetails.getEmail(), role));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new MessageResponse(EnumResponseStatusCode.EMAIL_PASS_NOT_CORRECT), HttpStatus.UNAUTHORIZED);
        }
    }

    public void logoutConsumer(HttpServletRequest req) {

    }
}
