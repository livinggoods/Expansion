package com.expansion.lg.kimaru.expansion.other;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kimaru on 4/5/17.
 */

public class DisplayDate {

    Long epoch;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat timeFormatter = new SimpleDateFormat("hh/mm/a");
    SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy hh/mm/a");

    // public DisplayDate (){}
    public DisplayDate(Long epoch){
        this.epoch = epoch;
    }

    public String dateOnly() {
        Date date = new Date (this.epoch);
        return dateFormatter.format(date);
    }
    public String timeOnly() {
        Date date = new Date (this.epoch);
        return timeFormatter.format(date);
    }

    public String dateAndTime() {
        Date date = new Date (this.epoch);
        return dateTimeFormatter.format(date);
    }
}
