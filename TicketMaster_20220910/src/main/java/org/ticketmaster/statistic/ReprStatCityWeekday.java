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

public class ReprStatCityWeekday extends ReprStat {

    public static JSONArray StatCityWeekday(ArrayList<ReprEvent> aoRepEvent) throws UnsupportedEncodingException {
        ArrayList<ReprStat> aoStatCity = new ArrayList<ReprStat>();

        String sCity = aoRepEvent.get(0).getCity();
        int iWeekday = aoRepEvent.get(0).getWeekday();
        ReprStat oStat = new ReprStat();
        oStat.setCity(sCity);
        oStat.setWeekday(asWeekdays[iWeekday]);
        oStat.setFrequency(1);
        aoStatCity.add(oStat);

        boolean found = false;
        for (int i = 1; i < aoRepEvent.size(); i++) {
            found = false;
            sCity = aoRepEvent.get(i).getCity();
            iWeekday = aoRepEvent.get(i).getWeekday();
            for (int j = 0; j < aoStatCity.size(); j++) {
                if (sCity.equals(aoStatCity.get(j).getCity()) && asWeekdays[iWeekday].equals(aoStatCity.get(j).getWeekday())) {
                    aoStatCity.get(j).setFrequency(aoStatCity.get(j).getFrequency() + 1);
                    found = true;
                    break;
                }
            }
            if (!found) {
                oStat = new ReprStat();
                oStat.setCity(sCity);
                oStat.setWeekday(asWeekdays[iWeekday]);
                oStat.setFrequency(1);
                aoStatCity.add(oStat);
                found = true;
            }
        }

        for (int i = 0; i < aoStatCity.size(); i++) {
            String sCityUtf8 = new String(aoStatCity.get(i).getCity().getBytes("ISO-8859-1"), "UTF-8");
            String sWeekdayUtf8 = new String(aoStatCity.get(i).getWeekday().getBytes("ISO-8859-1"), "UTF-8");
            System.out.printf("Table %4d -- city %s -- weekday %s -- frequency %4d \n", i, sCityUtf8, sWeekdayUtf8, aoStatCity.get(i).getFrequency());
        }

        JSONArray oJsonArray =  new JSONArray();

        int k = 0;
        int iMin = 100;
        int iMax = 0;
        int iTot = 0;
        float fAvg = 0;
        ReprStat oStatMin = new ReprStat();
        ReprStat oStatMax = new ReprStat();
        String sWeekday;
        int aiFrequency[] = new int[7];
        sCity = aoStatCity.get(0).getCity();
        aiFrequency[0] = aiFrequency[1] = aiFrequency[2] = aiFrequency[3] = aiFrequency[4] = aiFrequency[5] = aiFrequency[6] = 0;
        for (int i = 0; i < aoStatCity.size(); ) {
            iMin = 100;
            iMax = 0;
            iTot = 0;
            fAvg = 0.0f;
            while (i < aoStatCity.size() && aoStatCity.get(i).getCity().equals(sCity))
            {
                sWeekday = aoStatCity.get(i).getWeekday();
                switch (sWeekday)
                {
                    case "Sunday" -> {
                        k = aiFrequency[0] = aoStatCity.get(i).getFrequency();
                        iTot += k;
                        if (k < iMin) {
                            iMin = k;
                            oStatMin.setCity(aoStatCity.get(i).getCity());
                            oStatMin.setWeekday(aoStatCity.get(i).getWeekday());
                            oStatMin.setFrequency(aoStatCity.get(i).getFrequency());
                        }
                        if (k > iMax) {
                            iMax = k;
                            oStatMax.setCity(aoStatCity.get(i).getCity());
                            oStatMax.setWeekday(aoStatCity.get(i).getWeekday());
                            oStatMax.setFrequency(aoStatCity.get(i).getFrequency());
                        }
                    }
                    case "Monday" ->  {
                        k = aiFrequency[1] = aoStatCity.get(i).getFrequency();
                        iTot += k;
                        if (k < iMin) {
                            iMin = k;
                            oStatMin.setCity(aoStatCity.get(i).getCity());
                            oStatMin.setWeekday(aoStatCity.get(i).getWeekday());
                            oStatMin.setFrequency(aoStatCity.get(i).getFrequency());
                        }
                        if (k > iMax) {
                            iMax = k;
                            oStatMax.setCity(aoStatCity.get(i).getCity());
                            oStatMax.setWeekday(aoStatCity.get(i).getWeekday());
                            oStatMax.setFrequency(aoStatCity.get(i).getFrequency());
                        }
                    }
                    case "Tuesday" ->  {
                        k = aiFrequency[2] = aoStatCity.get(i).getFrequency();
                        iTot += k;
                        if (k < iMin) {
                            iMin = k;
                            oStatMin.setCity(aoStatCity.get(i).getCity());
                            oStatMin.setWeekday(aoStatCity.get(i).getWeekday());
                            oStatMin.setFrequency(aoStatCity.get(i).getFrequency());
                        }
                        if (k > iMax) {
                            iMax = k;
                            oStatMax.setCity(aoStatCity.get(i).getCity());
                            oStatMax.setWeekday(aoStatCity.get(i).getWeekday());
                            oStatMax.setFrequency(aoStatCity.get(i).getFrequency());
                        }
                    }
                    case "Wednesday" ->  {
                        k = aiFrequency[3] = aoStatCity.get(i).getFrequency();
                        iTot += k;
                        if (k < iMin) {
                            iMin = k;
                            oStatMin.setCity(aoStatCity.get(i).getCity());
                            oStatMin.setWeekday(aoStatCity.get(i).getWeekday());
                            oStatMin.setFrequency(aoStatCity.get(i).getFrequency());
                        }
                        if (k > iMax) {
                            iMax = k;
                            oStatMax.setCity(aoStatCity.get(i).getCity());
                            oStatMax.setWeekday(aoStatCity.get(i).getWeekday());
                            oStatMax.setFrequency(aoStatCity.get(i).getFrequency());
                        }
                    }
                    case "Thursday" ->  {
                        k = aiFrequency[4] = aoStatCity.get(i).getFrequency();
                        iTot += k;
                        if (k < iMin) {
                            iMin = k;
                            oStatMin.setCity(aoStatCity.get(i).getCity());
                            oStatMin.setWeekday(aoStatCity.get(i).getWeekday());
                            oStatMin.setFrequency(aoStatCity.get(i).getFrequency());
                        }
                        if (k > iMax) {
                            iMax = k;
                            oStatMax.setCity(aoStatCity.get(i).getCity());
                            oStatMax.setWeekday(aoStatCity.get(i).getWeekday());
                            oStatMax.setFrequency(aoStatCity.get(i).getFrequency());
                        }
                    }
                    case "Friday" ->  {
                        k = aiFrequency[5] = aoStatCity.get(i).getFrequency();
                        iTot += k;
                        if (k < iMin) {
                            iMin = k;
                            oStatMin.setCity(aoStatCity.get(i).getCity());
                            oStatMin.setWeekday(aoStatCity.get(i).getWeekday());
                            oStatMin.setFrequency(aoStatCity.get(i).getFrequency());
                        }
                        if (k > iMax) {
                            iMax = k;
                            oStatMax.setCity(aoStatCity.get(i).getCity());
                            oStatMax.setWeekday(aoStatCity.get(i).getWeekday());
                            oStatMax.setFrequency(aoStatCity.get(i).getFrequency());
                        }
                    }
                    case "Saturday" -> {
                        k = aiFrequency[6] = aoStatCity.get(i).getFrequency();
                        iTot += k;
                        if (k < iMin) {
                            iMin = k;
                            oStatMin.setCity(aoStatCity.get(i).getCity());
                            oStatMin.setWeekday(aoStatCity.get(i).getWeekday());
                            oStatMin.setFrequency(aoStatCity.get(i).getFrequency());
                        }
                        if (k > iMax) {
                            iMax = k;
                            oStatMax.setCity(aoStatCity.get(i).getCity());
                            oStatMax.setWeekday(aoStatCity.get(i).getWeekday());
                            oStatMax.setFrequency(aoStatCity.get(i).getFrequency());
                        }
                    }
                }
                i++;
            }
            for (int j = 0; j < 7; j++)
            {
                if (aiFrequency[j] == 0)
                {
                    iMin = 0;
                    oStatMin.setWeekday(asWeekdays[j]);
                    oStatMin.setFrequency(0);
                    break;
                }
            }
            fAvg = (float) iTot / (float) 7;

            JSONObject oJsonStat = new JSONObject();
            oJsonStat.put("field", "city");
            oJsonStat.put("value", sCity);
            oJsonStat.put("min", iMin);
            oJsonStat.put("minDay", oStatMin.format());
            oJsonStat.put("max", iMax);
            oJsonStat.put("maxDay", oStatMax.format());
            oJsonStat.put("sum", iTot);
            oJsonStat.put("avg", fAvg);

            oJsonArray.add(oJsonStat);

            System.out.printf("%s \n", oJsonStat.toString());

            if (i == aoStatCity.size()) break;

            sCity = aoStatCity.get(i).getCity();
            aiFrequency[0] = aiFrequency[1] = aiFrequency[2] = aiFrequency[3] = aiFrequency[4] = aiFrequency[5] = aiFrequency[6] = 0;
        }

        return oJsonArray;
    }

}
