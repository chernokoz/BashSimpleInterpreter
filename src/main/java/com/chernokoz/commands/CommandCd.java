package com.chernokoz.commands;

import com.chernokoz.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * class for command cd
 */
public class CommandCd extends Command {

    private final Environment env;

    public CommandCd(ArrayList<String> args, Environment env ) {
        super(args);
        this.env = env;
    }

    public void execute() throws IOException {
        List<String> arguments = getArgs();
        if (arguments.size() > 1) {
            System.out.println("Wrong number of arguments");
            return;
        }
        if (arguments.size() == 1) {
            String arg = arguments.get(0);
            if (arg.charAt(0) != '/') {
                arg = env.getCurrentDirectory() + "/" + arg;
            }
            File file = new File(arg);
            if (file.exists() && file.isDirectory()) {
                env.setCurrentDirectory(arg);
            } else {
                System.out.println("This directory does not exist");
            }
        }
    }
}
