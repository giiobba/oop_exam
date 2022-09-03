package org.ticketmaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Implements the TickMastApplication class, as a @SpringBootApplication, that is an implementation
 * of the SpringBootApplication interface from the SpringBoot package "package org.springframework.stereotype".
 * <p>
 * Implements only the @main method, which in turns triggers the SpringApplication.run method
 * to launch the SpringBoot server accepting HTTP requests.
 *
 * @author      Giovanni Patriarca, UNIVPM
 * @since 1.0
 * @see org.ticketmaster
 */

@SpringBootApplication
public class TickMastApplication extends Throwable {

    public static void main(String[] args) {
        SpringApplication.run(TickMastApplication.class, args);
    }

}
