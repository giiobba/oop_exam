package org.ticketmaster.exception;

import org.ticketmaster.exception.TickMastException;

public class TickMastExceptionParser extends Exception {

    public TickMastExceptionParser(String param)
    {
        super(param.toString());
    }

}
