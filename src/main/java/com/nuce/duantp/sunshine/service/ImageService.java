package com.nuce.duantp.sunshine.service;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;

import com.nuce.duantp.sunshine.dto.enums.DropBoxCommon;
import com.nuce.duantp.sunshine.dto.model.Image;
//import com.nuce.duantp.sunshine.repository.ImageRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class ImageService {
    private final DropBoxService dropBoxService;
    public Image createImage(Image image , MultipartFile file)  {
        if(image.getImagePath()==null){
            image.setImagePath("/"+file.getOriginalFilename());
        }
        else if(!image.getImagePath().contains("/")){
            image.setImagePath("/"+image.getImagePath());
        }
        try {
            SharedLinkMetadata response = dropBoxService.uploadFile(file, image.getImagePath());
            if(response.getUrl()!=null){
                image.setUrl(convertUrl(response.getUrl()));
            }

        } catch (DbxException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String convertUrl(String sourceUrl){
        sourceUrl = sourceUrl.substring(0,sourceUrl.length()-5);
        sourceUrl = sourceUrl .concat(DropBoxCommon.SUFFIX_IMAGE);
        return sourceUrl;
    }

}
