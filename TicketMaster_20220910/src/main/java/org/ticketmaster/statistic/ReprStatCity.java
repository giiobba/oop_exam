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
 * @ReprStatCity provides statistics sorted by alphabetically city (provides frequency)
 *
 * @author      Giovanni Patriarca, UNIVPM
 * @since 1.0
 * @see org.ticketmaster
 */

public class ReprStatCity extends ReprStat {

    /**
     * compute statistics of ReprEvents based on the filed City.
     * <p>
     * takes as input an array of ReprEvent.
     * <p>
     * gives as output a JSONArray, that is an array of JSONObject.
     *
     * @param aoRepEvent      JSONObject to be parsed into a Java RepEvent instance.
     * @since           1.0
     */

    public static JSONArray StatCity(ArrayList<ReprEvent> aoRepEvent) throws UnsupportedEncodingException {
        ArrayList<ReprStat> aoStatCity = new ArrayList<ReprStat>();

        String sCity = aoRepEvent.get(0).getCity();
        ReprStat oStat = new ReprStat();
        oStat.setCity(sCity);
        oStat.setWeekday("any");
        oStat.setFrequency(1);
        aoStatCity.add(oStat);

        boolean found = false;
        for (int i = 1; i < aoRepEvent.size(); i++) {
            found = false;
            sCity = aoRepEvent.get(i).getCity();
            for (int j = 0; j < aoStatCity.size(); j++) {
                if (sCity.equals(aoStatCity.get(j).getCity())) {
                    aoStatCity.get(j).setFrequency(aoStatCity.get(j).getFrequency() + 1);
                    found = true;
                    break;
                }
            }
            if (!found) {
                oStat = new ReprStat();
                oStat.setCity(sCity);
                oStat.setWeekday("any");
                oStat.setFrequency(1);
                aoStatCity.add(oStat);
                found = true;
            }
        }

        int k = 0;
        int iMin = 100;
        int iMax = 0;
        int iTot = 0;
        float fAvg = 0.0f;
        for (int i = 0; i < aoStatCity.size(); i++)
        {
            oStat = aoStatCity.get(i);
            String sCityUtf8 = new String(oStat.getCity().getBytes("ISO-8859-1"), "UTF-8");
            System.out.printf("Table %4d - City %s - Freq %4d \n", i, sCityUtf8, oStat.getFrequency());
            k = oStat.getFrequency();
            iTot += k;
            if (k < iMin) iMin = k;
            if (k > iMax) iMax = k;
        }
        fAvg = (float) iTot / (float) aoStatCity.size();

        JSONArray oJsonArray =  new JSONArray();

        JSONObject oJsonStat = new JSONObject();
        oJsonStat.put("field", "city");
        oJsonStat.put("min", iMin);
        oJsonStat.put("max", iMax);
        oJsonStat.put("sum", iTot);
        oJsonStat.put("avg", fAvg);

        System.out.printf("%s \n", oJsonStat.toString());

        oJsonArray.add(oJsonStat);

        for (int i = 0; i < aoStatCity.size(); i++)
        {
            oStat = aoStatCity.get(i);
            JSONObject oJsonCity = new JSONObject();
            oJsonCity.put("city", oStat.getCity());
            oJsonCity.put("frequency", oStat.getFrequency());
            oJsonArray.add(oJsonCity);
        }

        return oJsonArray;
    }

}
