package com.chernokoz.commands;

import java.util.ArrayList;

/**
 * class for command echo
 */
public class CommandEcho extends Command {

    public CommandEcho(ArrayList<String> args) {
        super(args);
    }

    @Override
    public void execute() {
        putOut(String.join(" ", getArgs()));
    }
}
