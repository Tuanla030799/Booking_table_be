package com.nuce.duantp.sunshine.dto.request;

import com.nuce.duantp.sunshine.dto.enums.ImageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageDTO {
    @NotNull
    @NotBlank
    @NotEmpty
    private String name;
    @NotNull
    @NotBlank
    @NotEmpty
    private File file;
    private String imagePath;
    private String description;
    @NotNull
    @NotBlank
    @NotEmpty
    private String idParent;
    private ImageType type;
    private String specifyType;
}
