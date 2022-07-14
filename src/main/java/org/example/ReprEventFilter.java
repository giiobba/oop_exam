package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ReprEventFilter {

    private static final int STAT_FIELD_NONE = 0;
    private static final int STAT_FIELD_CITY = 221;
    private static final int STAT_FIELD_WEEKDAY = 222;
    private static final int STAT_FIELD_CITY_WEEKDAY = 223;

    final private String[] masStatArgs = { "city", "weekday" };
    final private ArrayList<ReprEvent> maoReprEventsFiltered = new ArrayList<ReprEvent>();
    // final private ArrayList<JSONObject> maoJsonEventsFiltered = new ArrayList<JSONObject>();
    final private JSONArray maoJsonEventsFiltered = new JSONArray();

    JSONObject moStat;
    JSONArray moStatArray;
    private int miStatType = STAT_FIELD_NONE;

    public ArrayList<ReprEvent> getReprEventsArray() {
        return maoReprEventsFiltered;
    }
    public JSONArray getJsonEventsArray() {
        return maoJsonEventsFiltered;
    }

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

    public JSONArray EventToJsonFilter(ArrayList<ReprEvent> aoEvents, HashMap<String, String> args) throws TickMastException, UnsupportedEncodingException {
        // System.out.printf("**** ReprEventFilter::EventToJsonFilter -- %s \n", args.toString());
        boolean bFilterCity = false;
        boolean bFilterWeekday = false;

        if (args.containsKey("city")) {
            bFilterCity = true;
        }
        if (args.containsKey("weekday")) {
            bFilterWeekday = true;
        }
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
        System.out.printf("EventToJsonFilter -- %4d \n", maoJsonEventsFiltered.size());
        // System.out.printf(".... %s \n", maoJsonEventsFiltered.toString());

        return moStatArray;
    }

}
