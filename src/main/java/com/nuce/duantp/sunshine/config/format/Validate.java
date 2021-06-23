package com.nuce.duantp.sunshine.config.format;

import java.util.regex.Pattern;

public class Validate {
    public static String convertStatusBooking(int status) {
        String response="";
       switch (status){
           case 1:
               response ="Chưa thánh toán";
               break;
           case 0:
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

    public static String convertStatusAcc(int status) {
        String response="";
        switch (status){
            case 0:
                response ="Đã bị khóa";
                break;
            case 1:
                response="Đang hoạt động";
                break;
            default:
                response ="";
                break;
        }
        return response;
    }
    public static String convertStatusSale(int status) {
        String response="";
        switch (status){
            case 0:
                response ="Hết khuyến mãi";
                break;
            case 1:
                response="Còn khuyến mãi";
                break;
            default:
                response ="";
                break;
        }
        return response;
    }

    public static String convertStatusCharging(int status) {
        String response="";
        switch (status){
            case 0:
                response ="Y/c xác nhận";
                break;
            case 1:
                response="Thành công";
                break;
            case 2:
                response="Thất bại";
                break;
            default:
                response ="";
                break;
        }
        return response;
    }
}
