package com.chernokoz;

/**
 * class for basic word token
 */
public class WordToken implements Token {

    private final String word;

    public String getToken() {
        return word;
    }

    public WordToken(String word) {
        this.word = word;
    }
}
