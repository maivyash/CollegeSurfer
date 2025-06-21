package com.example.cpp;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BranchInputFilter implements InputFilter {

    private static final String PATTERN_REGEX = "A-Z";
    private static final Pattern pattern = Pattern.compile(PATTERN_REGEX);

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String input = dest.toString() + source.toString();

        Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            // If the input doesn't contain the pattern, reject it
            return "";
        }

        // If the input contains the pattern, accept it
        return null;
    }
}
