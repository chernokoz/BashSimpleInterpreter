package com.chernokoz;

import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.assertEquals;


public class CdLsTest {

    private String testFunc(String str, Environment env) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        try {
            Main.runLine(str, env);
        } catch (Exception e) {
            return out.toString();
        }
        return out.toString();
    }

    private String currDir;
    private Environment env;

    @Before
    public void setUp() {
        currDir =  System.getProperty("user.dir");
        env = new Environment();
    }

    @Test
    public void pwd() {
        assertEquals(currDir + "/", testFunc("pwd", env));
        assertEquals("", testFunc("pwd | echo", env));
    }

    @Test
    public void ls() {
        assertEquals(currDir + "/src/", testFunc("cd src/ | pwd", env));
        assertEquals(currDir + "/src/test/", testFunc("cd test/ | pwd", env));
        String testDir = currDir + "/src/test/resources/testLs";
        assertEquals(testDir, testFunc("cd resources/testLs | pwd", env));

        String expected = "somefile.txt\n" +
                "dir2\n" +
                "dir3\n" +
                "dir1\n";
        assertEquals(expected, testFunc("ls", env));
    }

    @Test
    public void lsWithArgument() {
        String expected = "somefile.txt\n" +
                "dir2\n" +
                "dir3\n" +
                "dir1\n";
        assertEquals(expected, testFunc("ls src/test/resources/testLs/", env));
    }

    @Test
    public void lsOneFile() {
        assertEquals("file3.txt\n", testFunc("ls src/test/resources/testLs/dir3", env));
    }

    @Test
    public void lsNoSuchFile() {
        final String notExistentPath = "ololo/";
        String expected = "ls: " + notExistentPath  + ": No such file or directory\n";
        assertEquals(expected, testFunc("ls " + notExistentPath, env));
    }

    @Test
    public void cd() {
        assertEquals(currDir + "/src/", testFunc("cd src/ | pwd", env));
        assertEquals(currDir + "/src/test/", testFunc("cd test/ | pwd", env));
        String testDir = currDir + "/src/test/resources/";
        assertEquals(testDir, testFunc("cd resources/ | pwd", env));
    }

    @Test
    public void cdUp() {
        String testDir = currDir + "/src/test/resources/testLs/";
        assertEquals(testDir + "dir1/",
                testFunc("cd " + "src/test/resources/testLs/dir1/" + " | pwd", env));

        assertEquals(testDir, testFunc("cd " + ".." + " | pwd", env));
    }

    @Test
    public void cdFile() {
        assertEquals("-bash: cd: .gitignore: Not a directory\n",
                testFunc("cd .gitignore", env));
    }

    @Test
    public void cdNotExistentDirectory() {
        assertEquals("-bash: cd: kkk: Not a directory\n",
                testFunc("cd kkk", env));
    }
}
