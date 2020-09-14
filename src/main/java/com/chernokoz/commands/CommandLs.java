package com.chernokoz.commands;

import com.chernokoz.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;


/** list computer files
 * in case first argument is empty list all files in current working directory
 * if first argument is not empty then list all files
 * in working directory plus additional path
 * if path does not exists prints error **/
public class CommandLs extends Command {

    private final Environment env;
    private String additionalPath = null;

    /** consider only first argument **/
    public CommandLs(ArrayList<String> args, Environment env) {
        super(args);

        if (!args.isEmpty()) {
            additionalPath = args.get(0);
        }
        this.env = env;
    }

    @Override
    public void execute() {
        String directory = env.getCurrentDirectory();
        putOut(lsArgument(directory, additionalPath));
    }

    private String lsArgument(String directory, String arg) {
        StringBuilder res = new StringBuilder();

        String path = directory;
        if (arg != null) {
            path += arg;
        }

        var file = new File(path);

        if (file.isDirectory()) {
            Arrays.stream(Objects.requireNonNull(file.list()))
                    .sorted()
                    .map(p -> res.append(p).append("\n"))
                    .collect(Collectors.toList());
        } else if (file.isFile()) {
            res.append(directory).append("\n");
        } else {
            System.out.println("ls: " + arg + ": No such file or directory");
        }

        return res.toString();
    }
}
