package com.nuce.duantp.sunshine.config.format;

import java.util.regex.Pattern;

public class Validate {
    public static String convertStatusBooking(int status) {
        String response="";
       switch (status){
           case 1:
               response="Đã thanh toán";
               break;
           case 0:
               response ="Chưa thánh toán";
               break;
           case 2:
               response="Đã hủy";
               break;
           case 3:
               response="Chờ admin xác nhận";
               break;
           default:
               response ="";
               break;
       }

       return response;
    }
}
