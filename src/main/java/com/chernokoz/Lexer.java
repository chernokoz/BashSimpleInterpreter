package com.chernokoz;

import java.util.ArrayList;

public class Lexer {
    private String string;

    public Lexer(String text) {
        this.string = text;
    }

    public Lexer() {

    }

    public ArrayList<Token> run() {
        ArrayList<Token> tokenList = new ArrayList<>();
        String currentWord = "";
        for (char symbol : string.toCharArray()) {
            if (Token.specialSymbols.contains(symbol)) {
                if (currentWord.length() > 0) {
                    tokenList.add(new ReservedWordToken(currentWord));
                }
                tokenList.add(new SpecialSymbolToken(symbol));
                continue;
            }
            currentWord += symbol;
            if (symbol == ' ') {
                if (Token.reservedWords.contains(currentWord)) {
                    tokenList.add(new ReservedWordToken(currentWord));
                } else {
                    if (currentWord.length() > 0) {
                        tokenList.add(new WordToken(currentWord));
                    }
                }
            }
        }



        return tokenList;
    }
}
