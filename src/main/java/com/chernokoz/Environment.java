package com.chernokoz;

import java.util.HashMap;
import java.util.StringJoiner;

public class Environment {

    private final HashMap<String, String> vars = new HashMap();

    public String getCurrentDirectory() {
        String currentDirectory = "";
        return currentDirectory;
    }

    public boolean isVar(String identifier) {
        return vars.containsKey(identifier);
    }

    public void putVar(String identifier, String value) {
        vars.put(identifier, value);
    }

    public String getVar(String identifier) {
        return vars.get(identifier);
    }

}
