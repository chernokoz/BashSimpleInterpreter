package com.chernokoz;

import com.chernokoz.commands.Command;
//import com.chernokoz.exceptions.CommandNotFoundException;
import com.chernokoz.exceptions.ExitException;
import com.chernokoz.exceptions.StopException;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Environment env = new Environment();
        System.out.println(env.getCurrentDirectory());

        Scanner in = new Scanner(System.in);

        String str;

        while (true) {

            try {
                str = in.nextLine();
            } catch (NoSuchElementException e) {
                continue;
            }

            try {
                runLine(str, env);
            } catch (ExitException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        System.out.print("[ Shell != Neste ] -> ");
    }

    protected static void runLine(String str, Environment env) throws ExitException, IOException {
        Lexer lexer = new Lexer(str);

        Parser parser = new Parser(lexer.run(), env);

        ArrayList<ArrayList<Command>> commands;
        try {
            commands = parser.run();
        } catch (StopException e) {
            return;
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

            if (commandSequenceOut != null) {
                System.out.print(commandSequenceOut);
                needNewLine = true;
            }
        }
    }
}
