package com.nuce.duantp.sunshine.service;

import com.nuce.duantp.format.CheckNameCustomer;
import com.nuce.duantp.format.CheckPass;
import com.nuce.duantp.format.CheckPhoneNumber;
import com.nuce.duantp.sunshine.dto.request.ChangePasswordReq;
import com.nuce.duantp.sunshine.dto.request.UpdateUserReq;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.model.tbl_Customer;
import com.nuce.duantp.sunshine.repository.CustomerRepo;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    AuthTokenFilter authTokenFilter;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> changePassword(ChangePasswordReq changePasswordReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (passwordEncoder.matches(changePasswordReq.getOldPass(), customer.get().getPassword())) {
            if (!CheckPass.checkPassword(changePasswordReq.getNewPass())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(EnumResponseStatusCode.INVALID_PASSWORD_FORMAT));
            } else {
                customer.get().setPassword(passwordEncoder.encode(changePasswordReq.getNewPass()));
                customerRepo.save(customer.get());
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(EnumResponseStatusCode.SUCCESS));
            }
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(EnumResponseStatusCode.OLD_PASS_NOT_CORRECT));
        }
    }

    public ResponseEntity<?> updateUser(UpdateUserReq updateUserReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (!CheckPhoneNumber.checkPhone(updateUserReq.getPhoneNumber())) {
            return ResponseEntity.badRequest().body(new MessageResponse(EnumResponseStatusCode.INVALID_PHONE_FORMAT));
        }
        if (!CheckNameCustomer.checkName(updateUserReq.getFullName())) {
            return ResponseEntity.badRequest().body(new MessageResponse(EnumResponseStatusCode.INVALID_NAME_FORMAT));
        } else {
            tbl_Customer customer1 = new tbl_Customer(updateUserReq);
            customerRepo.save(customer1);
            return ResponseEntity.ok().body(new MessageResponse(EnumResponseStatusCode.SUCCESS));
        }
    }



}
