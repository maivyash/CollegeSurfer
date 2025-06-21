package com.example.cpp;

import android.text.InputFilter;
import android.text.Spanned;

public class NumericInputFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // Check if the input is a negative sign at the beginning
        if (dstart == 0 && source.length() > 0 && source.charAt(0) == '-') {
            return "";
        }

        // Loop through each character in the source
        for (int i = start; i < end; i++) {
            // Check if the character is not a digit
            if (!Character.isDigit(source.charAt(i))) {
                // If not a digit, return an empty string to reject the input
                return "";
            }
        }

        // If all characters are digits, accept the input
        return null;
    }
}
