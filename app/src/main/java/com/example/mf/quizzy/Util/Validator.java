package com.example.mf.quizzy.util;

import android.util.Patterns;

import java.util.regex.Pattern;

public class Validator {
    Pattern mPasswordPattern;

    public static Builder getBuilderForPasswordLengthBetween(int minLength, int maxLength) {
        return new Builder(minLength, maxLength);
    }

    public static class Builder {
        private StringBuilder mRegexBuilder;
        private int mMinLength, mMaxLength;
        private Builder(int minLength, int maxLength) {
            mMaxLength = maxLength;
            mMinLength = minLength;
            mRegexBuilder = new StringBuilder("((?=.*[a-z])");
        }

        public Builder requireSpecialChar(){
            mRegexBuilder.append("(?=.*[@#$%!])");
            return this;
        }

        public Builder requireCapitalLetter(){
            mRegexBuilder.append("(?=.*[A-Z])");
            return this;
        }

        public Builder requireNumber(){
            mRegexBuilder.append("(?=.*d)");
            return this;
        }

        public Validator build(){
            mRegexBuilder.append(".{" + mMinLength + "," + mMaxLength + "})");
            return new Validator(Pattern.compile(mRegexBuilder.toString()));
        }
    }

    private Validator(Pattern passwordPattern) {
        this.mPasswordPattern = passwordPattern;
    }


    public boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public  boolean isPasswordValid(String password) {
        return mPasswordPattern.matcher(password).matches();
    }

}
