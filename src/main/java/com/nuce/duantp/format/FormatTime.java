package com.nuce.duantp.format;

import java.util.Calendar;
import java.util.Date;

public class FormatTime {
    public static Date addHoursToJavaUtilDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 7);
        return calendar.getTime();
    }

}
