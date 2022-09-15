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
 * @ReprStatWeekday provides statistics sorted by week day (provides frequency)
 *
 * @author      Giovanni Patriarca, UNIVPM
 * @since 1.0
 * @see org.ticketmaster
 */

public class ReprStatWeekday extends ReprStat {

    public static JSONArray StatWeekday(ArrayList<ReprEvent> aoRepEvent) throws UnsupportedEncodingException {
        ArrayList<ReprStat> aoStatWeekday = new ArrayList<ReprStat>();

        int iWeekday = aoRepEvent.get(0).getWeekday();
        ReprStat oStat = new ReprStat();
        oStat.setWeekday(asWeekdays[iWeekday]);
        oStat.setCity("any");
        oStat.setFrequency(1);
        aoStatWeekday.add(oStat);

        boolean found = false;
        for (int i = 1; i < aoRepEvent.size(); i++) {
            found = false;
            iWeekday = aoRepEvent.get(i).getWeekday();
            for (int j = 0; j < aoStatWeekday.size(); j++) {
                if (asWeekdays[iWeekday].equals(aoStatWeekday.get(j).getWeekday())) {
                    aoStatWeekday.get(j).setFrequency(aoStatWeekday.get(j).getFrequency() + 1);
                    found = true;
                    break;
                }
            }
            if (!found) {
                oStat = new ReprStat();
                oStat.setWeekday(asWeekdays[iWeekday]);
                oStat.setCity("any");
                oStat.setFrequency(1);
                aoStatWeekday.add(oStat);
                found = true;
            }
        }

        int k = 0;
        int iMin = 100;
        int iMax = 0;
        int iTot = 0;
        float fAvg = 0;
        for (int i = 0; i < aoStatWeekday.size(); i++)
        {
            oStat = aoStatWeekday.get(i);
            String sWeekdayUtf8 = new String(oStat.getWeekday().getBytes("ISO-8859-1"), "UTF-8");
            System.out.printf("Table %4d - Weekday %s - Freq %4d \n", i, sWeekdayUtf8, oStat.getFrequency());
            k = oStat.getFrequency();
            iTot += k;
            if (k < iMin) iMin = k;
            if (k > iMax) iMax = k;
        }
        fAvg = (float) iTot / (float) aoStatWeekday.size();

        JSONArray oJsonArray =  new JSONArray();

        JSONObject oJsonStat = new JSONObject();
        oJsonStat.put("field", "weekday");
        oJsonStat.put("min", iMin);
        oJsonStat.put("max", iMax);
        oJsonStat.put("sum", iTot);
        oJsonStat.put("avg", fAvg);

        System.out.printf("%s \n", oJsonStat.toString());

        oJsonArray.add(oJsonStat);

        for (int i = 0; i < aoStatWeekday.size(); i++)
        {
            oStat = aoStatWeekday.get(i);
            JSONObject oJsonWeekday = new JSONObject();
            oJsonWeekday.put("weekday", oStat.getWeekday());
            oJsonWeekday.put("frequency", oStat.getFrequency());
            oJsonArray.add(oJsonWeekday);
        }

        return oJsonArray;
    }

}
