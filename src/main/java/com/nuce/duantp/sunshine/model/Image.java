package com.nuce.duantp.sunshine.model;

import com.nuce.duantp.sunshine.enums.ImageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Table(name="image")
@Entity
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String url;
    private String imagePath;
    private String description;
    private String idParent;
    private String type;
    private String specifyType;
}
