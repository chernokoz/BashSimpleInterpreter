package com.chernokoz.commands;

import com.chernokoz.*;
import com.chernokoz.exceptions.ExitException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for commands
 */

public abstract class Command {

    /**
     * command input
     */
    private String in = null;

    /**
     * command output
     */
    private String out = null;

    /**
     * command arguments
     */
     private List<String> arguments = null;

     public Command(ArrayList<String> arguments) {
         this.arguments = arguments;
     }

    /**
     * put output of previous command to input of this command
     * @param in output of previous command
     */
    public void putIn(String in) {
        this.in = in;
    }

    public String getIn() {
        return in;
    }
    public String getOut() {
        return out;
    }

    public void putOut(String in) {
        this.out = in;
    }

    public List<String> getArgs() {
        return arguments;
    }

    /**
     * calculate command output, update env
     * @throws ExitException for send a exit command to main
     * @throws IOException for file IO
     */
    public abstract void execute() throws ExitException, IOException;

    /**
     * factory method to create command instance
     * @param command command value
     * @param args command args
     * @param isLastFlag is last in command sequence
     * @param env environment
     * @return command instance
     */
    public static Command createCommandInstance(String command, ArrayList<String> args, boolean isLastFlag, Environment env) {
        return switch (command) {
            case "echo" -> new CommandEcho(args);
            case "cd" -> new CommandCd(args);
            case "pwd" -> new CommandPwd(args, env);
            case "cat" -> new CommandCat(args,env);
            case "exit" -> new CommandExit(args, isLastFlag);
            case "wc" -> new CommandWc(args, env);
            default -> null;
        };
    }

}
