package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.config.schedule.AutoCancelBooking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test/123")
public class TestController {
//    @Resource(name = )
@Autowired
AutoCancelBooking autoCancelBooking;
    @GetMapping("tets")
    public void test(){
        autoCancelBooking.run();
    }



//    @ExceptionHandler(ArithmeticException.class)
//    public String testException(){
//        return "exception mej roi";
//    }

}
