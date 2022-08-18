package org.example.parser;

import org.example.event.ReprEvent;
import org.example.exception.TickMastException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
/**
 * Implements the class ReprEventParser used to parse events datas.
 * <p>
 * in this class it is implemented the @JsonToEventParse
 * <p>
 *
 * @author      Giovanni Patriarca, UNIVPM
 * @since 1.0
 * @see org.example
 */

public class ReprEventParser {

    final private ArrayList<ReprEvent> maoReprEventsParsed = new ArrayList<>();

    public ArrayList<ReprEvent> getReprEventsArray() {
        return maoReprEventsParsed;
    }

    /**
     * method to parse a JSON Object into an array of ReprEvents.
     * <p>
     *
     * @param oJsonRoot      JSON Object which is the root of the server response to the REST request.
     *                       The JSON Object contains the list of events.
     *                       The output is an array of ReprEvents corresponding to the array of JSON events.
     * @since           1.0
     */

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
