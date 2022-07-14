package org.example;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

@Service
public class TickMastService
{

    private static String msApiKey = System.getProperty("ticketmaster-api-key", "55VBfAWOGW4lK8A28ZUjV7VAYmQYX9pK");
    final private String[] masDataArgs = { "countrycode", "city", "weekday" };
    final private String[] masStatArgs = { "countrycode", "city", "weekday" };

    final private static List<String> masCountriesEu = Arrays.asList(
            "at", "be", "bg", "cy", "hr", "dk", "ee", "fi", "fr", "de", "gr", "ie", "it", "lv", "lt", "lu",
            "mt", "nl", "pl", "pt", "cz", "ro", "sk", "si", "es", "se", "hu");

    String msCountry;

    final private org.json.simple.JSONObject moJsonResponse = new JSONObject();
    final private ArrayList<ReprEvent> maoRepEvents = new ArrayList<>();

    public void checkDataArgs(HashMap<String, String> param) throws TickMastException {
        System.out.printf("**** TickMastService::checkDataArgs -- %s \n", param.toString());
        int verify = 0;
        for (String p : masDataArgs) {
            if (param.containsKey(p))
                verify++;
        }
        if (verify != param.size()) {
            System.out.printf("TickMastService::checkDataArgs -- " + param.toString() + " \n");
            throw new TickMastException("TickMastService::checkDataArgs -- " + param.toString());
        }
    }

    public void checkStatArgs(HashMap<String, String> param) throws TickMastException {
        System.out.printf("**** TickMastService::checkStatArgs -- %s \n", param.toString());
        int verify = 0;
        for (String p : masStatArgs) {
            if (param.containsKey(p))
                verify++;
        }
        if (verify != param.size()) {
            System.out.printf("TickMastService::checkDataArgs -- " + param.toString() + " \n");
            throw new TickMastException("TickMastService::checkStatArgs -- " + param.toString());
        }
    }

    public String searchData(HashMap<String, String> args) throws IOException, TickMastException
    {
        System.out.printf("**** TickMastService::searchData -- %s \n", args.toString());
        msCountry = args.get("countrycode");
        if (msCountry == null)
        {
            System.out.printf("TickMastService::searchData -- countrycode NOT PRESENT \n");
            throw new TickMastException("TickMastService::searchData -- countrycode NOT PRESENT \n");
        }
        if (!masCountriesEu.contains(msCountry))
        {
            System.out.printf("TickMastService::searchData -- countrycode NOT VALID -- %s \n", msCountry);
            throw new TickMastException("TickMastService::searchData -- countrycode NOT VALID -- " + msCountry + " \n");
        }


        // 1. Create the URL for SearchEvents
        String sUrl = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=" + msApiKey + "&countryCode=" + msCountry + "&size=100";

        // 2. Open the URL and parse the Response
        InputStream input = new URL(sUrl).openStream();
        JSONParser parser = new JSONParser();
        org.json.simple.JSONObject oJsonRoot = null;
        try {
            oJsonRoot = (org.json.simple.JSONObject) parser.parse(new InputStreamReader(input));
        } catch (ParseException e) {
            System.out.printf("TickMastService::searchData exception %s \n", e.toString());
            throw new RuntimeException(e);
        }

        if (oJsonRoot == null)
        {
            System.out.printf("TickMastService::searchData -- JSON object EMPTY \n");
            throw new TickMastException("TickMastService::searchData -- JSON object EMPTY");
        }

        // 3. Convert the JSON Objects to JAVA Objects
        ReprEventParser oReprEventParser = new ReprEventParser();
        oReprEventParser.JsonToEventParse(oJsonRoot);

        // 4. Filter the JSON Objects according to the args
        ReprEventFilter oReprEventFilter = new ReprEventFilter();
        oReprEventFilter.EventToJsonCopy(oReprEventParser.getReprEventsArray());

        moJsonResponse.clear();
        moJsonResponse.put("EventsNumber", oReprEventFilter.getJsonEventsArray().size());
        moJsonResponse.put("EventsArray", oReprEventFilter.getJsonEventsArray());

        String sHtmlBegin = "<html><body>";
        String sHtmlJson = JsonToHtml(moJsonResponse, 0, 4);
        String sHtmlJsonUtf08 = new String(sHtmlJson.getBytes("ISO-8859-1"), "UTF-8");
        // System.out.printf("JsonToHtmlUtf8 %s \n", sHtmlJsonUtf08);
        String sHtmlEnd = "</body></html>";
        return (sHtmlBegin + sHtmlJsonUtf08 + sHtmlEnd);
    }

