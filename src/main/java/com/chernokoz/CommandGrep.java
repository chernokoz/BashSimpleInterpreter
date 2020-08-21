package com.chernokoz;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandGrep extends Command {

    boolean iKey = false;
    boolean wKey = false;
    int keyAValue = 0;

    static class Grep {
        @Option(names = "-i", description = "create a new archive")
        boolean iKey;

        @Option(names = "-w", description = "create a new archive")
        boolean wKey;

        @Option(names = "-A", paramLabel = "ARCHIVE", description = "the archive file")
        int keyAValue;

        @Parameters
        List<String> positional;
    }

    private final ArrayList<String> arguments;

    public CommandGrep(ArrayList<String> args) {
        this.arguments = args;
    }

    @Override
    public void execute() throws Exception {

        Grep parser = new Grep();
        String[] stringArray = arguments.toArray(new String[0]);

        new CommandLine(parser).parseArgs(stringArray);

        iKey = parser.iKey;
        wKey = parser.wKey;
        keyAValue = parser.keyAValue;

        out = runGrep(parser.positional);
    }

    private boolean regexSearch(String str, String regex, boolean wKey) {

        if (wKey) {
            if (regex.startsWith("^") && regex.endsWith("$")) {
                return regexSearch(str, regex, false);
            } else if (regex.startsWith("^")) {
                boolean firstCase = regexSearch(str, regex + "$", false);
                boolean secondCase = regexSearch(str, regex + "\\s", false);
                return firstCase || secondCase;
            } else if (regex.startsWith("$")) {
                boolean firstCase = regexSearch(str, "^" + regex, false);
                boolean secondCase = regexSearch(str, "\\s" + regex, false);
                return firstCase || secondCase;
            } else {
                boolean firstCase = regexSearch(str, "^" + regex + "$" , false);
                boolean secondCase = regexSearch(str, "^" + regex + "\\s", false);
                boolean thirdCase = regexSearch(str, "\\s" + regex + "$", false);
                boolean fourthCase = regexSearch(str, "\\s" + regex + "\\s", false);
                return firstCase || secondCase || thirdCase || fourthCase;
            }
        }

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);

        return m.find();

    }

    private String runGrep(List<String> positional) throws Exception {

        StringJoiner result = new StringJoiner(System.getProperty("line.separator"));
        String pattern;
        String lineToParse;

        if (positional.isEmpty()) {
            throw new Exception();
        } else {
            pattern = positional.get(0);
        }

        if (iKey) {
            pattern = pattern.toLowerCase();
        }

        positional.remove(0);
        int stringsNeedToAdd = 0;

        if (positional.isEmpty()) {

            String[] lines = in.split(System.getProperty("line.separator"));

            for (String line : lines) {

                lineToParse = iKey ? line.toLowerCase() : line;

                boolean needToAdd = regexSearch(lineToParse, pattern, wKey);

                if (needToAdd) {
                    result.add(line);
                    stringsNeedToAdd = keyAValue;
                } else if (--stringsNeedToAdd >= 0) {
                    result.add(line);
                }

            }

        } else {
            for (String fileName : positional) {
                String content = new String(Files.readAllBytes(Paths.get(fileName)));
                result.add(runGrep(Collections.singletonList(content)));
            }
        }
        return result.toString();
    }
}

