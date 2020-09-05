package com.chernokoz.commands;

import com.chernokoz.Environment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.StringJoiner;

public class CommandCat extends Command {

    private final Environment env;

    public CommandCat(ArrayList<String> args, Environment env) {
        super(args);
        this.env = env;
    }

    @Override
    public void execute() throws IOException {
        final var arguments = getArgs();
        if (arguments.size() > 0) {
            String sep = System.lineSeparator();
            StringJoiner res = new StringJoiner(sep);
            for (String arg : arguments) {
                try {
                    String contents = new String(Files.readAllBytes(Paths.get(arg)));
                    res.add(contents);
                } catch (FileNotFoundException e) {
                    System.out.println("error: file not found");
                }
            }
            putOut(res.toString());
        } else {
            putOut(getIn());
        }
    }
}
