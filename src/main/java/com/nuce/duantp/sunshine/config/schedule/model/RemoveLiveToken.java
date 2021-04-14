package com.nuce.duantp.sunshine.config.schedule.model;

import com.nuce.duantp.sunshine.config.TimeUtils;
import com.nuce.duantp.sunshine.model.TokenLiving;
import com.nuce.duantp.sunshine.repository.TokenLivingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class RemoveLiveToken implements Runnable {
    @Autowired
    TokenLivingRepo tokenLivingRepo;
    @Value("${Vimo.app.jwtExpirationMs}")
    private Long time;

    @Override
    public void run() {
        int minute = (int) (time / (1000 * 60 ));
        Iterable<TokenLiving> tokenLivings = tokenLivingRepo.findAll();
        for (TokenLiving tokenLiving : tokenLivings) {
            Date date = new Date();
            Date date1 = TimeUtils.minusDate(tokenLiving.getUpdated(), minute, "MINUTE");
            if(date.compareTo(date1)>0){
                tokenLivingRepo.delete(tokenLiving);
            }
        }
    }
}
