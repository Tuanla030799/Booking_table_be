package com.nuce.duantp.sunshine.dto.response;

import com.nuce.duantp.sunshine.config.format.FormatMoney;
import com.nuce.duantp.sunshine.dto.model.tbl_Food;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchFoodRespon {
    private String foodName;

    private String foodImage;

    private String foodPrice;

    public SearchFoodRespon(tbl_Food food) {
        this.foodImage=food.getFoodImage();
        this.foodName=food.getFoodName();
        this.foodPrice= FormatMoney.formatMoney(String.valueOf(food.getFoodPrice()));
    }
}
