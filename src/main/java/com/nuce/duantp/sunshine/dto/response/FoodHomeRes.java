package com.nuce.duantp.sunshine.dto.response;

import com.nuce.duantp.sunshine.model.tbl_Food;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoodHomeRes {
    private String foodName;

    private String describes;

    private String foodImage;

    private Long foodPrice;

    public FoodHomeRes(tbl_Food food, String url) {
        this.foodName = food.getFoodName();
        String[] splits = food.getDescribes().split(" ");
        String str = "";
        if(splits.length>30){
            for (int i = 0; i < 30; i++) {
                str = str + splits[i] + " ";
            }
        }
       else {
           str=food.getDescribes();
        }
        this.describes = str;
        this.foodImage = url;
        this.foodPrice = food.getFoodPrice();
    }
}
