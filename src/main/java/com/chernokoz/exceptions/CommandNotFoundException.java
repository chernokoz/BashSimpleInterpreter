package com.chernokoz.exceptions;

public class CommandNotFoundException extends Exception {

    String info;

    public CommandNotFoundException(String s) {
        info = s;
    }
}
