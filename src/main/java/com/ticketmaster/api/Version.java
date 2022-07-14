package com.ticketmaster.api;

/**
 * Implements the Version class for the TicketMaster Discovery Api.
 *
 * @since 1.0
 * @see com.ticketmaster.api
 */

public class Version {

  public static final String SDK_VERSION = "0.1.8";

  public static final String getUserAgent() {
    return "Ticketmaster Discovery Java SDK/" + SDK_VERSION;
  }

  private Version() {

  }

}