    public String searchStat(HashMap<String, String> args) throws IOException, TickMastException
    {
        System.out.printf("**** TickMastService::searchStat -- %s \n", args.toString());
        msCountry = args.get("countrycode");
        if (msCountry == null)
        {
            System.out.printf("TickMastService::searchStat -- countrycode NOT PRESENT \n");
            throw new TickMastException("TickMastService::searchStat -- countrycode NOT PRESENT");
        }
        if (!masCountriesEu.contains(msCountry))
        {
            System.out.printf("TickMastService::searchData -- countrycode NOT VALID -- %s \n", msCountry);
            throw new TickMastException("TickMastService::searchData -- countrycode NOT VALID -- " + msCountry + " \n");
        }

        // 1. Create the URL for SearchEvents
        String sUrl = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=" + msApiKey + "&countryCode=" + msCountry + "&size=100";

        // 2. Open the URL and parse the Response
        InputStream input = new URL(sUrl).openStream();
        JSONParser parser = new JSONParser();
        org.json.simple.JSONObject oJsonRoot = null;
        try {
            oJsonRoot = (org.json.simple.JSONObject) parser.parse(new InputStreamReader(input));
        } catch (ParseException e) {
            System.out.printf("TickMastService::searchStat exception %s \n", e.toString());
            throw new RuntimeException(e);
        }

        if (oJsonRoot == null)
        {
            System.out.printf("TickMastService::searchData -- JSON object EMPTY \n");
            throw new TickMastException("TickMastService::searchData -- JSON object EMPTY");
        }

        // 3. Convert the JSON Objects to JAVA Objects
        ReprEventParser oReprEventParser = new ReprEventParser();
        oReprEventParser.JsonToEventParse(oJsonRoot);

        // 4. Filter the JSON Objects according to the args
        ReprEventFilter oReprEventFilter = new ReprEventFilter();
        JSONArray oStatArray = oReprEventFilter.EventToJsonFilter(oReprEventParser.getReprEventsArray(), args);

        moJsonResponse.clear();
        moJsonResponse.put("Statistics", oStatArray);
        moJsonResponse.put("EventsNumber", oReprEventFilter.getJsonEventsArray().size());
        moJsonResponse.put("EventsArray", oReprEventFilter.getJsonEventsArray());

        String sHtmlBegin = "<html><body>";
        String sHtmlJson = JsonToHtml(moJsonResponse, 0, 4);
        String sHtmlJsonUtf08 = new String(sHtmlJson.getBytes("ISO-8859-1"), "UTF-8");
        // System.out.printf("JsonToHtmlUtf8 %s \n", sHtmlJsonUtf08);
        String sHtmlEnd = "</body></html>";
        return (sHtmlBegin + sHtmlJsonUtf08 + sHtmlEnd);
    }

    /**
     * convert json Data to structured Html text
     *
     * @param obj
     * @return string
     */
    private String JsonToHtml(Object obj, int indent, int step) {
        StringBuilder html = new StringBuilder();
        String blanks1 = "";
        for (int i = 0; i < indent; i++) {
            blanks1 += "&nbsp;";
        }
        String blanks2 = "";
        for (int i = 0; i < step; i++) {
            blanks2 += "&nbsp;";
        }

        if (obj instanceof JSONObject) {
            JSONObject oJson = (JSONObject) obj;
            Set<Object> oSet = oJson.keySet();

            html.append(blanks1);
            html.append("{ <br>");

            if (oSet.size() > 0) {
                for (Object oKey : oSet) {
                    html.append(blanks1);
                    html.append(blanks2);
                    String sKey = oKey.toString();
                    html.append(sKey).append(" : ");
                    Object oVal = oJson.get(sKey);
                    // recursive call
                    html.append(JsonToHtml(oVal, indent + step, step));
                }
            }

            html.append(blanks1);
            html.append("} <br>");
        } else if (obj instanceof JSONArray) {
            JSONArray array = (JSONArray) obj;

            html.append("<br>");
            html.append(blanks1);
            html.append("[ <br>");

            for (int k = 0; k < array.size(); k++) {
                JSONObject oJson = (JSONObject) array.get(k);
                // recursive call
                html.append(JsonToHtml(oJson, indent + step, step));
            }

            html.append(blanks1);
            html.append("] <br>");
        } else {
            // print the value
            html.append(obj.toString());
            html.append(", <br>");
        }

        return html.toString();
    }

}
