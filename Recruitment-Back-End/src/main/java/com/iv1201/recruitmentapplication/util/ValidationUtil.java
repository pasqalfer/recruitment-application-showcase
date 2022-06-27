package com.iv1201.recruitmentapplication.util;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {
    private static final String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+↵\n" +
            ")*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";

    public static boolean isValidEmail(String value) {
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean emptyOrNull(String s) {
        return Objects.isNull(s) || s.isEmpty() || s.isBlank() || s.length() == 0;
    }
}
