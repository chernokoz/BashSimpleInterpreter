package com.chernokoz;

import com.chernokoz.exceptions.CommandNotFoundException;
import com.chernokoz.exceptions.ExitException;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class MainTest {

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
        assertEquals("5\n10", testFunc("echo 5  ; echo 10"));
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

        File file1 = File.createTempFile( "chr", ".txt");
        file1.deleteOnExit();

        File file2 = File.createTempFile( "chr", ".txt");
        file1.deleteOnExit();

        FileWriter writer1 = new FileWriter(file1.getAbsolutePath(), false);
        FileWriter writer2 = new FileWriter(file2.getAbsolutePath(), false);

        writer1.write("11111\n22222\n33333");
        writer2.write("444\n555\n666");

        writer1.flush();
        writer2.flush();

        assertEquals("11111\n22222\n33333", testFunc("cat " + file1.getAbsolutePath()));
        assertEquals("11111\n22222\n33333\n444\n555\n666", testFunc("cat " + file1.getAbsolutePath() + " " + file2.getAbsolutePath()));
    }

    @Test
    public void exit() throws ExitException, CommandNotFoundException {
        assertEquals("15", testFunc("echo 5 | echo 10 | exit | echo 15"));
        assertEquals("5\n10", testFunc("echo 5 ; echo 10; exit; echo 15"));
        assertEquals("exit: too many arguments" + System.lineSeparator(), testFunc("echo 123 | exit 123 456"));
    }

    @Test
    public void wc() throws ExitException, CommandNotFoundException {
        assertEquals("\t1\t1\t2", testFunc("echo 5 | wc"));
        assertEquals("\t1\t1\t7", testFunc("echo 123123 | wc"));
        assertEquals("\t1\t3\t7", testFunc("echo 123123 | wc | wc"));
    }

    @Test
    public void dollarAndEquals() throws ExitException, CommandNotFoundException {
        assertEquals("5", testFunc("a=5; echo $a"));
        assertEquals("3\n1", testFunc("a=5; a=3; echo $a; a=1; echo $a"));
        assertEquals("", testFunc("echo $a"));
        assertEquals("\n", testFunc("a=1 | echo $a; a=2 | echo $a"));
        assertEquals("5", testFunc("a=b; $a=5; echo $b"));
        assertEquals("", testFunc("a= ; echo $a"));
    }

    @Test
    public void doubleQuoting() throws ExitException, CommandNotFoundException {
        assertEquals("123", testFunc("echo \"123\""));
        assertEquals(" 123 ", testFunc("echo \" 123 \""));
        assertEquals("5 123 5", testFunc("a=5; echo \"$a 123 $a\""));
        assertEquals("5 5 $a", testFunc("a=5; echo $a \"$a\" '$a'"));
        assertEquals(" '3'3 ", testFunc("a=3; echo \" '$a'$a \""));
    }

    @Test
    public void singleQuoting() throws ExitException, CommandNotFoundException {
        assertEquals("222", testFunc("echo '222'"));
        assertEquals("$a", testFunc("echo '$a'"));
        assertEquals("222 222 111 111", testFunc("echo '222 222' '111 111'"));
    }

    @Test
    public void outsideCommand() throws ExitException, CommandNotFoundException {
        assertEquals("235", testFunc("echo 7 | printf 235"));

    }
}