package com.chernokoz.commands;

import com.chernokoz.Environment;

import java.io.*;
import java.util.StringJoiner;

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
        StringJoiner result = new StringJoiner("\n");
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("/bin/bash", "-c", commandString);
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
