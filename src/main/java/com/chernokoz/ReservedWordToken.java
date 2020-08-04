package com.chernokoz;

public class ReservedWordToken implements Token {

    private String reservedWord;

    public ReservedWordToken(String word) {
        reservedWord = word;
    }
}
