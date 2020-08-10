package com.chernokoz;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Welcome to shell!...");
        Environment env = new Environment();
        System.out.print("Current directory is: ");
        System.out.println(env.getCurrentDirectory());

        while (true) {

            Scanner in = new Scanner(System.in);
            System.out.print("[ Shell != Neste ] -> ");
            String str = in.nextLine();
            System.out.println("You entered string " + str);

            try {
                runLine(str);
            } catch (ExitException e) {
                break;
            } catch (CommandNotFoundException e) {
                System.out.println("Command not found!");
            }

    }
    }

    protected static void runLine(String str) throws ExitException, CommandNotFoundException {
            Lexer lexer = new Lexer(str);

            Parser parser = new Parser(lexer.run());

            ArrayList<ArrayList<Command>> commands = parser.run();
            Command prev = null;
            String commandSequenceOut = null;
            Command lastCommand = null;

            for (ArrayList<Command> commandSequence : commands) {

                for (Command command : commandSequence) {
                    if (prev != null) {
                        command.putIn(prev.out);
                    }
                    command.execute();
                    prev = command;

                    if (command.equals(commandSequence.get(commandSequence.size() - 1))) {
                        commandSequenceOut = command.out;
                        lastCommand = command;
                    }
                }

                if (!commandSequence.equals(commands.get(0)) && commandSequenceOut != null) {
                    System.out.print("\n");
                }
                if (commandSequenceOut != null) System.out.print(commandSequenceOut);
            }
        }
    }
