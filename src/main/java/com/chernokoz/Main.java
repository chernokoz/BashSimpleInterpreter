package com.chernokoz;

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

            Lexer lexer = new Lexer(str);

            if (str.equals("exit")) {
                System.out.println("Closing shell...");
                break;
            }
        }
    }
}
