package com.chernokoz.commands;

import com.chernokoz.Environment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class CommandWc extends Command {

    private final Environment env;

    public CommandWc(ArrayList<String> args, Environment env) {
        super(args);
        this.env = env;
    }

    public String wcHelper(String content, String fileName) {
        int linesCount = content.split(System.lineSeparator(), -1).length;
        int wordsCount = content.trim().split("\\s+", -1).length;
        int length = content.getBytes().length + 1;
        return "\t" + linesCount + "\t" + wordsCount + "\t" + length + (fileName == null ? "" : " " + fileName) ;
    }

    @Override
    public void execute() {
        List<String> arguments = getArgs();
        if (arguments.size() > 0) {
            StringJoiner joiner = new StringJoiner(System.lineSeparator());
            for (String arg : arguments) {
                try {
                    Stream<String> stream = Files.lines(Paths.get(arg));
                    StringBuilder contentBuilder = new StringBuilder();
                    stream.forEach(s -> contentBuilder.append(s).append(System.lineSeparator()));
                    joiner.add(wcHelper(contentBuilder.toString(), arg));
                } catch (FileNotFoundException e) {
                    System.out.println("wc: " + arg + ": No such file or directory");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            putOut(joiner.toString());
        } else {
            putOut(wcHelper(getIn(), null));
        }
    }
}
