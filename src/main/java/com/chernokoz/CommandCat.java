package com.chernokoz;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.StringJoiner;

public class CommandCat extends Command {

    private final Environment env;

    public CommandCat(ArrayList<String> args, Environment env) {
        arguments = args;
        this.env = env;
    }

    @Override
    public void execute() throws IOException {
        if (arguments.size() > 0) {
            StringJoiner res = new StringJoiner("\n");
            for (String arg : arguments) {
                try {
                    String contents = new String(Files.readAllBytes(Paths.get(arg)));
                    res.add(contents);
                } catch (FileNotFoundException e) {
                    System.out.println("error: file not found");
                }
            }
            out = res.toString();
        } else {
            out = in;
        }
    }
}
