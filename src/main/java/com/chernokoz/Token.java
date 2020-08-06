package com.chernokoz;

import java.lang.reflect.Array;
import java.util.*;

public interface Token {

    public String getToken();

    List<String> reservedWords = Arrays.asList("cat", "echo", "wc", "pwd", "exit");
    List<Character> specialSymbols = Arrays.asList('$', '\'', '\"', '|', '=', ';');
    Map<String, Class<?>> commandMap = Map.of("cd", CommandCd.class, "echo", CommandEcho.class);




}
