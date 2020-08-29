package com.chernokoz;

import com.chernokoz.commands.Command;
import com.chernokoz.commands.OutsideCommand;
import com.chernokoz.tokens.ReservedWordToken;
import com.chernokoz.tokens.Token;
import com.chernokoz.tokens.WhiteSpaceToken;

import java.util.ArrayList;

/**
 * class needed for parse token list and create command sequences
 */
public class Parser {

    /**
     * tokenList from lexer
     */
    ArrayList<Token> tokenList;

    /**
     * environment of command interpreter
     */
    private final Environment env;

    public Parser(ArrayList<Token> tokenList, Environment env) {
        this.tokenList = tokenList;
        this.env = env;
    }

    /**
     * method for parse one command sequence, ended with ";"
     * @param sequenceTokenList token list of this sequence
     * @return list of command if this sequence, divided with "|"
     */
    public ArrayList<Command> parseSequence(ArrayList<Token> sequenceTokenList) {
        ArrayList<Command> result = new ArrayList<>();
        String currentCommand = null;
        ArrayList<String> args = new ArrayList<>();
        boolean doubleQuoteFlag = false;
        boolean singleQuoteFlag = false;
        Token token;
        String tokenValue;

        for (int i = 0; i < sequenceTokenList.size(); i++) {

            token = sequenceTokenList.get(i);
            tokenValue = token.getToken();

            if (tokenValue.equals("\"") && !singleQuoteFlag) {
                doubleQuoteFlag = !doubleQuoteFlag;
            }

            if (tokenValue.equals("'") && !doubleQuoteFlag) {
                singleQuoteFlag = !singleQuoteFlag;
            }

            if (tokenValue.equals("$")
                    && !singleQuoteFlag
                    && i < sequenceTokenList.size() - 1
                    && !(sequenceTokenList.get(i + 1) instanceof WhiteSpaceToken)) {
                Token identifier = sequenceTokenList.get(i + 1);
                sequenceTokenList.remove(i);
                sequenceTokenList.remove(i);
                if (env.isVar(identifier.getToken())) {
                    String value = env.getVar(identifier.getToken());
                    sequenceTokenList.add(i, Token.createToken(value));
                } else {
                    sequenceTokenList.add(i, Token.createToken(""));
                }
            }
        }

        ArrayList<Token> unquotedSequenceTokenList = new ArrayList<>();
        StringBuilder currentWord = new StringBuilder();
        singleQuoteFlag = doubleQuoteFlag = false;
        boolean isQuoteToken;
        boolean isInQuote;
        boolean isInnerQuote;

        for (Token item : sequenceTokenList) {

            token = item;
            tokenValue = token.getToken();
            isInQuote = doubleQuoteFlag || singleQuoteFlag;
            isQuoteToken = tokenValue.equals("\"") || tokenValue.equals("'");
            isInnerQuote = singleQuoteFlag && tokenValue.equals("\"")
                    || doubleQuoteFlag && tokenValue.equals("'");

            if (isInQuote && !isQuoteToken || isInnerQuote) {
                currentWord.append(token.getToken());
                continue;
            }

            if (!isInQuote && !isQuoteToken) {
                unquotedSequenceTokenList.add(token);
                continue;
            }

            if (!isInQuote) {
                if (tokenValue.equals("'")) {
                    singleQuoteFlag = true;
                }
                if (tokenValue.equals("\"")) {
                    doubleQuoteFlag = true;
                }
                continue;
            }

            if (singleQuoteFlag) {
                unquotedSequenceTokenList.add(Token.createToken(currentWord.toString()));
                currentWord = new StringBuilder();
                singleQuoteFlag = false;
                continue;
            }

            unquotedSequenceTokenList.add(Token.createToken(currentWord.toString()));
            currentWord = new StringBuilder();
            doubleQuoteFlag = false;

        }

        sequenceTokenList = unquotedSequenceTokenList;
        boolean isOutsideCommand = false;
        StringBuilder outsideCommand = new StringBuilder();

        for (int i = 0; i < sequenceTokenList.size(); i++) {

            token = sequenceTokenList.get(i);
            tokenValue = token.getToken();

            if (isOutsideCommand) {
                if (tokenValue.equals("|")) {
                    result.add(new OutsideCommand(outsideCommand.toString(), env));
                    isOutsideCommand = false;
                } else {
                    outsideCommand.append(tokenValue);
                }
                continue;
            }

            if (tokenValue.equals("\"") && !singleQuoteFlag) {
                doubleQuoteFlag = !doubleQuoteFlag;
            }

            if (token instanceof WhiteSpaceToken) {
                continue;
            }

            if (currentCommand == null) {
                if (i + 1 < sequenceTokenList.size()
                        && sequenceTokenList.get(i + 1).getToken().equals("=")) {
                    String value =(i + 2 < sequenceTokenList.size()) ? sequenceTokenList.get(i + 2).getToken() : "";
                    env.putVar(token.getToken(), value);
                    i += 2;
                    continue;
                } else if (token instanceof ReservedWordToken) {
                    currentCommand = token.getToken();
                    continue;
                } else {
                    outsideCommand.append(tokenValue);
                    isOutsideCommand = true;
                    continue;
                }
            }

            if (token.getToken().equals("|")) {
                result.add(Command.createCommandInstance(currentCommand, args, false, env));
                args = new ArrayList<>();
                currentCommand = null;
                continue;
            }

            args.add(token.getToken());
        }

        if (currentCommand != null) {
            result.add(Command.createCommandInstance(currentCommand, args, true, env));
        }

        if (isOutsideCommand) {
            result.add(new OutsideCommand(outsideCommand.toString(), env));
        }

        return result;
    }

    /**
     * method for parse command for commands sequences, divided by ";",
     * parse them one by one with parseSequence method and get list of
     * lists of commands, divided with "|", where lists of commands
     * are divided with ";"
     * @return list of lists of commands
     */
    public ArrayList<ArrayList<Command>> run() {
        ArrayList<ArrayList<Command>> result = new ArrayList<>();
        ArrayList<Token> currentSequenceTokens = new ArrayList<>();

        boolean doubleQuoteFlag = false;
        boolean singleQuoteFlag = false;

        for (Token token : tokenList) {
            if (token.getToken().equals("\"")) {
                doubleQuoteFlag = !doubleQuoteFlag;
            }
            if (token.getToken().equals("'")) {
                singleQuoteFlag = !singleQuoteFlag;
            }
            if (!doubleQuoteFlag && !singleQuoteFlag && token.getToken().equals(";")) {
                result.add(parseSequence(currentSequenceTokens));
                currentSequenceTokens = new ArrayList<>();
                continue;
            }
            currentSequenceTokens.add(token);
        }

        if (!currentSequenceTokens.isEmpty()) {
            result.add(parseSequence(currentSequenceTokens));
        }

        return result;
    }
}
