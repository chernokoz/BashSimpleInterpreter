package com.chernokoz.tokens;

import java.util.*;

/**
 * interface for token
 */
public interface Token {

    /**
     * get string value of the command
     * @return string of command
     */
    String getToken();

    /**
     * reserved words list
     */
    List<String> reservedWords = Arrays.asList("cat", "echo", "wc", "pwd", "exit", "cd", "ls");

    /**
     * special symbols list
     */
    List<Character> specialSymbols = Arrays.asList('$', '\'', '\"', '|', '=', ';');

    /**
     * factory method to create a token instance
     * @param str string of command
     * @return token instance
     */
    static Token createToken(String str) {
        if (WhiteSpaceToken.checkForWhiteSpace(str)) {
            return new WhiteSpaceToken(str);
        }
        if (reservedWords.contains(str)) {
            return new ReservedWordToken(str);
        }
        return new WordToken(str);
    }



}
