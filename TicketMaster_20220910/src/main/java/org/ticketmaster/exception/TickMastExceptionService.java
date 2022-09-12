package org.ticketmaster.exception;

import org.ticketmaster.exception.TickMastException;

public class TickMastExceptionService extends Exception {

    public TickMastExceptionService(String param)
    {
        super(param.toString());
    }

}
