package org.example.filter;

import org.example.event.ReprEvent;
import org.example.exception.TickMastException;
import org.example.statistic.ReprStat;

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
 * @see org.example
 */

public class ReprEventFilter {

    private static final int DATA_FIELD_NONE = 0;
    private static final int STAT_FIELD_NONE = 0;
    private static final int DATA_FIELD_CITY = 11;
    private static final int DATA_FIELD_WEEKDAY = 12;
    private static final int DATA_FIELD_CITY_WEEKDAY = 13;
    private static final int STAT_FIELD_CITY = 21;
    private static final int STAT_FIELD_WEEKDAY = 22;
    private static final int STAT_FIELD_CITY_WEEKDAY = 23;

    final private String[] masStatArgs = { "city", "weekday" };
    final private ArrayList<ReprEvent> maoReprEventsFiltered = new ArrayList<ReprEvent>();
    // final private ArrayList<JSONObject> maoJsonEventsFiltered = new ArrayList<JSONObject>();
    final private JSONArray maoJsonEventsFiltered = new JSONArray();

    JSONObject moStat;
    JSONArray moStatArray;
    private int miDataType = DATA_FIELD_NONE;
    private int miStatType = STAT_FIELD_NONE;

    public ArrayList<ReprEvent> getReprEventsArray() {
        return maoReprEventsFiltered;
    }
    public JSONArray getJsonEventsArray() {
        return maoJsonEventsFiltered;
    }

    /**
     * method to copy the input array of ReprEvents to the output array of filtered ReprEvents.
     * <p>
     * no filtering is performed.
     * all input ReprEvents are just copied to the output filtered ReprEvents.
     * <p>
     *
     * @param aoEvents      array of ReprEvent to be copied to the output maoReprEventsFiltered and maoJsonEventsFiltered.
     * @since           1.0
     */

    public void EventToJsonCopy(ArrayList<ReprEvent> aoEvents)
    {
        // System.out.printf("**** ReprEventFilter::EventToJsonCopy -- %s \n", aoEvents.toString());
        maoReprEventsFiltered.clear();
        maoJsonEventsFiltered.clear();
        for (ReprEvent oEvent : aoEvents) {
            maoReprEventsFiltered.add(oEvent);
            maoJsonEventsFiltered.add(oEvent.format());
        }
        System.out.printf("EventToJsonCopy -- %4d \n", maoJsonEventsFiltered.size());
        // System.out.printf(".... %s \n", maoJsonEventsFiltered.toString());
    }

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

    public void EventToJsonFilter(ArrayList<ReprEvent> aoEvents, HashMap<String, String> args) throws TickMastException, UnsupportedEncodingException {
        boolean bFilterCity = false;
        boolean bFilterWeekday = false;
        String sCity = "";
        String sWeekday = "";

        if (args.containsKey("city")) {
            bFilterCity = true;
            sCity = args.get("city");
            System.out.printf("FILTER ON CITY %s \n", sCity);
        }
        if (args.containsKey("weekday")) {
            bFilterWeekday = true;
            sWeekday = args.get("weekday");
            System.out.printf("FILTER ON WEEKDAY %s \n", sWeekday);
        }
        miDataType = DATA_FIELD_NONE;
        if (bFilterCity)
        {
            if (bFilterWeekday) {
                miDataType = DATA_FIELD_CITY_WEEKDAY;
            }
            else {
                miDataType = DATA_FIELD_CITY;
            }
        }
        else if (bFilterWeekday) {
            if (bFilterCity) {
                miDataType = DATA_FIELD_CITY_WEEKDAY;
            }
            else {
                miDataType = DATA_FIELD_WEEKDAY;
            }
        }
        else { // no filter &city or &weekday
            // throw new TickMastException("ReprEventFilter::EventToJsonFilter -- filter EMPTY");
        }

        System.out.printf("#### FILTER ON %4d \n", miDataType);

        maoReprEventsFiltered.clear();
        maoJsonEventsFiltered.clear();
        if (miDataType == DATA_FIELD_CITY) {
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
        } else if (miDataType == DATA_FIELD_WEEKDAY) {
            System.out.printf("**** FILTER ON WEEKDAY %s \n", sWeekday);
            sWeekday = sWeekday.toLowerCase();
            int k = -1;
            for (int i = 0; i < ReprStat.asWeekdays.length; i++) {
                System.out.printf("@@@@ match weekday %4d / %s / %s \n", i, ReprStat.asWeekdays[i], sWeekday);
                if (ReprStat.asWeekdays[i].toLowerCase().equals(sWeekday)) {
                    k = i;
                }
            }
            if (k >= 0 && k <= 6) {
                for (int i = 0; i < aoEvents.size(); i++) {
                    ReprEvent oEvent = aoEvents.get(i);
                    if (oEvent.getWeekday() == k) {
                        // System.out.printf("Event %4d \n", i);
                        maoReprEventsFiltered.add(oEvent);
                        maoJsonEventsFiltered.add(oEvent.format());
                    }
                }
            } else {
                for (int i = 0; i < aoEvents.size(); i++) {
                    ReprEvent oEvent = aoEvents.get(i);
                    // System.out.printf("Event %4d \n", i);
                    maoReprEventsFiltered.add(oEvent);
                    maoJsonEventsFiltered.add(oEvent.format());
                }
            }
        } else if (miDataType == DATA_FIELD_CITY_WEEKDAY) {
            System.out.printf("**** FILTER ON CITY %s && WEEKDAY %s \n", sCity, sWeekday);
            sCity = sCity.toLowerCase();
            sWeekday = sWeekday.toLowerCase();
            int k = -1;
            for (int i = 0; i < ReprStat.asWeekdays.length; i++) {
                System.out.printf("@@@@ match weekday %4d / %s / %s \n", i, ReprStat.asWeekdays[i], sWeekday);
                if (ReprStat.asWeekdays[i].toLowerCase().equals(sWeekday)) {
                    k = i;
                }
            }
            if (k >= 0 && k <= 6) {
                for (int i = 0; i < aoEvents.size(); i++) {
                    ReprEvent oEvent = aoEvents.get(i);
                    if (oEvent.getWeekday() == k && oEvent.getCity().toLowerCase().contains(sCity)) {
                        // System.out.printf("Event %4d \n", i);
                        maoReprEventsFiltered.add(oEvent);
                        maoJsonEventsFiltered.add(oEvent.format());
                    }
                }
            } else {
                for (int i = 0; i < aoEvents.size(); i++) {
                    ReprEvent oEvent = aoEvents.get(i);
                    if (oEvent.getCity().toLowerCase().contains(sCity)) {
                        // System.out.printf("Event %4d \n", i);
                        maoReprEventsFiltered.add(oEvent);
                        maoJsonEventsFiltered.add(oEvent.format());
                    }
                }
            }
        } else if (miDataType == DATA_FIELD_NONE) {
            for (int i = 0; i < aoEvents.size(); i++) {
                ReprEvent oEvent = aoEvents.get(i);
                maoReprEventsFiltered.add(oEvent);
                maoJsonEventsFiltered.add(oEvent.format());
            }
        }

        System.out.printf("EventToJsonFilter -- %4d \n", maoJsonEventsFiltered.size());
        System.out.printf(".... %s \n", maoJsonEventsFiltered.toString());

        return;
    }

