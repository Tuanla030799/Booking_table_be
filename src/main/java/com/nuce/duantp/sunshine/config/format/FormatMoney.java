package com.nuce.duantp.sunshine.config.format;

import java.text.DecimalFormat;

public class FormatMoney {
    public  static String formatMoney(String m){
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(Double.parseDouble(m))+" VNĐ";
    }
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 4);
    }
}
