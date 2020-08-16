package com.chernokoz;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringJoiner;

public class CommandWc extends Command {

    private final Environment env;

    public CommandWc(ArrayList<String> args, Environment env) {
        arguments = args;
        this.env = env;
    }

    public String wcHelper(String content, String fileName) {
        int linesCount = content.split("\n", -1).length;
        int wordsCount = content.trim().split("\\s+", -1).length;
        int length = content.getBytes().length + 1;
        return "\t" + linesCount + "\t" + wordsCount + "\t" + length + (fileName == null ? "" : " " + fileName) ;
    }

    @Override
    public void execute() {
        if (arguments.size() > 0) {
            StringJoiner joiner = new StringJoiner("\n");
            for (String arg : arguments) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(env.getCurrentDirectory() + arg));
                    joiner.add(wcHelper(reader.toString(), arg));
                } catch (FileNotFoundException e) {
                    System.out.println("wc: " + arg + ": No such file or directory");
                }
            }
            out = joiner.toString();
        } else {
            out = wcHelper(in, null);
        }
    }
}
