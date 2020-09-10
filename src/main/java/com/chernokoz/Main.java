package com.chernokoz;

import com.chernokoz.commands.Command;
import com.chernokoz.exceptions.CommandNotFoundException;
import com.chernokoz.commands.CommandPwd;
import com.chernokoz.exceptions.ExitException;
import com.chernokoz.exceptions.StopException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        var env = new Environment();
        System.out.println(env.getCurrentDirectory());

        var in = new Scanner(System.in);

        String str;
        boolean result = false;

        while (true) {

            try {
                str = in.nextLine();
            } catch (NoSuchElementException e) {
                continue;
            }

            try {
                result = runLine(str, env);
            } catch (ExitException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (result) System.out.print(System.lineSeparator());
        }
    }

    /**
     * function with logic for run one line
     */
    protected static boolean runLine(String str, Environment env) throws ExitException, IOException {
        var lexer = new Lexer(str);

        boolean result = false;

        var parser = new Parser(lexer.run(), env);

        ArrayList<ArrayList<Command>> commands;
        try {
            commands = parser.run();
        } catch (StopException e) {
            return false;
        }
        Command prev = null;
        String commandSequenceOut;
        boolean needNewLine = false;

        String sep = System.lineSeparator();

        for (ArrayList<Command> commandSequence : commands) {

            commandSequenceOut = null;

            for (Command command : commandSequence) {

                if (prev != null) {
                    command.putIn(prev.getOut());
                }
                command.execute();
                prev = command;

                if (command.equals(commandSequence.get(commandSequence.size() - 1))) {
                    commandSequenceOut = command.getOut();
                }
            }

            if (!commandSequence.equals(commands.get(0)) && commandSequenceOut != null && needNewLine) {
                System.out.print(sep);
                needNewLine = false;
            }

            if (commandSequenceOut != null && !commandSequenceOut.equals("")) {
                System.out.print(commandSequenceOut);
                needNewLine = true;
                result = true;
            }
        }

        return result;
    }
}
