package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.dto.request.LoginRequest;
import com.nuce.duantp.sunshine.dto.request.SignupRequest;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.dto.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.dto.model.TokenLiving;
import com.nuce.duantp.sunshine.repository.CustomerRepo;
import com.nuce.duantp.sunshine.repository.TokenLivingRepo;
import com.nuce.duantp.sunshine.scoped.User;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import com.nuce.duantp.sunshine.security.jwt.JwtUtils;
import com.nuce.duantp.sunshine.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthTokenFilter authTokenFilter;
    private final PasswordEncoder encoder;
    private final  JwtUtils jwtUtils;
    private final  PasswordEncoder passwordEncoder;
    private final CustomerRepo customerRepo;
    @Resource(name = "userBean")
    User user;
    private final TokenLivingRepo tokenLivingRepo;

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
