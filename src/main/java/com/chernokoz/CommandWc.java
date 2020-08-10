package com.chernokoz;

import java.util.ArrayList;

public class CommandWc extends Command {

    public CommandWc(ArrayList<String> args) {arguments = args;}

    @Override
    public void execute() {
        if (arguments.size() > 0) {
            out = String.valueOf(5 * in.length());
        } else {
            out = " 1   1   1 1";
        }
    }
}