    /**
     * method to compute statistics on the input array of ReprEvents.
     * <p>
     * performs statistics according to the arguments passed as HahMap (city and/or weekday).
     * <p>
     *
     * @param aoEvents      array of ReprEvent to be filtered to the output maoReprEventsFiltered and maoJsonEventsFiltered.
     * @param args      HashMap containing the parameters to perform the filtering.
     * @since           1.0
     */

    public JSONArray EventToJsonStatistic(ArrayList<ReprEvent> aoEvents, HashMap<String, String> args) throws TickMastException, UnsupportedEncodingException {
        boolean bFilterCity = false;
        boolean bFilterWeekday = false;

        if (args.containsKey("city")) {
            bFilterCity = true;
        }
        if (args.containsKey("weekday")) {
            bFilterWeekday = true;
        }
        miStatType = STAT_FIELD_NONE;
        if (bFilterCity)
        {
            if (bFilterWeekday) {
                miStatType = STAT_FIELD_CITY_WEEKDAY;
            }
            else {
                miStatType = STAT_FIELD_CITY;
            }
        }
        else if (bFilterWeekday) {
            if (bFilterCity) {
                miStatType = STAT_FIELD_CITY_WEEKDAY;
            }
            else {
                miStatType = STAT_FIELD_WEEKDAY;
            }
        }
        else { // no filter &city or &weekday
            throw new TickMastException("ReprEventFilter::EventToJsonFilter -- filter EMPTY");
        }

        if (miStatType == STAT_FIELD_CITY) {
            Collections.sort(aoEvents, ReprEvent.eventCityComparator);
            // for (int i = 0; i < aoEvents.size(); i++) {
            // System.out.printf("Event %4d -- city %s \n", i, aoEvents.get(i).getCity());
            // }
            moStatArray = ReprStat.StatCity(aoEvents);
        } else if (miStatType == STAT_FIELD_WEEKDAY) {
            Collections.sort(aoEvents, ReprEvent.eventWeekdayComparator);
            // for (int i = 0; i < aoEvents.size(); i++) {
            // System.out.printf("Event %4d -- weekday %s \n", i, aoEvents.get(i).getWeekday());
            // }
            moStatArray = ReprStat.StatWeekday(aoEvents);
        } else if (miStatType == STAT_FIELD_CITY_WEEKDAY) {
            Collections.sort(aoEvents, ReprEvent.eventCityWeekdayComparator);
            // for (int i = 0; i < aoEvents.size(); i++) {
            // System.out.printf("Event %4d -- city %s weekday %s \n", i, aoEvents.get(i).getCity(), aoEvents.get(i).getWeekday());
            // }
            moStatArray = ReprStat.StatCityWeekday(aoEvents);
        }

        maoReprEventsFiltered.clear();
        maoJsonEventsFiltered.clear();
        for (ReprEvent oEvent : aoEvents) {
            maoReprEventsFiltered.add(oEvent);
            maoJsonEventsFiltered.add(oEvent.format());
        }
        System.out.printf("EventToJsonStatistic -- %4d \n", maoJsonEventsFiltered.size());
        System.out.printf(".... %s \n", maoJsonEventsFiltered.toString());

        return moStatArray;
    }

}
