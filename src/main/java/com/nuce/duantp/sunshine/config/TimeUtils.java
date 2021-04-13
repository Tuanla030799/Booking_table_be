package com.nuce.duantp.sunshine.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
    public static ZonedDateTime convertTime(Long time) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(time),
                ZoneId.of("Asia/Ho_Chi_Minh"));
        return zdt;
    }

    public static LocalDateTime convertZonedToLocal(ZonedDateTime time) {
        ZoneId vnZone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime swissZoned = time.withZoneSameInstant(vnZone);
        LocalDateTime vnLocal = swissZoned.toLocalDateTime();
        return vnLocal;
    }

    public static ZonedDateTime convertLocalToZoned(LocalDateTime time) {
        ZonedDateTime zdt = time.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        return zdt;
    }

    public static LocalDateTime convertLongToLocal(Long time) {
        LocalDateTime zdt = LocalDateTime.ofInstant(Instant.ofEpochMilli(time),
                ZoneId.of("Asia/Ho_Chi_Minh"));
        return zdt;
    }
    public static Date minusDate(Date date,int i,String type){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(type.equals("HOUR")){
            cal.add(Calendar.HOUR, i);
        }
        if(type.equals("MINUTE")){
            cal.add(Calendar.MINUTE, i);
        }
        Date oneHourBack = cal.getTime();
        return oneHourBack;
    }
    public static Date convertStringToDate(String str) throws ParseException {
        Date date=new SimpleDateFormat("dd-M-yyyy hh:mm:ss").parse(str);
        return date;
    }
}
