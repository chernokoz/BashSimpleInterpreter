package com.chernokoz;

import java.util.ArrayList;

public abstract class Command {

    String in = null;
    String out = null;

    ArrayList<String> arguments = null;

    public void putIn(String in) {
        this.in = in;
    }

    public abstract void execute() throws ExitException;

    public static Command createCommandInstance(String command, ArrayList<String> args, boolean isLastFlag) {
        return switch (command) {
            case "echo" -> new CommandEcho(args);
            case "cd" -> new CommandCd(args);
            case "pwd" -> new CommandPwd(args);
            case "cat" -> new CommandCat(args);
            case "exit" -> new CommandExit(args, isLastFlag);
            case "wc" -> new CommandWc(args);
            default -> null;
        };
    }

}
