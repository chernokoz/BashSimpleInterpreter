package com.chernokoz;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public interface Token {
    public List<String> reservedWords = Arrays.asList("cat", "echo", "wc", "pwd", "exit");
    public List<Character> specialSymbols = Arrays.asList('$', '\'', '\"', '|', '=');
}
