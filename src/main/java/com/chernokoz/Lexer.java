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
                    tokenList.add(finishCurrentWord(currentWord));
                    currentWord = "";
                }
                tokenList.add(new SpecialSymbolToken(symbol));
                continue;
            }
            if (symbol == ' ') {
                if (Token.reservedWords.contains(currentWord)) {
                    tokenList.add(new ReservedWordToken(currentWord));
                    currentWord = "";
                } else {
                    if (currentWord.length() > 0 && !WhiteSpaceToken.checkForWhiteSpace(currentWord)) {
                        tokenList.add(new WordToken(currentWord));
                        currentWord = "";
                    }
                }
            }
            else {
                if (WhiteSpaceToken.checkForWhiteSpace(currentWord)) {
                    tokenList.add(new WhiteSpaceToken(currentWord));
                    currentWord = "";
                }
            }
            currentWord += symbol;
        }
        if (currentWord.length() > 0 && !WhiteSpaceToken.checkForWhiteSpace(currentWord)) {
            if (ReservedWordToken.reservedWords.contains(currentWord)) {
                tokenList.add(new ReservedWordToken(currentWord));
            }
            tokenList.add(new WordToken(currentWord));
        }
        return tokenList;
    }

    private Token finishCurrentWord(String currentWord) {
        if (WhiteSpaceToken.checkForWhiteSpace(currentWord)) {
            return new WhiteSpaceToken(currentWord);
        }
        if (ReservedWordToken.reservedWords.contains(currentWord)) {
            return new ReservedWordToken(currentWord);
        }
        return new WordToken(currentWord);
    }

}
