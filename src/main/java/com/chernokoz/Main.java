package com.chernokoz;

import com.chernokoz.commands.Command;
import com.chernokoz.exceptions.CommandNotFoundException;
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

    public static void main(String[] args) throws IOException {

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
            } catch (CommandNotFoundException e) {
                System.out.println("Command not found!");
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        System.out.print("[ Shell != Neste ] -> ");
    }

    protected static void runLine(String str, Environment env) throws ExitException, CommandNotFoundException, IOException {
            Lexer lexer = new Lexer(str);

            Parser parser = new Parser(lexer.run(), env);

        ArrayList<ArrayList<Command>> commands = null;
        try {
            commands = parser.run();
        } catch (StopException e) {
            return;
        }
        Command prev = null;
            String commandSequenceOut = null;
            Command lastCommand = null;
            boolean needNewLine = false;

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
                        lastCommand = command;
                    }
                }

                if (!commandSequence.equals(commands.get(0)) && commandSequenceOut != null && needNewLine) {
                    System.out.print("\n");
                    needNewLine = false;
                }

                if (commandSequenceOut != null) {
                    System.out.print(commandSequenceOut);
                    needNewLine = true;
                }
            }
        }
    }
