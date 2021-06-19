package com.nuce.duantp.onesignal;

import com.phamtan.base.onesingnal.inout.request.DataDetail;
import com.phamtan.base.onesingnal.inout.request.NotificationRequest;
import com.phamtan.base.onesingnal.service.OneSignalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/test")
public class OnesignalService {
    @Value("${phamtan.onesignal.app-id}")
    private String appId;
    @Autowired
    private OneSignalService oneSignalService;
    @GetMapping("/test")
    public void sendNotify() throws  URISyntaxException {
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setAppId(appId);
        notificationRequest.setChromeWebIcon("https://picsum.photos/200");
        HashMap<String, Map<String, String>> data = new HashMap<>();
        Map<String, String> content1 = new HashMap<>();
        content1.put("en", "do thi dieu linh");
        notificationRequest.setContents(content1);
        DataDetail dataDetail = new DataDetail();
        dataDetail.setKey("foo");
        dataDetail.setValue("bar");
        HashMap<String, DataDetail> data1 = new HashMap<>();
        notificationRequest.setData(data1);
        oneSignalService.createNotify(notificationRequest);
    }
}
