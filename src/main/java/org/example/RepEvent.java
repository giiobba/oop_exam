package org.example;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import org.example.RepStat;

/**
 * Implements the class RepEvent used to represent a TicketMaster event as a Java class.
 * <p>
 * Includes the most significant fields of the JSON event as Java variables.
 * <p>
 * Implements the "get" methods used by the application.
 * <br>
 * Implements the two Comparators used for ordering by "city" and by "weekday".
 * <br>
 * Implements the two methods to convert from JSON to Java (parse) and from Java to JSON (format).
 *
 * @author      Giovanni Patriarca, UNIVPM
 * @since 1.0
 * @see org.example
 */

public class RepEvent {
  public static final String[] asWeekdays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

  public String name;
  public String type;
  public String id;
  public String test;
  public String url;
  public String locale;
  public String images;
  public String distance;
  public String units;
  public String sales;
  private String dates;
  public String getDates() {
    return dates;
  }

  private int weekday;
  public int getWeekday() { return weekday; }

  public String classifications;
  public String outlets;
  public String seatmap;
  public String _links;
  public String _embedded;
  private String city;
  public String getCity() {
    return city;
  }

  public static Comparator<RepEvent> eventCityComparator = new Comparator<RepEvent>() {
    @Override
    public int compare(RepEvent e1, RepEvent e2) {
      return (int) (e1.getCity().compareTo(e2.getCity()));
    }
  };

  public static Comparator<RepEvent> eventWeekdayComparator = new Comparator<RepEvent>() {
    @Override
    public int compare(RepEvent e1, RepEvent e2) {
      return (e1.getWeekday() < e2.getWeekday() ? -1 : e1.getWeekday() > e2.getWeekday() ? 1 : 0);
    }
  };

  public static Comparator<RepEvent> eventCityWeekdayComparator = new Comparator<RepEvent>() {
    @Override
    public int compare(RepEvent e1, RepEvent e2) {
      if (e1.getCity().compareTo(e2.getCity()) < 0) {
        return -1;
      }
      else if (e1.getCity().compareTo(e2.getCity()) > 0) {
        return 1;
      }
      else {
        return (e1.getWeekday() < e2.getWeekday() ? -1 : e1.getWeekday() > e2.getWeekday() ? 1 : 0);
      }
    }
  };

