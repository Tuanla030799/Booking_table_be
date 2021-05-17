package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.dto.response.NewsRes;
import com.nuce.duantp.sunshine.dto.response.PageHomeRes;
import com.nuce.duantp.sunshine.service.SunShineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sunshine")
public class SunShineController {
    @Autowired
    private SunShineService sunShineService;

//    @GetMapping("/get-all-news")
//    public List<NewsRes> getAllNews(){
//        return  sunShineService.getAllNews();
//    }
    @GetMapping("/home")
    public PageHomeRes pageHome(){
        return sunShineService.pageHome();
    }
}
