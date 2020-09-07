package com.chernokoz.commands;

import com.chernokoz.Environment;

import java.io.File;
import java.util.ArrayList;

/** change directory command
 * changes the current working directory
 * to the one set in first argument
 * in case the directory exists **/
public class CommandCd extends Command {

    private final String newPath;
    private final Environment env;

    /** gets only first argument. Other arguments are ignored **/
    public CommandCd(ArrayList<String> args, Environment env) {
        super(args);
        if (args.size() > 0) {
            newPath = args.get(0);
        } else {
            newPath = "";
        }
        this.env = env;
    }

    public void execute() {
        String currDir = handleMoveUp(env.getCurrentDirectory(), newPath);

        File file = new File(currDir);
        if (file.exists() && file.isDirectory()) {
            env.setCurrentDirectory(currDir);
        } else {
            System.out.println("-bash: cd: " + newPath + ": Not a directory");
        }
    }

    private String handleMoveUp(String currDir, String newPath) {
        String res = currDir;

        if (newPath.equals("..")) {
            res = currDir.substring(0, currDir
                    .substring(0, currDir.length() - 1)
                    .lastIndexOf('/')) + "/";
        } else {
            res += newPath;
        }

        return res;
    }
}
