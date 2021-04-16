package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.service.SunShineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sunshine")
public class SunShineController {
    @Autowired
    private SunShineService sunShineService;

//    @GetMapping("/")
}
