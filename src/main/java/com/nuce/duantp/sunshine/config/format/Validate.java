package com.nuce.duantp.sunshine.config.format;

import java.util.regex.Pattern;

public class Validate {
    public static String convertStatusBooking(int status) {
        String response="";
       switch (status){
           case 0:
               response ="Chưa thánh toán";
               break;
           case 1:
               response="Chờ admin xác nhận";
               break;
           case 2:
               response="Đã thanh toán";
               break;
           case 3:
               response="Đã hủy";
               break;
           case 4:
               response="Tự động huỷ";
               break;
           default:
               response ="";
               break;
       }

       return response;
    }
}
