package com.nuce.duantp.sunshine.config.schedule;

import com.nuce.duantp.sunshine.service.SunShineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoUpdateBeneficiary implements Runnable {
    @Autowired
    SunShineService sunShineService;

    @Override
    public void run() {
       sunShineService.updateBeneficiary();
    }
}
