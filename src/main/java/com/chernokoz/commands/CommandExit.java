package com.chernokoz.commands;

import com.chernokoz.exceptions.ExitException;

import java.util.ArrayList;

public class CommandExit extends Command {

    private boolean isLast = false;

    public CommandExit(ArrayList<String> args, boolean isLastFlag) {
        super(args);
        isLast = isLastFlag;
    }

    @Override
    public void execute() throws ExitException {
        if (getArgs().size() > 1) {
            System.out.println("exit: too many arguments");
        } else if (isLast) {
            throw new ExitException();
        }
    }
}
