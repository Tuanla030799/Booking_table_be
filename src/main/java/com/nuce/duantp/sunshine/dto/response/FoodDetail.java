package com.nuce.duantp.sunshine.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuce.duantp.sunshine.config.format.FormatMoney;
import com.nuce.duantp.sunshine.dto.model.tbl_Food;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodDetail {
    private Long foodId;

    private String foodName;

    private String describes;

    private String foodImage;

    private String foodPrice;

    public FoodDetail(tbl_Food food) {
        this.foodId=food.getFoodId();
        this.foodPrice= FormatMoney.formatMoney(String.valueOf(food.getFoodPrice()));
        this.foodImage=food.getFoodImage();
        this.describes=food.getDescribes();
        this.foodName=food.getFoodName();
    }
}
