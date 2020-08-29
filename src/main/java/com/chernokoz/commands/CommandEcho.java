package com.chernokoz.commands;

import java.util.ArrayList;

public class CommandEcho extends Command {

    public CommandEcho(ArrayList<String> args) {
        super(args);
    }

    @Override
    public void execute() {
        putOut(String.join(" ", getArgs()));
    }
}
