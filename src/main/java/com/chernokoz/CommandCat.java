package com.chernokoz;

import java.util.ArrayList;

public class CommandCat extends Command {


    public CommandCat(ArrayList<String> args) {
        arguments = args;
    }

    @Override
    public void execute() {
        if (arguments.size() > 0) {
            System.out.println("TODO"); //TODO
        } else {
            System.out.println(in);
        }
    }
}
