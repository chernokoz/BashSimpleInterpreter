package com.chernokoz;

import java.util.ArrayList;

public class Parser {

    ArrayList<Token> tokenList;

    public Parser(ArrayList<Token> tokenList) {
        this.tokenList = tokenList;
    }

    public ArrayList<ArrayList<Command>> run() throws Exception {
        ArrayList<ArrayList<Command>> result = new ArrayList<>();

        ArrayList<Command> currentSequence = new ArrayList<>();

        String currentCommand = null;
        ArrayList<String> args = new ArrayList<>();

        for (Token token : tokenList) {

            if (currentCommand == null) {
                if (token instanceof ReservedWordToken) {
                    currentCommand = token.getToken();
                } else {
                    throw new Exception("command not found: " + token.getToken());
                }
            }


            if (token.getToken().equals("|")) {
                currentSequence.add(Command.createCommandInstance(currentCommand, args));
            }

            if (token.getToken().equals(";")) {
                result.add(currentSequence);
            }
        }


        return result;
    }
}
