package com.nuce.duantp.sunshine.controller;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test/123")
public class TestController {
//    @Resource(name = )
    @GetMapping
    public String test(){
        System.out.println(1/0);
        return "fdfdaf";
    }
//    @ExceptionHandler(ArithmeticException.class)
//    public String testException(){
//        return "exception mej roi";
//    }

}
