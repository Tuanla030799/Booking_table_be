package com.nuce.duantp.sunshine.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test/123")
public class TestController {
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
