package org.example;

import com.ticketmaster.api.discovery.DiscoveryApi;
import com.ticketmaster.api.discovery.operation.SearchEventsOperation;
import com.ticketmaster.api.discovery.response.PagedResponse;
import com.ticketmaster.discovery.model.Events;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Implements the Main class for the TicketMaster application.
 * <p>
 * Step 1.
 * <br>
 * Parsing command line arguments to understand the REST command (GET).
 * The possible types of GET command are: /metadata, /data, /stats.
 * <p>
 * Step 2.
 * <br>
 * Instantiate the DiscoveryApi client to handle the request and response.
 * The authentication is base on ApiKey.
 * <p>
 * Step 3.
 * <br>
 * Make the request and get the response for the Events Search.
 * <p>
 * Step 4.
 * <br>
 * Convert the JSON Events array into a Java RepEvent array
 * <p>
 * Step 5.
 * <br>
 * Manage the different requests: /data, /stat.
 *
 * @author      Giovanni Patriarca, UNIVPM
 * @since 1.0
 * @see org.example
 */

public class Main {

    /***
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    ***/

    private static final int DATA_NO_FILTER = 110;
    private static final int DATA_FILTER = 120;
    private static final int DATA_FILTER_CITY = 121;
    private static final int DATA_FILTER_WEEKDAY = 122;
    private static final int DATA_FILTER_ERROR = 129;

    private static final int STAT_NO_FIELD = 210;
    private static final int STAT_FIELD = 220;
    private static final int STAT_FIELD_CITY = 221;
    private static final int STAT_FIELD_WEEKDAY = 222;
    private static final int STAT_FIELD_CITY_WEEKDAY = 223;
    private static final int STAT_FIELD_ERROR = 229;
    private static final int STAT_FILTER = 230;
    private static final int STAT_FILTER_CITY = 231;
    private static final int STAT_FILTER_WEEKDAY = 232;
    private static final int STAT_FILTER_ERROR = 239;

    private static String sApiKey = System.getProperty("ticketmaster-api-key", "55VBfAWOGW4lK8A28ZUjV7VAYmQYX9pK");
    private static final String LAT = "34.0522";
    private static final String LONG = "-118.2437";
    private static List<String> asCountriesEu = Arrays.asList(
            "at", "be", "bg", "cy", "hr", "dk", "ee", "fi", "fr", "de", "gr", "ie", "it", "lv", "lt", "lu",
            "mt", "nl", "pl", "pt", "cz", "ro", "sk", "si", "es", "se", "hu");

    /**
     * Main method of the Main class for the TicketMaster event search application.
     *
     * @param args      command line arguments as an array of Strings
     * @since           1.0
     */

