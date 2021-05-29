package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.config.format.CheckPass;
import com.nuce.duantp.sunshine.config.format.MyStringRandomGen;
import com.nuce.duantp.sunshine.dto.request.ChangePasswordReq;
import com.nuce.duantp.sunshine.dto.request.UpdateUserReq;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.dto.response.UserDetail;
import com.nuce.duantp.sunshine.dto.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.security.services.AuthService;
import com.nuce.duantp.sunshine.service.TokenLivingService;
import com.nuce.duantp.sunshine.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final TokenLivingService tokenLivingService;

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordReq changePasswordReq, HttpServletRequest req) {
        if (tokenLivingService.checkTokenLiving(req)) {
            return userService.changePassword(changePasswordReq, req);
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);

    }

    @GetMapping("/forgot-password/{email}")
    public ResponseEntity<MessageResponse> forgotPassword(@PathVariable String email) {
        MyStringRandomGen msr = new MyStringRandomGen();
        String password = msr.generateRandomString();
        System.out.println(password);
        while ((!CheckPass.checkPassword(password))) {
            password = msr.generateRandomString();
            System.out.println(password);
        }
        return authService.ForgotPassword(email, password);
    }

    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserReq updateUserReq, HttpServletRequest req) {
        if (tokenLivingService.checkTokenLiving(req)) {
            return userService.updateUser(updateUserReq, req);
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get-user-detail")
    public UserDetail userDetail(HttpServletRequest req){
        if (tokenLivingService.checkTokenLiving(req)) {
            return userService.userDetail(req);
        }
        else return null;
    }
}
