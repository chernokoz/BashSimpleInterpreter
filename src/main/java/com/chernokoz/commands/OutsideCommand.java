package com.chernokoz.commands;

import com.chernokoz.Environment;

import java.io.*;
import java.util.StringJoiner;

/**
 * class for command needs to run outside
 */
public class OutsideCommand extends Command {

    Environment environment;
    String commandString;

    public OutsideCommand(String commandString, Environment env) {
        super(null);
        this.commandString = commandString;
        environment = env;
    }

    @Override
    public void execute() throws IOException {
        StringJoiner result = new StringJoiner(System.lineSeparator());

        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");

        ProcessBuilder builder = new ProcessBuilder().redirectError(ProcessBuilder.Redirect.INHERIT);
        if (isWindows) {
            builder.command("cmd.exe", "/c", commandString);
        } else {
            builder.command("sh", "-c", commandString);
        }
        builder.directory(new File(environment.getCurrentDirectory()));
        Process process = builder.start();

        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        while ((line = br.readLine()) != null) {
            result.add(line);
        }

        putOut(result.toString());
    }
}
