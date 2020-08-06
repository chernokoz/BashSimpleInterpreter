package com.chernokoz;

import java.util.ArrayList;

public class CommandEcho extends Command {

    public CommandEcho(ArrayList<String> args) {
        this.arguments = args;
    }

    @Override
    public void execute() {
    }
}
