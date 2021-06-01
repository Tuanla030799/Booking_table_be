package com.nuce.duantp.sunshine.service;

import com.nuce.duantp.sunshine.config.format.CheckNameCustomer;
import com.nuce.duantp.sunshine.config.format.CheckPass;
import com.nuce.duantp.sunshine.config.format.CheckPhoneNumber;
import com.nuce.duantp.sunshine.dto.model.Image;
import com.nuce.duantp.sunshine.dto.model.TokenLiving;
import com.nuce.duantp.sunshine.dto.request.ChangePasswordReq;
import com.nuce.duantp.sunshine.dto.request.LoginRequest;
import com.nuce.duantp.sunshine.dto.request.UpdateUserReq;
import com.nuce.duantp.sunshine.dto.response.JwtResponse;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.dto.response.UserDetail;
import com.nuce.duantp.sunshine.dto.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.dto.model.tbl_Customer;
import com.nuce.duantp.sunshine.repository.CustomerRepo;
import com.nuce.duantp.sunshine.repository.TokenLivingRepo;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import com.nuce.duantp.sunshine.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthTokenFilter authTokenFilter;
    private final  CustomerRepo customerRepo;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final AuthService authService;
    private final TokenLivingRepo tokenLivingRepo;
    private Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public ResponseEntity<?> changePassword(ChangePasswordReq changePasswordReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        try{
          if (passwordEncoder.matches(changePasswordReq.getOldPass(), customer.get().getPassword())) {
            if (!CheckPass.checkPassword(changePasswordReq.getNewPass())) {
              return ResponseEntity
                .ok()
                .body(new MessageResponse(EnumResponseStatusCode.INVALID_PASSWORD_FORMAT));
            } else {
                /*
                * TODO:
                *  -sau khi đổi mật khấu sẽ gọi hàm login để lưu token mới vào bảo tokenLive
                * - trả cho FE success và token mới
                * */
              customer.get().setPassword(passwordEncoder.encode(changePasswordReq.getNewPass()));
                LoginRequest loginRequest=new LoginRequest(customer.get().getEmail(), changePasswordReq.getNewPass());
                ResponseEntity<JwtResponse> data = (ResponseEntity<JwtResponse>) authService.login(loginRequest);
              customerRepo.save(customer.get());
                TokenLiving tokenLiving = tokenLivingRepo.findByEmail(loginRequest.getEmail());
                tokenLiving.setToken(data.getBody().getToken());
                tokenLivingRepo.save(tokenLiving);
              LOGGER.warn("Change password success by " + customer.get().getEmail(), UserService.class);
              return ResponseEntity
                .ok()
                .body(new MessageResponse(EnumResponseStatusCode.SUCCESS,data.getBody().getToken()));
            }
          } else {
            return ResponseEntity
              .ok()
              .body(new MessageResponse(EnumResponseStatusCode.OLD_PASS_NOT_CORRECT));
          }
        }catch (Exception e){
          return ResponseEntity
            .badRequest()
            .body(new MessageResponse(EnumResponseStatusCode.BAD_REQUEST));
        }

    }

    public ResponseEntity<?> updateUser(UpdateUserReq updateUserReq, HttpServletRequest req) {
        Optional<tbl_Customer> customerOptional = authTokenFilter.whoami(req);
        if (!CheckPhoneNumber.checkPhone(updateUserReq.getPhoneNumber())) {
            return ResponseEntity.badRequest().body(new MessageResponse(EnumResponseStatusCode.INVALID_PHONE_FORMAT));
        }
        if (!CheckNameCustomer.checkName(updateUserReq.getFullName())) {
            return ResponseEntity.badRequest().body(new MessageResponse(EnumResponseStatusCode.INVALID_NAME_FORMAT));
        } else {
            tbl_Customer customer=customerOptional.get();
            customer.updateCustomer(updateUserReq);
            Image image = new Image();
            image.setName(customer.getImage());
            image.setDescription(customer.getFullName());
            image.setImagePath("/Avatar/" + customer.getImage() + ".jpg");
            image.setType("AVATAR");
            image.setSpecifyType("specifyType");
            image.setIdParent("idParent");
            imageService.createImage(image, updateUserReq.getFile());
            customer.setImage(image.getUrl());
            customerRepo.save(customer);
            LOGGER.warn("update info success by " + customer.getEmail()+"\n"+updateUserReq, UserService.class);
            return ResponseEntity.ok().body(new MessageResponse(EnumResponseStatusCode.SUCCESS));
        }
    }

    public UserDetail userDetail(HttpServletRequest req){
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        return new UserDetail(customer.get());
    }


}
