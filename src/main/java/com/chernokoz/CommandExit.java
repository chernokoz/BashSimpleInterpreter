package com.chernokoz;

import java.util.ArrayList;

public class CommandExit extends Command {

    private boolean isLast = false;

    public CommandExit(ArrayList<String> args, boolean isLastFlag) {
        arguments = args;
        isLast = isLastFlag;
    }

    @Override
    public void execute() throws ExitException {
        if (arguments.size() > 1) {
            System.out.println("exit: too many arguments");
        } else if (isLast) {
            throw new ExitException("fd");
        }
    }
}