    public static void main(String[] args) throws IOException {

        String sCity = "";
        String sWeekday = "";

        System.out.printf("TicketMaster Event Search \n");
        System.out.printf("Api Key %s \n", sApiKey);

        /***
         * Step 1.
         * Parsing command line arguments to understand the REST command (GET)
         * possible types of GET command are: /metadata, /data, /stats
         */

        System.out.printf("**** App arguments >>>> \n");

        int iRequestType = 0;

        String sRest = args[0];
        if (sRest.contains("GET")) {
            System.out.printf(".... GET call is VALID \n");
        } else {
            System.out.printf(".... GET call is *NOT* VALID -- exit \n");
            return;
        }

        String sCountry = args[1];
        System.out.printf(".... GET country code [%s] \n", sCountry);
        List<String> asCountries = Arrays.asList(sCountry.split(","));
        for (int i = 0; i < asCountries.size(); i++)
        {
            String asCountryTest = asCountries.get(i);
            if (asCountriesEu.contains(asCountryTest)) {
                System.out.printf(".... GET call with country %s is VALID \n", asCountryTest);
            } else {
                System.out.printf(".... GET call with country %s is *NOT* VALID \n", asCountryTest);
                return;
            }
        }
        /***
         ***/

        String sType = args[2];
        String sFilter = "";
        if (sType.contains("/data")) {
            System.out.printf(".... GET call for DATA \n");
            if (sType.contains("?filter=")) {
                System.out.printf(".... GET call for DATA with FILTER \n");
                String sPattern = "/data?filter=";
                int p = sType.indexOf(sPattern) + sPattern.length();
                String sJsonTemp = sType.substring(p);
                System.out.printf(".... GET call for DATA with FILTER %s \n", sJsonTemp);
                if (sJsonTemp.contains("city")) {
                    JSONObject oJsonTemp = new JSONObject(sJsonTemp);
                    sCity = oJsonTemp.getString("city");
                    System.out.printf(".... argument city %s \n", sCity);
                    iRequestType = DATA_FILTER_CITY;
                }
                else if (sJsonTemp.contains("weekday")) {
                    JSONObject oJsonTemp = new JSONObject(sJsonTemp);
                    sWeekday = oJsonTemp.getString("weekday");
                    System.out.printf(".... argument weekday %s \n", sWeekday);
                    iRequestType = DATA_FILTER_WEEKDAY;
                }
                else {
                    System.out.printf(".... argument *NOT* VALID \n");
                    iRequestType = DATA_FILTER_ERROR;
                }
            }
            else {
                System.out.printf(".... GET call for DATA *without* FILTER \n");
                iRequestType = DATA_NO_FILTER;
            }
            sType = "/data";
        } else if (sType.contains("/stats")) {
            System.out.printf(".... GET call for STATS \n");
            if (sType.contains("?field=")) {
                System.out.printf(".... GET call for STATS with FIELD \n");
                String sPattern = "/stats?field=";
                int p = sType.indexOf(sPattern) + sPattern.length();
                // String sField = sType.substring(p);
                // System.out.printf(".... GET call for STATS with FIELD %s \n", sField);
                if (sType.contains("&filter="))
                {
                    System.out.printf(".... GET call for STATS with FIELD *and* FILTER\n");
                    sPattern = "&filter=";
                    int q = sType.indexOf(sPattern) + sPattern.length();
                    String sJsonTemp = sType.substring(q);
                    String sField = sType.substring(p, sType.indexOf(sPattern));
                    System.out.printf(".... GET call for STATS with FIELD %s *and* FILTER %s \n", sField, sJsonTemp);
                    if (sJsonTemp.contains("city")) {
                        JSONObject oJsonTemp = new JSONObject(sJsonTemp);
                        sCity = oJsonTemp.getString("city");
                        System.out.printf(".... argument city %s \n", sCity);
                        iRequestType = STAT_FILTER_CITY;
                    }
                    else if (sJsonTemp.contains("weekday")) {
                        JSONObject oJsonTemp = new JSONObject(sJsonTemp);
                        sWeekday = oJsonTemp.getString("weekday");
                        System.out.printf(".... argument weekday %s \n", sWeekday);
                        iRequestType = STAT_FILTER_WEEKDAY;
                    }
                    else {
                        System.out.printf(".... argument *NOT* VALID \n");
                        iRequestType = STAT_FILTER_ERROR;
                    }
                }
                else
                {
                    System.out.printf(".... GET call for STATS with FIELD *without* FILTER\n");
                    String sField = sType.substring(p);
                    System.out.printf(".... GET call for STATS with FIELD %s \n", sField);
                    if (sField.contains("city+weekday")) {
                        System.out.printf(".... argument city+weekday \n");
                        iRequestType = STAT_FIELD_CITY_WEEKDAY;
                    }
                    else if (sField.contains("city")) {
                        System.out.printf(".... argument city \n");
                        iRequestType = STAT_FIELD_CITY;
                    }
                    else if (sField.contains("weekday")) {
                        System.out.printf(".... argument weekday \n");
                        iRequestType = STAT_FIELD_WEEKDAY;
                    }
                    else {
                        System.out.printf(".... argument *NOT* VALID \n");
                        iRequestType = STAT_FIELD_ERROR;
                    }
                }
            }
            else {
                System.out.printf(".... GET call fir STATS *without* FIELD \n");
                iRequestType = STAT_NO_FIELD;
            }
            sType = "/stats";
        } else {
            System.out.printf(".... GET call for ARG *NOT* VALID -- exit \n");
            sType = "/error";
            return;
        }

        System.out.printf("**** App arguments <<<< \n");

        /***
         * Step 2.
         * Instantiate the DiscoveryApi client to handle the request & response
         * the authentication is base on ApiKey
         */

        // 1. Instantiate a DiscoveryApi client:
        DiscoveryApi discoveryApi = new DiscoveryApi(sApiKey);
        System.out.printf("discoverApi %s \n", discoveryApi.toString());

        /***
         * Step 3.
         * Make the request and get the response for the Events Search
         */

        // 2. Make our first search
        /***
        // Adding a filter for events nearby Los Angeles
        // Asking for a maximum of 50 events per pages
        PagedResponse<Events> response =
                discoveryApi.searchEvents(new SearchEventsOperation().latlong(LAT, LONG).pageSize(50));
        ***/
        PagedResponse<Events> response =
                discoveryApi.searchEvents(new SearchEventsOperation().countryCode(sCountry).pageSize(100));

        System.out.printf("response %s \n", response.toString());
        String sJsonPayload = response.getJsonPayload();
        // System.out.printf("Json Payload %s \n", sJsonPayload);

        /***
         * Step 4.
         * Parse the response for the Events Search as a Json "string"
         */

        // json jackson (1st try)
        /***
        ObjectMapper objectMapper = new ObjectMapper();
        String sToken = "\"events\":[";
        int iOffset = sJsonPayload.indexOf(sToken) + sToken.length();
        System.out.printf("offset of events %4d \n", iOffset);
        String sJsonOffset = sJsonPayload.substring(iOffset);
        System.out.printf("response.getJsonPayload() %s \n", sJsonOffset);
        ***/

        // json org.json (2nd try)
        JSONObject oJsonRoot = new JSONObject(sJsonPayload);
        JSONObject oJsonEmbedded = oJsonRoot.getJSONObject("_embedded");
        JSONArray aoJsonEvents = oJsonEmbedded.getJSONArray("events");
        // System.out.printf("Json Payload %s \n", aoJsonEvents.toString(2));

        /***
         * Step 4.
         * Convert the JSON Events array into a Java RepEvent array
         */

        // modified code
        ArrayList<RepEvent> aoRepEvent = new ArrayList<RepEvent>();
        for (int i = 0; i < aoJsonEvents.length(); i++) {
            // System.out.printf("Event %4d \n", i);
            JSONObject oJsonEvent = aoJsonEvents.getJSONObject(i);
            // String oJsonEnventString = oJsonEvent.toString(2);
            // System.out.printf("%s \n", oJsonEnventString);
            RepEvent oRepEvent = new RepEvent();
            oRepEvent.parse(oJsonEvent);
            aoRepEvent.add(oRepEvent);
            // oRepEvent.format();
        }
        System.out.printf("**** **** Event list with %4d elements \n", aoRepEvent.size());

        /***
         * Step 5.
         * Manage the different requests: /data, /stat.
         */
        switch (iRequestType)
        {
            case DATA_NO_FILTER -> {
                // print all events as JSON objects
                for (int i = 0; i < aoRepEvent.size(); i++) {
                    System.out.printf("Event %4d \n", i);
                    aoRepEvent.get(i).format();
                }
            }

            case DATA_FILTER_CITY -> {
                // search the Array by city
                if (! sCity.equals("")) {
                    int k = 0;
                    for (int i = 0; i < aoRepEvent.size(); i++) {
                        RepEvent oRepEvent = aoRepEvent.get(i);
                        if (oRepEvent.getCity().contains(sCity)) {
                            k++;
                            System.out.printf("Event %4d \n", i);
                            aoRepEvent.get(i).format();
                        }
                    }
                    System.out.printf("**** **** Event list with %4d elements in CITY %s \n", k, sCity);
                }
            }

            case DATA_FILTER_WEEKDAY -> {
                // search the Array by weekday
                if (! sWeekday.equals("")) {
                    int k = 0;
                    int iWeekday = 0;
                    for (int i = 0; i < RepEvent.asWeekdays.length; i++) {
                        if (sWeekday.equals(RepEvent.asWeekdays[i])) iWeekday = i;
                    }
                    for (int i = 0; i < aoRepEvent.size(); i++) {
                        RepEvent oRepEvent = aoRepEvent.get(i);
                        if (oRepEvent.getWeekday() == iWeekday) {
                            k++;
                            System.out.printf("Event %4d \n", i);
                            aoRepEvent.get(i).format();
                        }
                    }
                    System.out.printf("**** **** Event list with %4d elements on WEEKDAY %s \n", k, RepEvent.asWeekdays[iWeekday]);
                }
            }

            case STAT_FIELD_CITY -> {
                Collections.sort(aoRepEvent, RepEvent.eventCityComparator);
                for (int i = 0; i < aoRepEvent.size(); i++) {
                    // System.out.printf("Event %4d -- city %s \n", i, aoRepEvent.get(i).getCity());
                }
                RepEvent.StatsCity(aoRepEvent);
            }

            case STAT_FIELD_WEEKDAY -> {
                ArrayList<RepEvent> aoRepEventByWeekday = new ArrayList<RepEvent>();
                Collections.sort(aoRepEvent, RepEvent.eventWeekdayComparator);
                for (int i = 0; i < aoRepEvent.size(); i++) {
                    // System.out.printf("Event %4d -- date %s -- weekday %4d \n", i, aoRepEvent.get(i).getDates(), aoRepEvent.get(i).getWeekday());
                }
                RepEvent.StatsWeekday(aoRepEvent);
            }

            case STAT_FIELD_CITY_WEEKDAY -> {
                ArrayList<RepEvent> aoRepEventByWeekday = new ArrayList<RepEvent>();
                Collections.sort(aoRepEvent, RepEvent.eventCityWeekdayComparator);
                for (int i = 0; i < aoRepEvent.size(); i++) {
                    // System.out.printf("Event %4d -- city %s -- date %s -- weekday %4d \n", i, aoRepEvent.get(i).getCity(), aoRepEvent.get(i).getDates(), aoRepEvent.get(i).getWeekday());
                }
                RepEvent.StatsCityWeekday(aoRepEvent);
            }

            case STAT_FILTER_CITY -> {
                Collections.sort(aoRepEvent, RepEvent.eventCityComparator);
                for (int i = 0; i < aoRepEvent.size(); i++) {
                    // System.out.printf("Event %4d -- city %s \n", i, aoRepEvent.get(i).getCity());
                }
                RepEvent.StatsCity(aoRepEvent);
                // search the Array by city
                if (! sCity.equals("")) {
                    int k = 0;
                    for (int i = 0; i < aoRepEvent.size(); i++) {
                        RepEvent oRepEvent = aoRepEvent.get(i);
                        if (oRepEvent.getCity().contains(sCity)) {
                            k++;
                            System.out.printf("Event %4d \n", i);
                            aoRepEvent.get(i).format();
                        }
                    }
                    System.out.printf("**** **** Event list with %4d elements in CITY %s \n", k, sCity);
                }
            }

            case STAT_FILTER_WEEKDAY -> {
                ArrayList<RepEvent> aoRepEventByWeekday = new ArrayList<RepEvent>();
                Collections.sort(aoRepEvent, RepEvent.eventWeekdayComparator);
                for (int i = 0; i < aoRepEvent.size(); i++) {
                    // System.out.printf("Event %4d -- date %s -- weekday %4d \n", i, aoRepEvent.get(i).getDates(), aoRepEvent.get(i).getWeekday());
                }
                RepEvent.StatsWeekday(aoRepEvent);
                // search the Array by weekday
                if (! sWeekday.equals("")) {
                    int k = 0;
                    int iWeekday = 0;
                    for (int i = 0; i < RepEvent.asWeekdays.length; i++) {
                        if (sWeekday.equals(RepEvent.asWeekdays[i])) iWeekday = i;
                    }
                    for (int i = 0; i < aoRepEvent.size(); i++) {
                        RepEvent oRepEvent = aoRepEvent.get(i);
                        if (oRepEvent.getWeekday() == iWeekday) {
                            k++;
                            System.out.printf("Event %4d \n", i);
                            aoRepEvent.get(i).format();
                        }
                    }
                    System.out.printf("**** **** Event list with %4d elements on WEEKDAY %s \n", k, RepEvent.asWeekdays[iWeekday]);
                }
            }
        }

        // System.out.printf("response.getContent() %s \n", response.getContent());
        // System.out.printf("response.getContent().getEvents() %s \n", response.getContent().getEvents());

        // 3. Let's create a beautiful ASCII art table
        // original code
        /***
        AsciiArtTable table = response.getContent().getEvents().stream().forEach(event -> {
            AsciiArtRow row = new AsciiArtRow();

            row.addCell(event.getName());
            row.addCell(event.getDates().toString());
            // row.addCell(event.getClassifications().get(0).toString());
            row.addCell(event.getAttractions().toString());

            table.addRow(row);
        });
        ***/

        // 4. And we'll print that piece of art in the console
        // System.out.println(table.toString());

    }

}

