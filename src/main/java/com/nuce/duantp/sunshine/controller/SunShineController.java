package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.dto.model.tbl_Sale;
import com.nuce.duantp.sunshine.dto.response.*;
import com.nuce.duantp.sunshine.dto.model.tbl_Food;
import com.nuce.duantp.sunshine.repository.FoodRepo;
import com.nuce.duantp.sunshine.repository.SaleRepo;
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
    @Autowired
    private SaleRepo saleRepo;

    @GetMapping("/home")
    public PageHomeRes pageHome(){
        return sunShineService.pageHome();
    }

    @GetMapping("/get-list-food")
    public List<FoodHomeRes> getListFood(@RequestParam(name = "bookingId" ,defaultValue = " ")String bookingId){
        return sunShineService.getListFood(bookingId);
    }

    @GetMapping("/get-food/{id}")
    public FoodHomeRes getFood(@PathVariable(name = "id" )Long id){
        tbl_Food food=foodRepo.findByFoodId(id);
        FoodHomeRes foodHomeRes=new FoodHomeRes(food,1);
        return foodHomeRes;
    }

    @GetMapping("/get-list-sale")
    public List<SaleHomeRes> getListSale(){
        return sunShineService.getListSale();
    }

    @GetMapping("/search-food/{name}")
    public List<SearchFoodRespon> searchFoodRespons(@PathVariable(name = "name")String name){
        return sunShineService.searchFoodRespons(name);
    }

    @GetMapping("/food-detail/{foodId}")
    public FoodDetail foodDetail(@PathVariable(name = "foodId") Long foodId){
        tbl_Food food=foodRepo.findByFoodId(foodId);
        FoodDetail foodDetail=new FoodDetail(food);
        return  foodDetail;
    }
    @GetMapping("/sale-detail/{saleId}")
    public SaleDetail saleDetail(@PathVariable(name = "saleId") Long saleId){
        tbl_Sale sale=saleRepo.findBySaleId(saleId);
        SaleDetail saleDetail = new SaleDetail(sale);
        return saleDetail;
    }




}
