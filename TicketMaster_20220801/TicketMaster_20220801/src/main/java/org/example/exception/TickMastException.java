package org.example.exception;

public class TickMastException extends Exception {

    public TickMastException(String param)
    {
        super(param.toString());
    }

}
