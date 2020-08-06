package com.chernokoz;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("Welcome to shell!...");
        Environment env = new Environment();
        System.out.print("Current directory is: ");
        System.out.println(env.getCurrentDirectory());

        while (true) {

            Scanner in = new Scanner(System.in);
            System.out.print("[ Shell != Neste ] -> ");
            String str = in.nextLine();
            System.out.println("You entered string " + str);

            Lexer lexer = new Lexer(str);

            Parser parser = new Parser(lexer.run());

            for (ArrayList<Command> commandSequence : parser.run()) {
                for (Command command : commandSequence) {
                    command.execute();
                }
            }

            if (str.equals("exit")) {
                System.out.println("Closing shell...");
                break;
            }

        }
    }
}
