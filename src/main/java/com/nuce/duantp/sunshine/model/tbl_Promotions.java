package com.nuce.duantp.sunshine.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_Promotions")
public class tbl_Promotions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotionsId")
    private Long promotionsId;

    @Column(name = "promotionsTitel")
    private String promotionsTitel;

    @Column(name = "promotionsDitail")
    private String promotionsDitail;

    @Column(name = "promotionsImage", length = 1000)
    private byte[] promotionsImage;

    @Column(name = "promotionsStatus")
    private int promotionsStatus;

    @Column(name = "created")
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date created;



}
