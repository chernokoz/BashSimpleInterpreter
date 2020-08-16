package com.chernokoz;

import java.util.ArrayList;

public class CommandPwd extends Command {

    private final Environment env;

    public CommandPwd(ArrayList<String> args, Environment env) {
        arguments = args;
        this.env = env;
    }

    @Override
    public void execute() {
        if (arguments.isEmpty()) {
            out = env.getCurrentDirectory();
        } else {
            System.out.println("pwd: too many arguments");
        }
    }
}
