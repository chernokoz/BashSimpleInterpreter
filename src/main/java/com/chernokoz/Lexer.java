package com.chernokoz;

import java.util.ArrayList;

/**
 * class for make lexical analyse of command and get tokens
 */
public class Lexer {

    /**
     * command line
     */
    private final String string;

    public Lexer(String text) {
        this.string = text;
    }

    /**
     * run Lexer
     * @return Tokens, creating this line
     */
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
            }  else {
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
            } else {
                tokenList.add(new WordToken(currentWord));
            }
        }

        return tokenList;
    }

    /**
     * factory method to create Token instance
     * @param currentWord current word
     * @return Token equals to this word
     */
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
