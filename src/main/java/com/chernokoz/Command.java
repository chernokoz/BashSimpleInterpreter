package com.chernokoz;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Abstract class for commands
 */

public abstract class Command {

    /**
     * command input
     */
    String in = null;

    /**
     * command output
     */
    String out = null;

    /**
     * command arguments
     */
    ArrayList<String> arguments = null;

    /**
     * put output of previous command to input of this command
     * @param in output of previous command
     */
    public void putIn(String in) {
        this.in = in;
    }

    /**
     * calculate command output, update env
     * @throws ExitException for send a exit command to main
     * @throws IOException for file IO
     */
    public abstract void execute() throws Exception;

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
            case "grep" -> new CommandGrep(args);
            default -> null;
        };
    }

}
