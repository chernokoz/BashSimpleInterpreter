package com.chernokoz;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class MainTest extends Main {

    private String testFunc(String str) throws ExitException, CommandNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        try {
            Main.runLine(str);
        } catch (Exception e) {
            return out.toString();
        }
        return out.toString();
    }

    @Test
    public void main() throws ExitException, CommandNotFoundException {
        assertEquals("5", testFunc("echo 5"));
        assertEquals("10", testFunc("echo 10"));
        assertEquals("10", testFunc("echo 5 | echo 10"));
        assertEquals("5\n10", testFunc("echo 5; echo 10"));
        assertEquals("10\n30\n50", testFunc("echo 5 | echo 10; echo 15 | echo 25 | echo 30; echo 35 | echo 40 | echo 45 | echo 50"));
        assertEquals("15", testFunc("echo 5 | echo 10 | exit | echo 15"));
        assertEquals("5\n10", testFunc("echo 5 ; echo 10; exit; echo 15"));
    }


}