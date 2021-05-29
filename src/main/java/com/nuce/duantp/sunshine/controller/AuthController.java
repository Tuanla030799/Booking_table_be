package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.dto.request.LoginRequest;
import com.nuce.duantp.sunshine.dto.request.SignupRequest;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.model.TokenLiving;
import com.nuce.duantp.sunshine.repository.CustomerRepo;
import com.nuce.duantp.sunshine.repository.TokenLivingRepo;
import com.nuce.duantp.sunshine.scoped.User;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import com.nuce.duantp.sunshine.security.jwt.JwtUtils;
import com.nuce.duantp.sunshine.security.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    AuthService authService;

    @Autowired
    AuthTokenFilter authTokenFilter;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CustomerRepo customerRepo;

    @Resource(name = "userBean")
    User user;

    @Autowired
    TokenLivingRepo tokenLivingRepo;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateConsumer(@Valid @RequestBody LoginRequest loginRequest) {
        user.setEmail(loginRequest.getEmail()); //lấy ra máy đang sử dụng user
        return authService.login(loginRequest);
    }

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> registerConsumer(@ModelAttribute SignupRequest signupRequest) {
        return authService.registerConsumer(signupRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logoutConsumer(HttpServletRequest req) {
        String authTokenHeader = req.getHeader("Authorization");
        String[] splits = authTokenHeader.split(" ");
        TokenLiving tokenLiving= tokenLivingRepo.findByToken(splits[1]);
        tokenLivingRepo.delete(tokenLiving);
        MessageResponse messageResponse=new MessageResponse(EnumResponseStatusCode.SUCCESS);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }




}
