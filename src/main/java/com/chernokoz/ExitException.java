package com.chernokoz;

public class ExitException extends Exception {

    String info;

    public ExitException(String fd) {
        info = fd;
    }
}
