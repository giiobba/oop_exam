package org.ticketmaster.exception;

import org.ticketmaster.exception.TickMastException;

public class TickMastExceptionFilter extends TickMastException {

    public TickMastExceptionFilter(String param)
    {
        super(param.toString());
    }

}
