package com.chernokoz;

import java.lang.reflect.Array;
import java.util.*;

public interface Token {

    String getToken();

    List<String> reservedWords = Arrays.asList("cat", "echo", "wc", "pwd", "exit");
    List<Character> specialSymbols = Arrays.asList('$', '\'', '\"', '|', '=', ';');
    Map<String, Class<?>> commandMap = Map.of("cd", CommandCd.class, "echo", CommandEcho.class);

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
