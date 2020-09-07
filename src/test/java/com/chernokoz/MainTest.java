package com.chernokoz;

import com.chernokoz.exceptions.ExitException;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class MainTest {

    private String testFunc(String str) {
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
    public void echo() {
        String s = System.lineSeparator();
        assertEquals("5", testFunc("echo 5"));
        assertEquals("10", testFunc("echo 10"));
        assertEquals("10", testFunc("echo 5 | echo 10"));
        assertEquals(String.format("5%s10", s), testFunc("echo 5  ; echo 10"));
        assertEquals(String.format("10%s30%s50", s, s),
                testFunc("echo 5 | echo 10; echo 15 | echo 25 | echo 30; echo 35 | echo 40 | echo 45 | echo 50"));
    }

    @Test
    public void cat() throws IOException {
        String s = System.lineSeparator();

        assertEquals("123", testFunc("echo 123 | cat"));
        assertEquals("123", testFunc("echo 123 | cat | cat | cat | cat"));

        File file1 = File.createTempFile( "chr", ".txt");
        file1.deleteOnExit();

        File file2 = File.createTempFile( "chr", ".txt");
        file1.deleteOnExit();

        FileWriter writer1 = new FileWriter(file1.getAbsolutePath(), false);
        FileWriter writer2 = new FileWriter(file2.getAbsolutePath(), false);

        writer1.write(String.format("11111%s22222%s33333", s, s));
        writer2.write(String.format("444%s555%s666", s, s));

        writer1.flush();
        writer2.flush();

        assertEquals(String.format("11111%s22222%s33333", s, s), testFunc("cat " + file1.getAbsolutePath()));
        assertEquals(String.format("11111%s22222%s33333%s444%s555%s666", s, s, s, s, s),
                testFunc("cat " + file1.getAbsolutePath() + " " + file2.getAbsolutePath()));
    }

    @Test
    public void exit() {
        String s = System.lineSeparator();
        assertEquals("15", testFunc("echo 5 | echo 10 | exit | echo 15"));
        assertEquals(String.format("5%s10", s), testFunc("echo 5 ; echo 10; exit; echo 15"));
        assertEquals("exit: too many arguments" + System.lineSeparator(),
                testFunc("echo 123 | exit 123 456"));
    }

    @Test
    public void wc() {
        assertEquals("\t1\t1\t2", testFunc("echo 5 | wc"));
        assertEquals("\t1\t1\t7", testFunc("echo 123123 | wc"));
        assertEquals("\t1\t3\t7", testFunc("echo 123123 | wc | wc"));
    }

    @Test
    public void dollarAndEquals() {
        String s = System.lineSeparator();
        assertEquals("5", testFunc("a=5; echo $a"));
        assertEquals(String.format("3%s1", s), testFunc("a=5; a=3; echo $a; a=1; echo $a"));
        assertEquals("", testFunc("echo $a"));
        assertEquals(String.format("%s", s), testFunc("a=1 | echo $a; a=2 | echo $a"));
        assertEquals("5", testFunc("a=b; $a=5; echo $b"));
        assertEquals("", testFunc("a= ; echo $a"));
    }

    @Test
    public void doubleQuoting() {
        assertEquals("123", testFunc("echo \"123\""));
        assertEquals(" 123 ", testFunc("echo \" 123 \""));
        assertEquals("5 123 5", testFunc("a=5; echo \"$a 123 $a\""));
        assertEquals("5 5 $a", testFunc("a=5; echo $a \"$a\" '$a'"));
        assertEquals(" '3'3 ", testFunc("a=3; echo \" '$a'$a \""));
    }

    @Test
    public void singleQuoting() {
        assertEquals("222", testFunc("echo '222'"));
        assertEquals("$a", testFunc("echo '$a'"));
        assertEquals("222 222 111 111", testFunc("echo '222 222' '111 111'"));
    }

    @Test
    public void outsideCommand() {
        String s = System.lineSeparator();
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");
        if (isWindows) {
            assertEquals(String.format("rootProject.name = 'bashInterpreter'%s", s), testFunc("type settings.gradle"));
        } else {
            assertEquals("235", testFunc("echo 5 | printf 235"));
        }
    }

    @Test
    public void dollarsSuperCase() {
        assertEquals("", testFunc("a=ex; b=it; $a$b"));
    }
}