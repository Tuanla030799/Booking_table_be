package com.nuce.duantp.sunshine.service;

import com.nuce.duantp.sunshine.dto.model.TokenLiving;
import com.nuce.duantp.sunshine.repository.TokenLivingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class TokenLivingService {
    @Autowired
    TokenLivingRepo tokenLivingRepo;

    public boolean checkTokenLiving(HttpServletRequest req){
        /*
         * TODO: enable
         * */
        String authTokenHeader = req.getHeader("Authorization");
        String[] splits = authTokenHeader.split(" ");

        TokenLiving tokenLiving= tokenLivingRepo.findByToken(splits[1]);
        if(tokenLiving==null){
            return false;
        }
        return true;
    }
}
