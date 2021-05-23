package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.dto.response.FoodHomeRes;
import com.nuce.duantp.sunshine.dto.response.NewsRes;
import com.nuce.duantp.sunshine.dto.response.PageHomeRes;
import com.nuce.duantp.sunshine.service.SunShineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sunshine")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
public class SunShineController {
    @Autowired
    private SunShineService sunShineService;

//    @GetMapping("/get-all-News")
//    public List<NewsRes> getAllNews(){
//        return  sunShineService.getAllNews();
//    }
    @GetMapping("/home")
    public PageHomeRes pageHome(){
        return sunShineService.pageHome();
    }

    @GetMapping("/get-list-food")
    public List<FoodHomeRes> getListFood(){
        return sunShineService.getListFood();
    }


}
