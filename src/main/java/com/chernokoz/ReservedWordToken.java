package com.chernokoz;

/**
 * class for reserved word token
 */
public class ReservedWordToken implements Token {

    private final String reservedWord;

    public String getToken() {
        return reservedWord;
    }

    public ReservedWordToken(String word) {
        reservedWord = word;
    }
}
