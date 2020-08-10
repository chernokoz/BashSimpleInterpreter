package com.chernokoz;

import java.util.ArrayList;

public class Parser {

    ArrayList<Token> tokenList;

    public Parser(ArrayList<Token> tokenList) {
        this.tokenList = tokenList;
    }

    public ArrayList<ArrayList<Command>> run() throws CommandNotFoundException {
        ArrayList<ArrayList<Command>> result = new ArrayList<>();

        ArrayList<Command> currentSequence = new ArrayList<>();

        String currentCommand = null;
        ArrayList<String> args = new ArrayList<>();

        for (Token token : tokenList) {

            if (token instanceof WhiteSpaceToken) {
                continue;
            }

            if (currentCommand == null) {
                if (token instanceof ReservedWordToken) {
                    currentCommand = token.getToken();
                } else {
                    throw new CommandNotFoundException("command not found: " + token.getToken());
                }
                continue;
            }

            if (token.getToken().equals("|")) {
                currentSequence.add(Command.createCommandInstance(currentCommand, args, false));
                args = new ArrayList<>();
                currentCommand = null;
                continue;
            }

            if (token.getToken().equals(";")) {
                currentSequence.add(Command.createCommandInstance(currentCommand, args, true));
                result.add(currentSequence);
                args = new ArrayList<>();
                currentCommand = null;
                currentSequence = new ArrayList<>();
                continue;
            }

            args.add(token.getToken());
        }

        currentSequence.add(Command.createCommandInstance(currentCommand, args, true));

        result.add(currentSequence);

        return result;
    }
}
