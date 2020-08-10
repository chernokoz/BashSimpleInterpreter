package com.chernokoz;

import java.util.ArrayList;

public class CommandPwd extends Command {


    public CommandPwd(ArrayList<String> args) {
        arguments = args;
    }

    @Override
    public void execute() {
        if (arguments.isEmpty()) {
            out = "Current Directory";
        } else {
            System.out.println("pwd: too many arguments");
        }
    }
}
