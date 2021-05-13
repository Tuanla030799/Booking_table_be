package com.nuce.duantp.sunshine.config.format;

import java.util.regex.Pattern;

public class CheckEmail {
    public static boolean checkEmail(String email) {
        String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[a-z]{1,}(.+)$";
        return Pattern.matches(EMAIL_PATTERN, email);
    }
}
