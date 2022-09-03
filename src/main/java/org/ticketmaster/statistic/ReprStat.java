package org.ticketmaster.statistic;

import org.ticketmaster.event.ReprEvent;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Implements the class ReprStat used to represent an entry in a table of statistical frequencies for TicketMaster events.
 * <br>
 * Includes the "cty" and "weekday" members, which are the event fields used for statistics.
 * <br>
 * Includes the "frequency" member, which is the event field frequency used for statistics.
 * <br>
 * implements the static methods to compute the ReprEvent statistics:
 * @StatCity provides statistics sorted by alphabetically city (provides frequency)
 * @StatWeekday provides statistics sorted by week day (provides frequency)
 * @StatCityWeekday provides statistics alphabetically sorted by city, provides minimal frequency, maximal frequency,each day frequency, the average of the amount of events in a week
 *
 * @author      Giovanni Patriarca, UNIVPM
 * @since 1.0
 * @see org.ticketmaster
 */

public class ReprStat {

    public static final String[] asWeekdays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    private String city;
    public String getCity() { return city;}
    public void setCity(String s) { city = s; }
    private String weekday;
    public String getWeekday() { return weekday;}
    public void setWeekday(String s) { weekday = s; }

    private int frequency;
    public int getFrequency() { return frequency; }
    public void setFrequency(int i) { frequency = i; }

    public JSONObject format() {
        JSONObject oJsonStat = new JSONObject();
        oJsonStat.put("city", this.city);
        oJsonStat.put("weekday", this.weekday);
        oJsonStat.put("frequency", this.frequency);

        return oJsonStat;
    }

}
