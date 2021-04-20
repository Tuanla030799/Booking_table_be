package com.nuce.duantp.sunshine.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuce.duantp.sunshine.dto.request.AddFoodReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_Food")
@EntityListeners(AuditingEntityListener.class)
public class tbl_Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "foodId")
    private Long foodId;

    @Column(name = "foodName")
    private String foodName;

    @Column(name = "describes")
    private String describes;

    @Column(name = "foodImage")
    private String foodImage;

    @Column(name = "foodPrice")
    private Long foodPrice;

    @Column(name = "created")
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date created;

    @Column(name = "foodStatus")
    private int foodStatus =1;

    public tbl_Food(AddFoodReq addFoodReq) {
        this.describes =addFoodReq.getDescribe();
        this.foodName=addFoodReq.getFoodName();
        this.foodPrice=addFoodReq.getFoodPrice();
        this.foodStatus=1;
    }
}
