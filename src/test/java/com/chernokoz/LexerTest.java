package com.chernokoz;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void run() {

        Lexer lexer1 = new Lexer("echo \"abc\" | cat text.txt | cd Downloads");
        var tokens1 = lexer1.run();

        tokens1.forEach(
                x -> System.out.println(x.getToken())
        );

        assertEquals(17, tokens1.size());



        Lexer lexer2 = new Lexer("echo        \"    a b c    \"   | cd /");
        var tokens2 = lexer2.run();

        tokens2.forEach(
                x -> System.out.println(x.getToken())
        );

        assertEquals(17, tokens2.size());
    }
}