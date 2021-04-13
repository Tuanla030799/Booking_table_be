package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.dto.response.HistoryPointRes;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RequestMapping("/api/customer")
public class CustomerController {

//    @GetMapping("/history-used-point")
//    public HistoryPointRes viewPointUsed(HttpServletRequest req){
//
//    }
}
