package com.example.cpp;

import android.text.InputFilter;
import android.text.Spanned;

public class CharacterInputFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // Loop through each character in the source
        for (int i = start; i < end; i++) {
            // Check if the character is a digit
            if (Character.isDigit(source.charAt(i))) {
                // If it's a digit, return an empty string to reject the input
                return "";
            }
        }
        // If all characters are non-digits, accept the input
        return null;
    }
}
