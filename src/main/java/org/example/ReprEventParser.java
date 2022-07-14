package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class ReprEventParser {

    final private ArrayList<ReprEvent> maoReprEventsParsed = new ArrayList<>();

    public ArrayList<ReprEvent> getReprEventsArray() {
        return maoReprEventsParsed;
    }

    public void JsonToEventParse(JSONObject oJsonRoot) throws TickMastException
    {
        JSONObject oJsonEmbedded = (JSONObject) oJsonRoot.get("_embedded");
        if (oJsonEmbedded == null) {
            System.out.printf("ReprEventParser::JsonToEventParse -- _embedded NOT FOUND \n");
            throw new TickMastException("ReprEventParser::JsonToEventParse -- _embedded NOT FOUND \n");
        }
        JSONArray aoJsonEvents = (JSONArray) oJsonEmbedded.get("events");
        if (aoJsonEvents == null) {
            System.out.printf("ReprEventParser::JsonToEventParse -- events NOT FOUND \n");
            throw new TickMastException("ReprEventParser::JsonToEventParse -- events NOT FOUND \n");
        }
        // System.out.printf("RepEventParser::JsonToEventParse - events %s \n", aoJsonEvents.toString());
        maoReprEventsParsed.clear();
        for (Object oObject :  aoJsonEvents) {
            JSONObject oJson = (JSONObject) oObject;
            ReprEvent oRepEvent = new ReprEvent();
            oRepEvent.parse(oJson);
            maoReprEventsParsed.add(oRepEvent);
        }
        // System.out.printf("RepEventParser::JsonToEventParse - parsed %4d \n", maoReprEventsParsed.size());
    }

}
