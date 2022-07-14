package org.example;

import org.json.JSONObject;

/**
 * Implements the class RepStat used to represent an entry in a table of statistical frequencies for TicketMaster events.
 * <br>
 * Includes the "field" member, which is the event field used for statistics.
 * <br>
 * Includes the "frequency" member, which is the event field frequency used for statistics.
 *
 * @author      Giovanni Patriarca, UNIVPM
 * @since 1.0
 * @see org.example
 */

public class RepStat {
    private String field1;
    public String getField1() { return field1;}
    public void setField1(String s) { field1 = s; }
    private String field2;
    public String getField2() { return field2;}
    public void setField2(String s) { field2 = s; }

    private int frequency;
    public int getFrequency() { return frequency; }
    public void setFrequency(int i) { frequency = i; }

    public JSONObject format() {
        JSONObject oJsonEvent = new JSONObject();
        oJsonEvent.put("field1", this.field1);
        oJsonEvent.put("field2", this.field2);
        oJsonEvent.put("frequency", this.frequency);

        // System.out.printf("%s \n", oJsonEvent.toString(2));

        return oJsonEvent;
    }
}
