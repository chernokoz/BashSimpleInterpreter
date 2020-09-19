package com.chernokoz.commands;

import com.chernokoz.Environment;

import java.io.File;
import java.util.ArrayList;

public class CommandLs extends Command {

    private final Environment env;

    public CommandLs(ArrayList<String> args, Environment env) {
        super(args);
        this.env = env;
    }

    @Override
    public void execute() {
        final var arguments = getArgs();
        String out = "";
        String curDir = null;
        if (arguments.size() == 0) {
            curDir = env.getCurrentDirectory();
        } else if (arguments.size() == 1) {
            curDir = arguments.get(0);
        } else {
            System.out.println("Wrong number of arguments");
        }
        File file = new File(curDir);

        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();

            for (File f: listFiles) {
                out += f.getName() + "\n";
            }
        } else if (file.isFile()) {
            out = file.getName() + "\n";
        } else {
            System.out.println("No such file or directory");
        }
        putOut(out);
    }
}
