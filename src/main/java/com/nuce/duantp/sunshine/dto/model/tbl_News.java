package com.nuce.duantp.sunshine.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "tbl_News")
@EntityListeners(AuditingEntityListener.class)

public class tbl_News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "newsId")
    private Long newsId;

    @Column(name = "newsTitle")
    private String newsTitle;

    @Column(name = "newsDetail")
    private String newsDetail;

    @Column(name = "newsImage")
    private String newsImage;

    @Column(name = "newsStatus")
    private int newsStatus;

    @Column(name = "created")
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date created;

    public tbl_News(String newsTitle,String newsDetail) {
        this.newsDetail =newsDetail;
        this.newsTitle =newsTitle;
        this.newsImage= String.valueOf(new Date().getTime());
        this.newsStatus =1;
    }
}
