package com.chernokoz;

import com.chernokoz.commands.Command;
import com.chernokoz.exceptions.CommandNotFoundException;
import com.chernokoz.exceptions.ExitException;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("Welcome to shell!");
        Environment env = new Environment();
        System.out.print("Current directory is: ");
        System.out.println(env.getCurrentDirectory());

        Scanner in = new Scanner(System.in);

        String str;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("[ Shell != Neste ] -> ");

        Console console = System.console();
        while (true) {

//            str = reader.readLine();
//            if ( str == null ) {
//                continue;
//            }
//            try {
//                str = console.readLine("user: ");
//            } catch (NullPointerException e) {
//                continue;
//            }


            try {
                str = in.nextLine();
            } catch (NoSuchElementException e) {
                continue;
            }

//            System.out.print("[ Shell != Neste ] -> ");

//            System.out.println("You entered string " + str);

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

            ArrayList<ArrayList<Command>> commands = parser.run();
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
