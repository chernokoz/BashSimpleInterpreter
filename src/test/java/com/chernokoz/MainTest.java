package com.chernokoz;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class MainTest extends Main {

    private String testFunc(String str) throws ExitException, CommandNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        try {
            Main.runLine(str, new Environment());
        } catch (Exception e) {
            return out.toString();
        }
        return out.toString();
    }

    @Test
    public void echo() throws ExitException, CommandNotFoundException {
        assertEquals("5", testFunc("echo 5"));
        assertEquals("10", testFunc("echo 10"));
        assertEquals("10", testFunc("echo 5 | echo 10"));
        assertEquals("5\n10", testFunc("echo 5; echo 10"));
        assertEquals("10\n30\n50", testFunc("echo 5 | echo 10; echo 15 | echo 25 | echo 30; echo 35 | echo 40 | echo 45 | echo 50"));
    }

    @Test
    public void pwd() throws ExitException, CommandNotFoundException {
        assertEquals("", testFunc("pwd"));
        assertEquals("", testFunc("pwd | echo"));
    }

    @Test
    public void cat() throws ExitException, CommandNotFoundException, IOException {
        assertEquals("123", testFunc("echo 123 | cat"));
        assertEquals("123", testFunc("echo 123 | cat | cat | cat | cat"));

        File file = File.createTempFile( "chr", ".txt");
        file.deleteOnExit();

        FileWriter writer = new FileWriter(file.getAbsolutePath(), false);

        writer.write("11111\n22222\n33333");

        writer.flush();

        assertEquals("11111\n22222\n33333", testFunc("cat " + file.getAbsolutePath()));
    }

    @Test
    public void exit() throws ExitException, CommandNotFoundException {
        assertEquals("15", testFunc("echo 5 | echo 10 | exit | echo 15"));
        assertEquals("5\n10", testFunc("echo 5 ; echo 10; exit; echo 15"));
        assertEquals("exit: too many arguments\n", testFunc("echo 123 | exit 123 456"));
    }

    @Test
    public void wc() throws ExitException, CommandNotFoundException {
        assertEquals("\t1\t1\t2", testFunc("echo 5 | wc"));
        assertEquals("\t1\t1\t7", testFunc("echo 123123 | wc"));
        assertEquals("\t1\t3\t7", testFunc("echo 123123 | wc | wc"));
    }
}