package com.chernokoz;

import java.util.ArrayList;

public abstract class Command {

    String in = null;
    String out = null;

    ArrayList<String> arguments = null;

    public abstract void execute();

    public static Command createCommandInstance(String command, ArrayList<String> args) {
        switch (command) {
            case "echo":
                return new CommandEcho(args);
            case "cd":
                return new CommandCd(args);
            default:
                return null;
        }
    }

}
