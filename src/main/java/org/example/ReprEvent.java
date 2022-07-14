package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Implements the class ReprEvent used to represent a TicketMaster event as a Java class.
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

public class ReprEvent {

  public String name;
  public String type;
  public String id;
  public String url;
  private String city;
  public String locale;
  private String dates;
  // public String test;
  // public String images;
  // public String distance;
  // public String units;
  // public String sales;
  private int weekday;

  public String getDates() {
    return dates;
  }

  public String getCity() {
    return city;
  }
  public int getWeekday() { return weekday; }

  // public String classifications;
  // public String outlets;
  // public String seatmap;
  // public String _links;
  // public String _embedded;

  public ReprEvent() {
    this.name = "";
    this.type = "";
    this.id = "";
    this.url = "";
    this.city = "";
    this.locale = "";
    this.dates = "";
  }

  public ReprEvent(String name, String type, String id, String url, String city, String locale, String dates) {
    this.name = name;
    this.type = type;
    this.id = id;
    this.url = url;
    this.city = city;
    this.locale = locale;
    this.dates = dates;
  }

  public static Comparator<ReprEvent> eventCityComparator = new Comparator<ReprEvent>() {
    @Override
    public int compare(ReprEvent e1, ReprEvent e2) {
      return (int) (e1.getCity().compareTo(e2.getCity()));
    }
  };

  public static Comparator<ReprEvent> eventWeekdayComparator = new Comparator<ReprEvent>() {
    @Override
    public int compare(ReprEvent e1, ReprEvent e2) {
      return (e1.getWeekday() < e2.getWeekday() ? -1 : e1.getWeekday() > e2.getWeekday() ? 1 : 0);
    }
  };

  public static Comparator<ReprEvent> eventCityWeekdayComparator = new Comparator<ReprEvent>() {
    @Override
    public int compare(ReprEvent e1, ReprEvent e2) {
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

  /**
   * parse method of the ReprEvent class.
   * <p>
   * takes as input a JSONObject.
   * <p>
   * gives as output a Java ReprEvent object.
   *
   * @param oJsonEvent      JSONObject to be parsed into a Java ReprEvent instance.
   * @since           1.0
   */

  public void parse(JSONObject oJsonEvent) {
    this.id = (String) oJsonEvent.get("id");
    // System.out.printf("    id: %s \n", this.id);
    this.name = (String) oJsonEvent.get("name");
    // System.out.printf("    name: %s \n", this.name);
    this.type = (String) oJsonEvent.get("type");
    // System.out.printf("    type: %s \n", this.type);
    this.locale = (String) oJsonEvent.get("locale");
    // System.out.printf("    locale: %s \n", this.locale);
    this.url = (String) oJsonEvent.get("url");
    // System.out.printf("    url: %s \n", this.url);
    JSONObject oJsonEmbedded = (JSONObject) oJsonEvent.get("_embedded");
    JSONArray oJsonVenues = (JSONArray) oJsonEmbedded.get("venues");
    JSONObject oJsonVenue = (JSONObject) oJsonVenues.get(0);
    JSONObject oJsonCity = (JSONObject) oJsonVenue.get("city");
    this.city = (String) oJsonCity.get("name");
    // System.out.printf("    city: %s \n", this.city);
    JSONObject oJsonDates = (JSONObject) oJsonEvent.get("dates");
    JSONObject oJsonStart = (JSONObject) oJsonDates.get("start");
    this.dates = (String) oJsonStart.get("localDate");
    try {
      Date date = new SimpleDateFormat("yyyy-MM-dd").parse(this.dates);
      this.weekday = date.getDay();
    } catch (ParseException e) {
      System.out.printf("ReprEvent::parse -- ParseException -- " + e.toString() + " \n");
      throw new RuntimeException(e);
    }
    // System.out.printf("    date: %s - weekday: %4d \n", this.dates, this.weekday);
  }

  /**
   * format method of the ReprEvent class.
   * <p>
   * takes as intput *this* Java ReprEvent object.
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

    // System.out.printf("%s \n", oJsonEvent.toString());

    return oJsonEvent;
  }

}
