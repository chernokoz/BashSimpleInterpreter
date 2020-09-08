package com.chernokoz.commands;

import com.chernokoz.exceptions.PatternNotFoundException;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * class for command grep
 */
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
        super(args);
        this.arguments = args;
    }

    @Override
    public void execute() throws PatternNotFoundException, IOException {

        Grep parser = new Grep();
        String[] stringArray = arguments.toArray(new String[0]);

        new CommandLine(parser).parseArgs(stringArray);

        iKey = parser.iKey;
        wKey = parser.wKey;
        keyAValue = parser.keyAValue;

        putOut(runGrep(parser.positional));
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

    /**
     * function for run grep logic
     */
    private String runGrep(List<String> positional) throws PatternNotFoundException, IOException {

        StringJoiner result = new StringJoiner(System.lineSeparator());
        String pattern;
        String lineToParse;

        if (positional.isEmpty()) {
            throw new PatternNotFoundException();
        } else {
            pattern = positional.get(0);
        }

        if (iKey) {
            pattern = pattern.toLowerCase();
        }

        positional.remove(0);
        int stringsNeedToAdd = 0;

        if (positional.isEmpty()) {

            String[] lines = getIn().split(System.lineSeparator());

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