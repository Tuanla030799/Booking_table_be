package com.nuce.duantp.sunshine.config.format;

import java.util.regex.Pattern;

public class CheckPass {
    public static boolean checkFormatPassword(String password) {
        String PASS_PATTERN = "^(?=\\w*[0-9])(?=\\w*[a-z])(?=\\w*[A-Z])\\w{6,20}";
        return Pattern.matches(PASS_PATTERN, password);
    }
}
