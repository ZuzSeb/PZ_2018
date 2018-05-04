package com.zuzseb.learning.exception;

public class WrongActualPasswordException extends Exception {
    public WrongActualPasswordException(String msg) {
        super(msg);
    }
}
