package com.chernokoz;

public class CommandNotFoundException extends Exception {

    String info;

    public CommandNotFoundException(String s) {
        info = s;
    }
}
