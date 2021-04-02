package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.format.CheckPass;
import com.nuce.duantp.format.MyStringRandomGen;
import com.nuce.duantp.sunshine.dto.request.LoginRequest;
import com.nuce.duantp.sunshine.dto.request.SignupRequest;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.repository.CustomerRepo;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import com.nuce.duantp.sunshine.security.jwt.JwtUtils;
import com.nuce.duantp.sunshine.security.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

@RestController
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

    @PostMapping("/login")
    public ResponseEntity<?> authenticateConsumer(@Valid @RequestBody LoginRequest loginRequest) {
//        System.out.printf(loginRequest.getEmail() + "  " + loginRequest.getPassword());
        return authService.login(loginRequest);
    }

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> registerConsumer(@RequestBody SignupRequest signupRequest) {
        return authService.registerConsumer(signupRequest);
    }

//    @PostMapping("/logout")
//    public ResponseEntity<MessageResponse> logoutConsumer() {
//        return ResponseEntity
//                .ok()
//                .body(new MessageResponse(HttpStatus.OK.value(), "Success"));
//    }

    @GetMapping("/forgot-password")
    public ResponseEntity<MessageResponse> ForgotPassword(@RequestParam("email")String email){
        MyStringRandomGen msr = new MyStringRandomGen();
        String password=msr.generateRandomString();
        System.out.println(password);
        while ((!CheckPass.checkPassword(password))){
            password=msr.generateRandomString();
            System.out.println(password);
        }
        System.out.println("\nforgot pass"+email);
        return authService.ForgotPassword(email,password);
    }


}
