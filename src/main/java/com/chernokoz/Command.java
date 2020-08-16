package com.chernokoz;

import java.io.IOException;
import java.util.ArrayList;

public abstract class Command {

    String in = null;
    String out = null;

    ArrayList<String> arguments = null;

    public void putIn(String in) {
        this.in = in;
    }

    public abstract void execute() throws ExitException, IOException;

    public static Command createCommandInstance(String command, ArrayList<String> args, boolean isLastFlag, Environment env) {
        return switch (command) {
            case "echo" -> new CommandEcho(args);
            case "cd" -> new CommandCd(args);
            case "pwd" -> new CommandPwd(args, env);
            case "cat" -> new CommandCat(args,env);
            case "exit" -> new CommandExit(args, isLastFlag);
            case "wc" -> new CommandWc(args, env);
            default -> null;
        };
    }

}
