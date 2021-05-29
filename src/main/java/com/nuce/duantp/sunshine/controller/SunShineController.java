package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.dto.response.FoodHomeRes;
import com.nuce.duantp.sunshine.dto.response.NewsRes;
import com.nuce.duantp.sunshine.dto.response.PageHomeRes;
import com.nuce.duantp.sunshine.model.tbl_Food;
import com.nuce.duantp.sunshine.repository.FoodRepo;
import com.nuce.duantp.sunshine.service.SunShineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sunshine")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
public class SunShineController {
    @Autowired
    private SunShineService sunShineService;
    @Autowired
    private FoodRepo foodRepo;
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

    @GetMapping("/get-food/{id}")
    public FoodHomeRes getFood(@PathVariable(name = "id" )Long id){
        tbl_Food food=foodRepo.findByFoodId(id);
        FoodHomeRes foodHomeRes=new FoodHomeRes(food,1);
        return foodHomeRes;
    }

}
