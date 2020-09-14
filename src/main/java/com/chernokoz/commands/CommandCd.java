package com.chernokoz.commands;

import com.chernokoz.Environment;

import java.io.File;
import java.util.ArrayList;

/** change directory command
 * changes the current working directory
 * to the one set in first argument
 * in case the directory exists **/
public class CommandCd extends Command {

    private String newPath;
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
        if (newPath.endsWith("/")) { // handle for windows
            newPath = newPath.substring(0, newPath.length() - 1);
        }

        String currDir = handleMoveUp(env.getCurrentDirectory(), newPath);
        if (!currDir.endsWith(File.separator) && !newPath.equals("..")) {
            currDir += File.separator;
        }

        var file = new File(currDir);
        if (file.exists() && file.isDirectory() && file.canRead()) {
            env.setCurrentDirectory(currDir);
        } else {
            System.out.println("-bash: cd: " + newPath + ": Not a directory");
        }
    }

    private String handleMoveUp(String currDir, String newPath) {
        var withoutLastSep = currDir.substring(0, currDir.length() - 1);

        // handle fs root case
        if (withoutLastSep.lastIndexOf(File.separator) == -1)
            return currDir;

        if (newPath.equals("..")) {
            return currDir.substring(0,
                    withoutLastSep.lastIndexOf(File.separator)) + File.separator;
        } else {
            return currDir + newPath;
        }
    }
}
