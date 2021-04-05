package com.nuce.duantp.format;

import java.util.regex.Pattern;

public class CheckPhoneNumber {
    public static boolean checkPhone(String phone) {
        String PHONE_PATTERN = "^\\d{10,11}$";
        return Pattern.matches(PHONE_PATTERN, phone);
    }
}
