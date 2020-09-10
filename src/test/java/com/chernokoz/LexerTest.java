package com.chernokoz;

import org.junit.Test;

import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void run() {
        Lexer lexer1 = new Lexer("echo \"abc\" | cat text.txt | cd Downloads");
        var tokens1 = lexer1.run();

        assertEquals(17, tokens1.size());

        Lexer lexer2 = new Lexer("echo        \"    a b c    \"   | cd /");
        var tokens2 = lexer2.run();

        assertEquals(17, tokens2.size());
    }
}