package com.nuce.duantp.sunshine.dto.response;

import com.nuce.duantp.sunshine.config.format.FormatMoney;
import com.nuce.duantp.sunshine.dto.model.tbl_Food;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoodHomeRes {
    private Long foodId;

    private int stt;

    private String foodName;

    private String describes;

    private String foodImage;

    private String foodPrice;

    private int quantity;
    public FoodHomeRes(tbl_Food food,int stt) {
        this.foodId=food.getFoodId();
        this.foodName = food.getFoodName();
        String[] splits = food.getDescribes().split(" ");
        String str = "";
        if(splits.length>20){
            for (int i = 0; i < 20; i++) {
                str = str + splits[i] + " ";
            }
        }
       else {
           str=food.getDescribes();
        }
        this.describes = str;
        this.foodImage = food.getFoodImage();
        this.foodPrice = FormatMoney.formatMoney(String.valueOf(food.getFoodPrice()));
        this.stt=stt;
        this.quantity=0;
    }
    public FoodHomeRes(tbl_Food food,int stt,int quantity) {
        this.foodId=food.getFoodId();
        this.foodName = food.getFoodName();
        String[] splits = food.getDescribes().split(" ");
        String str = "";
        if(splits.length>20){
            for (int i = 0; i < 20; i++) {
                str = str + splits[i] + " ";
            }
        }
       else {
           str=food.getDescribes();
        }
        this.describes = str;
        this.foodImage = food.getFoodImage();
        this.foodPrice = FormatMoney.formatMoney(String.valueOf(food.getFoodPrice()));
        this.stt=stt;
        this.quantity=quantity;
    }
}
