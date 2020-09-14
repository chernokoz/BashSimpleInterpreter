package com.chernokoz;

import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.regex.Matcher;
import static org.junit.Assert.assertEquals;


public class CdLsTest {

    private String testFunc(String str, Environment env) {
        var out = new ByteArrayOutputStream();
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
        currDir =  System.getProperty("user.dir") + "/";
        env = new Environment();
    }

    @Test
    public void pwd() {
        checkMultiplatform(currDir, "pwd", env);
    }

    @Test
    public void ls() {
        checkMultiplatform(currDir + "src/", "cd src/ | pwd", env);
        checkMultiplatform(currDir + "src/test/", "cd test/ | pwd", env);

        String testDir = currDir + "src/test/resources/testLs/";
        checkMultiplatform(testDir, "cd resources/testLs/ | pwd", env);

        String expected = "dir1\n" +
                "dir2\n" +
                "dir3\n" +
                "somefile.txt\n";
        checkMultiplatform(expected, "ls", env);
    }

    @Test
    public void lsWithArgument() {
        String expected = "dir1\n" +
                "dir2\n" +
                "dir3\n" +
                "somefile.txt\n";
        checkMultiplatform(expected, "ls src/test/resources/testLs/", env);
    }

    @Test
    public void lsOneFile() {
        checkMultiplatform("file3.txt\n","ls src/test/resources/testLs/dir3", env);
    }

    @Test
    public void lsNoSuchFile() {
        final String notExistentPath = "ololo/";
        String expected = "ls: " + notExistentPath  + ": No such file or directory\n";
        checkMultiplatform(expected, "ls " + notExistentPath, env);
    }

    @Test
    public void cd() {
        checkMultiplatform(currDir + "src/", "cd src/ | pwd", env);
        checkMultiplatform(currDir + "src/test/", "cd test/ | pwd", env);
        String testDir = currDir + "src/test/resources/";
        checkMultiplatform(testDir,"cd resources/ | pwd", env);
    }

    @Test
    public void cdUp() {
        String testDir = currDir + "src/test/resources/testLs/";
        assertEquals(testDir + "dir1" + File.separator,
                testFunc("cd " + "src/test/resources/testLs/dir1/" + " | pwd", env));

        checkMultiplatform(testDir,"cd .. | pwd", env);
    }

    @Test
    public void cdFile() {
        checkMultiplatform("-bash: cd: .gitignore: Not a directory\n",
                "cd .gitignore", env);
    }

    @Test
    public void cdUpFromRoot() {
        // check doesn't throw any errors
        checkMultiplatform("",
                "cd .. | cd .. | cd .. | cd .. | cd .. | cd .. | cd .. | cd .. | cd ..", env);
    }

    @Test
    public void cdNotSkipRootDirectory() {
        var rootDir = env.getCurrentDirectory();
        checkMultiplatform(rootDir, "cd src | cd .. | pwd", env);
        assertEquals(rootDir, env.getCurrentDirectory());
    }

    @Test
    public void cdNotSkipProjectDirectory() {
        var rootDir = env.getCurrentDirectory();
        checkMultiplatform(rootDir,
                "cd src | cd .. | cd .. | cd bashSimpleInterpreter | pwd", env);
        assertEquals(rootDir, env.getCurrentDirectory());
    }

    @Test
    public void cdNotExistentDirectory() {
        checkMultiplatform("-bash: cd: kkk: Not a directory\n", "cd kkk", env);
    }


    private void checkMultiplatform(String expected, String command, Environment env) {
        assertEquals(fixSeparators(expected),
                testFunc(fixSeparators(command), env));
    }

    private String fixSeparators(String path) {
        return path
                .replaceAll("\n", System.lineSeparator())
                .replaceAll("/", Matcher.quoteReplacement(File.separator))
                .toLowerCase(); // case insensitivity in Windows
    }
}
