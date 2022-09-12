package org.ticketmaster.filter;

import org.ticketmaster.event.ReprEvent;
import org.ticketmaster.exception.TickMastException;
import org.ticketmaster.exception.TickMastExceptionFilter;
import org.ticketmaster.statistic.ReprStat;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

/**
 * Implements the class ReprEventFilter used to implement filters for TicketMaster events.
 * <p>
 * The @EventToJsonCopy method handles the case when no filters are requested, so it returns simply a list of events.
 * <br>
 * The @EventToJsonFilter method handles the cases when the two valid filters are provided,
 * i.e. "&city" and/or "&weekday" managing three cases:
 * <ul>
 *     <li> filter by "city": the list of events is ordered by city name,
 *     and then min, max, avg number of events per city is computed calling @ReprStat.StatCity; </li>
 *     <li> filter by "weekday": the list of events is ordered by weekday (0 = sunday ... 6 = saturday),
 *     and then min, max, avg number of events per weekday is computed calling ReprStat.StatWeekday; </li>
 *     <li> filter by "city and weekday": the list of events is ordered by city name,
 *     then, for each city, the number of events per weekday is computed,
 *     and the min, max, avg number of events per city and per weekday is computed callin @ReprStat.StatCityWeekday; </li>
 * </ul>
 * In each case, a JSON object containing the statistics is created and returned
 * to be included in the HTTTP response, which is then displayed in the web page.
 *
 * @author      Giovanni Patriarca, UNIVPM
 * @since 1.0
 * @see org.ticketmaster
 */

public class ReprEventFilterCity extends ReprEventFilter {

    /**
     * method to filter the input array of ReprEvents to the output array of filtered ReprEvents.
     * <p>
     * performs filtering according to the arguments passed as HahMap (city and/or weekday).
     * <p>
     *
     * @param aoEvents      array of ReprEvent to be filtered to the output maoReprEventsFiltered and maoJsonEventsFiltered.
     * @param args      HashMap containing the parameters to perform the filtering.
     * @since           1.0
     */

    public void EventToJsonFilterCity(ArrayList<ReprEvent> aoEvents, String sCity) throws TickMastExceptionFilter, UnsupportedEncodingException {
        maoReprEventsFiltered.clear();
        maoJsonEventsFiltered.clear();

        System.out.printf("**** FILTER ON CITY %s \n", sCity);
        sCity = sCity.toLowerCase();
        for (int i = 0; i < aoEvents.size(); i++) {
            ReprEvent oEvent = aoEvents.get(i);
            if (oEvent.getCity().toLowerCase().contains(sCity)) {
                // System.out.printf("Event %4d \n", i);
                maoReprEventsFiltered.add(oEvent);
                maoJsonEventsFiltered.add(oEvent.format());
            }
        }

        System.out.printf("EventToJsonFilterCity -- %4d \n", maoJsonEventsFiltered.size());
        System.out.printf(".... %s \n", maoJsonEventsFiltered.toString());

        return;
    }

}
