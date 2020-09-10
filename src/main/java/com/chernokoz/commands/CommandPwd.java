package com.chernokoz.commands;

import com.chernokoz.Environment;

import java.util.ArrayList;

/**
 * class for command pwd
 */
public class CommandPwd extends Command {

    private final Environment env;

    public CommandPwd(ArrayList<String> args, Environment env) {
        super(args);
        this.env = env;
    }

    @Override
    public void execute() {
        if (getArgs().isEmpty()) {
            putOut(env.getCurrentDirectory());
        } else {
            System.out.println("pwd: too many arguments");
        }
    }
}
