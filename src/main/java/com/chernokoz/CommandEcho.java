package com.chernokoz;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CommandEcho extends Command {

    public CommandEcho(ArrayList<String> args) {
        this.arguments = args;
    }

    @Override
    public void execute() {
        out = String.join(" ", arguments);
    }
}
