package com.chernokoz;

public class WordToken implements Token {

    private String word;

    public String getToken() {
        return word;
    }

    public WordToken(String word) {
        this.word = word;
    }
}
