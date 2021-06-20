package com.nuce.duantp.sunshine.security.services;


import com.nuce.duantp.sunshine.config.format.*;
import com.nuce.duantp.sunshine.controller.SendEmailController;
import com.nuce.duantp.sunshine.dto.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.dto.model.ResetPassword;
import com.nuce.duantp.sunshine.dto.model.TokenLiving;
import com.nuce.duantp.sunshine.dto.model.UserDetailsImpl;
import com.nuce.duantp.sunshine.dto.model.tbl_Customer;
import com.nuce.duantp.sunshine.dto.request.LoginRequest;
import com.nuce.duantp.sunshine.dto.request.SendEmailReq;
import com.nuce.duantp.sunshine.dto.request.SignupRequest;
import com.nuce.duantp.sunshine.dto.response.JwtResponse;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.repository.CustomerRepo;
import com.nuce.duantp.sunshine.repository.ResetPasswordRepo;
import com.nuce.duantp.sunshine.repository.TokenLivingRepo;
import com.nuce.duantp.sunshine.security.jwt.JwtUtils;
import com.nuce.duantp.sunshine.service.ImageService;
import com.phamtan.base.email.service.EmailService;
import freemarker.template.Configuration;
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
    private final Configuration configuration;
    private final TokenLivingRepo tokenLivingRepo;
    private final ImageService imageService;
    private final ResetPasswordRepo resetPasswordRepo;
    private final EmailService emailService;
    private final SendEmailController sendEmailController;

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
            ResetPassword resetPassword=new ResetPassword(customer.getEmail());
            resetPasswordRepo.save(resetPassword);
            return ResponseEntity.ok().body(new MessageResponse(EnumResponseStatusCode.SUCCESS));
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageResponse message = new MessageResponse(EnumResponseStatusCode.BAD_REQUEST);
            return ResponseEntity.badRequest().body(message);
        }
    }

    public ResponseEntity<MessageResponse> ForgotPassword(String email) {
        try {
            tbl_Customer customer = userRepository.findCustomerByEmail(email);
            ResetPassword resetPassword=resetPasswordRepo.findByEmail(customer.getEmail());
            if(resetPassword.getCount()==0){
                MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.BACK_TO_FUTURE);
                return new ResponseEntity<>(messageResponse, HttpStatus.TOO_MANY_REQUESTS);
            }
            if(resetPassword.isStatus()==false){
                MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOO_MANY_REQUESTS);
                return new ResponseEntity<>(messageResponse, HttpStatus.TOO_MANY_REQUESTS);
            }
            MyStringRandomGen msr = new MyStringRandomGen();
            String password = msr.generateRandomString();
            while ((!CheckPass.checkFormatPassword(password))) {
                password = msr.generateRandomString();
                System.out.println(password);
            }
            customer.setPassword(passwordEncoder.encode(password));
            userRepository.save(customer);
            SendEmailReq sendEmailReq=new SendEmailReq(email,email,"Lấy lại mật khẩu",password,customer.getFullName()
                    ,"forgotPass.ftl");
            sendEmailController.sendEmail(sendEmailReq);
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
