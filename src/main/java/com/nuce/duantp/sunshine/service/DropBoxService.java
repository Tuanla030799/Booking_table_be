package com.nuce.duantp.sunshine.service;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DeleteResult;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Service
public class DropBoxService {
    private final String accessToken = System.getenv("DB_ACCESS_TOKEN");

    DbxClientV2 client ;
    @PostConstruct
    public void init(){
        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
         client = new DbxClientV2(config, accessToken);
    }

    /**
     *
     * @param file
     * @param path : must start with / character
     * @return
     * @throws DbxException
     */
    public SharedLinkMetadata uploadFile(MultipartFile file, String path) throws DbxException {
        try {
            InputStream in = file.getInputStream();
            FileMetadata metadata = client.files().uploadBuilder(path)
                    .uploadAndFinish(in);
            SharedLinkMetadata response = client.sharing().createSharedLinkWithSettings(metadata.getPathLower());
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public DeleteResult deleteImage(String path) throws DbxException {
       return  client.files().deleteV2(path);
    }

}