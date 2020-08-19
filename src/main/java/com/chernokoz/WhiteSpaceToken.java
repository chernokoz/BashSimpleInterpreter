package com.chernokoz;

/**
 * class for white space token
 */
public class WhiteSpaceToken implements Token {

    int size;

    public WhiteSpaceToken(int size) {
        this.size = size;
    }
    public WhiteSpaceToken(String str) {
        this.size = str.length();
    }

    @Override
    public String getToken() {
        return " ".repeat(size);
    }

    public static boolean checkForWhiteSpace(String str) {
        return str.length() > 0 && str.equals(" ".repeat(str.length()));
    }
}