  public static JSONObject StatsCity( ArrayList<RepEvent> aoRepEvent) {
    ArrayList<RepStat> aoStatCity = new ArrayList<RepStat>();

    String sCity = aoRepEvent.get(0).getCity();
    RepStat oStat = new RepStat();
    oStat.setField1(sCity);
    oStat.setFrequency(1);
    aoStatCity.add(oStat);

    boolean found = false;
    for (int i = 1; i < aoRepEvent.size(); i++) {
      found = false;
      sCity = aoRepEvent.get(i).getCity();
      for (int j = 0; j < aoStatCity.size(); j++) {
        if (sCity.equals(aoStatCity.get(j).getField1())) {
          aoStatCity.get(j).setFrequency(aoStatCity.get(j).getFrequency() + 1);
          found = true;
          break;
        }
      }
      if (!found) {
        oStat = new RepStat();
        oStat.setField1(sCity);
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
      System.out.printf("Table %4d - City %s - Freq %4d \n", i, oStat.getField1(), oStat.getFrequency());
      k = oStat.getFrequency();
      iTot += k;
      if (k < iMin) iMin = k;
      if (k > iMax) iMax = k;
    }
    fAvg = (float) iTot / (float) aoStatCity.size();

    JSONObject oJsonEvent = new JSONObject();
    oJsonEvent.put("field", "city");
    oJsonEvent.put("min", iMin);
    oJsonEvent.put("max", iMax);
    oJsonEvent.put("sum", iTot);
    oJsonEvent.put("avg", fAvg);

    System.out.printf("%s \n", oJsonEvent.toString(2));

    return oJsonEvent;
  }

  public static JSONObject StatsWeekday( ArrayList<RepEvent> aoRepEvent)
  {
    ArrayList<RepStat> aoStatWeekday = new ArrayList<RepStat>();

    int iWeekday = aoRepEvent.get(0).getWeekday();
    RepStat oStat = new RepStat();
    oStat.setField1(asWeekdays[iWeekday]);
    oStat.setFrequency(1);
    aoStatWeekday.add(oStat);

    boolean found = false;
    for (int i = 1; i < aoRepEvent.size(); i++) {
      found = false;
      iWeekday = aoRepEvent.get(i).getWeekday();
      for (int j = 0; j < aoStatWeekday.size(); j++) {
        if (asWeekdays[iWeekday].equals(aoStatWeekday.get(j).getField1())) {
          aoStatWeekday.get(j).setFrequency(aoStatWeekday.get(j).getFrequency() + 1);
          found = true;
          break;
        }
      }
      if (!found) {
        oStat = new RepStat();
        oStat.setField1(asWeekdays[iWeekday]);
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
      System.out.printf("Table %4d - Weekday %s - Freq %4d \n", i, oStat.getField1(), oStat.getFrequency());
      k = oStat.getFrequency();
      iTot += k;
      if (k < iMin) iMin = k;
      if (k > iMax) iMax = k;
    }
    fAvg = (float) iTot / (float) aoStatWeekday.size();

    JSONObject oJsonEvent = new JSONObject();
    oJsonEvent.put("field", "weekday");
    oJsonEvent.put("min", iMin);
    oJsonEvent.put("max", iMax);
    oJsonEvent.put("sum", iTot);
    oJsonEvent.put("avg", fAvg);

    System.out.printf("%s \n", oJsonEvent.toString(2));

    return oJsonEvent;
  }

  public static JSONObject StatsCityWeekday( ArrayList<RepEvent> aoRepEvent) {
    ArrayList<RepStat> aoStatCity = new ArrayList<RepStat>();

    String sCity = aoRepEvent.get(0).getCity();
    int iWeekday = aoRepEvent.get(0).getWeekday();
    RepStat oStat = new RepStat();
    oStat.setField1(sCity);
    oStat.setField2(asWeekdays[iWeekday]);
    oStat.setFrequency(1);
    aoStatCity.add(oStat);

    boolean found = false;
    for (int i = 1; i < aoRepEvent.size(); i++) {
      found = false;
      sCity = aoRepEvent.get(i).getCity();
      iWeekday = aoRepEvent.get(i).getWeekday();
      for (int j = 0; j < aoStatCity.size(); j++) {
        if (sCity.equals(aoStatCity.get(j).getField1()) && asWeekdays[iWeekday].equals(aoStatCity.get(j).getField2())) {
          aoStatCity.get(j).setFrequency(aoStatCity.get(j).getFrequency() + 1);
          found = true;
          break;
        }
      }
      if (!found) {
        oStat = new RepStat();
        oStat.setField1(sCity);
        oStat.setField2(asWeekdays[iWeekday]);
        oStat.setFrequency(1);
        aoStatCity.add(oStat);
        found = true;
      }
    }

    for (int i = 0; i < aoStatCity.size(); i++) {
      System.out.printf("Table entry %4d -- city %s -- weekday %s -- frequency %4d \n", i, aoStatCity.get(i).getField1(), aoStatCity.get(i).getField2(), aoStatCity.get(i).getFrequency());
    }

    int k = 0;
    int iMin = 100;
    int iMax = 0;
    int iTot = 0;
    float fAvg = 0;
    RepStat oStatMin = new RepStat();
    RepStat oStatMax = new RepStat();
    String sWeekday;
    int aiFrequency[] = new int[7];
    sCity = aoStatCity.get(0).getField1();
    aiFrequency[0] = aiFrequency[1] = aiFrequency[2] = aiFrequency[3] = aiFrequency[4] = aiFrequency[5] = aiFrequency[6] = 0;
    for (int i = 0; i < aoStatCity.size(); ) {
      iMin = 100;
      iMax = 0;
      iTot = 0;
      fAvg = 0.0f;
      while (i < aoStatCity.size() && aoStatCity.get(i).getField1().equals(sCity))
      {
        sWeekday = aoStatCity.get(i).getField2();
        switch (sWeekday)
        {
          case "Sunday" -> {
            k = aiFrequency[0] = aoStatCity.get(i).getFrequency();
            iTot += k;
            if (k < iMin) {
              iMin = k;
              oStatMin.setField1(aoStatCity.get(i).getField1());
              oStatMin.setField2(aoStatCity.get(i).getField2());
              oStatMin.setFrequency(aoStatCity.get(i).getFrequency());
            }
            if (k > iMax) {
              iMax = k;
              oStatMax.setField1(aoStatCity.get(i).getField1());
              oStatMax.setField2(aoStatCity.get(i).getField2());
              oStatMax.setFrequency(aoStatCity.get(i).getFrequency());
            }
          }
          case "Monday" ->  {
            k = aiFrequency[1] = aoStatCity.get(i).getFrequency();
            iTot += k;
            if (k < iMin) {
              iMin = k;
              oStatMin.setField1(aoStatCity.get(i).getField1());
              oStatMin.setField2(aoStatCity.get(i).getField2());
              oStatMin.setFrequency(aoStatCity.get(i).getFrequency());
            }
            if (k > iMax) {
              iMax = k;
              oStatMax.setField1(aoStatCity.get(i).getField1());
              oStatMax.setField2(aoStatCity.get(i).getField2());
              oStatMax.setFrequency(aoStatCity.get(i).getFrequency());
            }
          }
          case "Tuesday" ->  {
            k = aiFrequency[2] = aoStatCity.get(i).getFrequency();
            iTot += k;
            if (k < iMin) {
              iMin = k;
              oStatMin.setField1(aoStatCity.get(i).getField1());
              oStatMin.setField2(aoStatCity.get(i).getField2());
              oStatMin.setFrequency(aoStatCity.get(i).getFrequency());
            }
            if (k > iMax) {
              iMax = k;
              oStatMax.setField1(aoStatCity.get(i).getField1());
              oStatMax.setField2(aoStatCity.get(i).getField2());
              oStatMax.setFrequency(aoStatCity.get(i).getFrequency());
            }
          }
          case "Wednesday" ->  {
            k = aiFrequency[3] = aoStatCity.get(i).getFrequency();
            iTot += k;
            if (k < iMin) {
              iMin = k;
              oStatMin.setField1(aoStatCity.get(i).getField1());
              oStatMin.setField2(aoStatCity.get(i).getField2());
              oStatMin.setFrequency(aoStatCity.get(i).getFrequency());
            }
            if (k > iMax) {
              iMax = k;
              oStatMax.setField1(aoStatCity.get(i).getField1());
              oStatMax.setField2(aoStatCity.get(i).getField2());
              oStatMax.setFrequency(aoStatCity.get(i).getFrequency());
            }
          }
          case "Thursday" ->  {
            k = aiFrequency[4] = aoStatCity.get(i).getFrequency();
            iTot += k;
            if (k < iMin) {
              iMin = k;
              oStatMin.setField1(aoStatCity.get(i).getField1());
              oStatMin.setField2(aoStatCity.get(i).getField2());
              oStatMin.setFrequency(aoStatCity.get(i).getFrequency());
            }
            if (k > iMax) {
              iMax = k;
              oStatMax.setField1(aoStatCity.get(i).getField1());
              oStatMax.setField2(aoStatCity.get(i).getField2());
              oStatMax.setFrequency(aoStatCity.get(i).getFrequency());
            }
          }
          case "Friday" ->  {
            k = aiFrequency[5] = aoStatCity.get(i).getFrequency();
            iTot += k;
            if (k < iMin) {
              iMin = k;
              oStatMin.setField1(aoStatCity.get(i).getField1());
              oStatMin.setField2(aoStatCity.get(i).getField2());
              oStatMin.setFrequency(aoStatCity.get(i).getFrequency());
            }
            if (k > iMax) {
              iMax = k;
              oStatMax.setField1(aoStatCity.get(i).getField1());
              oStatMax.setField2(aoStatCity.get(i).getField2());
              oStatMax.setFrequency(aoStatCity.get(i).getFrequency());
            }
          }
          case "Saturday" -> {
            k = aiFrequency[6] = aoStatCity.get(i).getFrequency();
            iTot += k;
            if (k < iMin) {
              iMin = k;
              oStatMin.setField1(aoStatCity.get(i).getField1());
              oStatMin.setField2(aoStatCity.get(i).getField2());
              oStatMin.setFrequency(aoStatCity.get(i).getFrequency());
            }
            if (k > iMax) {
              iMax = k;
              oStatMax.setField1(aoStatCity.get(i).getField1());
              oStatMax.setField2(aoStatCity.get(i).getField2());
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
          oStatMin.setField2(asWeekdays[j]);
          oStatMin.setFrequency(0);
          break;
        }
      }
      fAvg = (float) iTot / (float) 7;

      JSONObject oJsonEvent = new JSONObject();
      oJsonEvent.put("field", "city");
      oJsonEvent.put("value", sCity);
      oJsonEvent.put("min", iMin);
      oJsonEvent.put("minDay", oStatMin.format());
      oJsonEvent.put("max", iMax);
      oJsonEvent.put("maxDay", oStatMax.format());
      oJsonEvent.put("sum", iTot);
      oJsonEvent.put("avg", fAvg);

      System.out.printf("%s \n", oJsonEvent.toString(2));

      if (i == aoStatCity.size()) break;

      sCity = aoStatCity.get(i).getField1();
      aiFrequency[0] = aiFrequency[1] = aiFrequency[2] = aiFrequency[3] = aiFrequency[4] = aiFrequency[5] = aiFrequency[6] = 0;
    }

    /***
     for (int i = 0; i < aoStatCity.size(); i++)
     {
     oStat = aoStatCity.get(i);
     System.out.printf("Table %4d - City/Weekday %s - Freq %4d \n", i, oStat.getField(), oStat.getFrequency());
     k = oStat.getFrequency();
     iTot += k;
     if (k < iMin) iMin = k;
     if (k > iMax) iMax = k;
     }
     iAvg = iTot / aoStatCity.size();

     JSONObject oJsonEvent = new JSONObject();
     oJsonEvent.put("field", "city");
     oJsonEvent.put("min", iMin);
     oJsonEvent.put("max", iMax);
     oJsonEvent.put("sum", iTot);
     oJsonEvent.put("avg", iAvg);

     System.out.printf("%s \n", oJsonEvent.toString(2));
     ***/

    return null;
  }

  /**
   * parse method of the RepEvent class.
   * <p>
   * takes as input a JSONObject.
   * <p>
   * gives as output a Java RepEvent object.
   *
   * @param oJsonEvent      JSONObject to be parsed into a Java RepEvent instance.
   * @since           1.0
   */

  public void parse(JSONObject oJsonEvent) {
    try {
      this.id = oJsonEvent.getString("id");
      // System.out.printf("    id: %s \n", this.id);
    } catch (JSONException e) {
      // System.out.printf("    id: NOT FOUND \n");
    }
    try {
      this.name = oJsonEvent.getString("name");
      // System.out.printf("    name: %s \n", this.name);
    } catch (JSONException e) {
      // System.out.printf("    name: NOT FOUND \n");
    }
    try {
      this.type = oJsonEvent.getString("type");
      // System.out.printf("    type: %s \n", this.type);
    } catch (JSONException e) {
      // System.out.printf("    type: NOT FOUND \n");
    }
    try {
      this.locale = oJsonEvent.getString("locale");
      // System.out.printf("    locale: %s \n", this.locale);
    } catch (JSONException e) {
      // System.out.printf("    locale: NOT FOUND \n");
    }
    try {
      this.url = oJsonEvent.getString("url");
      // System.out.printf("    url: %s \n", this.url);
    } catch (JSONException e) {
      // System.out.printf("    url: NOT FOUND \n");
    }
    try {
      this.city = oJsonEvent.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("city").getString("name");
      // System.out.printf("    city: %s \n", this.city);
    } catch (JSONException e) {
      // System.out.printf("    city: NOT FOUND \n");
      this.city = "****";
    }
    try {
      this.dates = oJsonEvent.getJSONObject("dates").getJSONObject("start").getString("localDate");
      try {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(this.dates);
        this.weekday = date.getDay();
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }
      // System.out.printf("    date: %s - weekday: %4d \n", this.dates, this.weekday);
    } catch (JSONException e) {
      // System.out.printf("    date: NOT FOUND \n");
      this.dates = "2000-01-01";
      this.weekday = 6;
    }
  }

  /**
   * format method of the RepEvent class.
   * <p>
   * takes as intput *this* Java RepEvent object.
   * <p>
   * gives as input a JSONObject.
   *
   * @since           1.0
   */

  public JSONObject format() {
    JSONObject oJsonEvent = new JSONObject();
    oJsonEvent.put("id", this.id);
    oJsonEvent.put("name", this.name);
    oJsonEvent.put("type", this.type);
    oJsonEvent.put("url", this.url);
    oJsonEvent.put("city", this.city);
    oJsonEvent.put("date", this.dates);
    oJsonEvent.put("weekday", this.weekday);

    System.out.printf("%s \n", oJsonEvent.toString(2));

    return oJsonEvent;
  }

}
