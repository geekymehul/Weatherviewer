package com.developinggeek.weatherviewer;

/**
 * Created by DELL-PC on 12/29/2016.
 */
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
public class Weather {
    public final String dayofweek;
    public final String mintemp;
    public final String maxtemp;
    public final String humidity;
    public final String description;
    public final String iconURL;

    public Weather(long timestamp, double mintemp, double maxtemp, double humidity,
                   String description, String iconName)
    {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(0);

        this.dayofweek = convertTimestampToDay(timestamp);
        this.mintemp = numberFormat.format(mintemp);
        this.maxtemp = numberFormat.format(maxtemp);
        this.humidity = NumberFormat.getPercentInstance().format(humidity / 100);
        this.description = description;
        this.iconURL = "http://openweathermap.org/img/w/"+ iconName + ".png";
    }

    private String convertTimestampToDay(long timestamp) {
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(timestamp*1000);
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND,tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat dateformatter = new SimpleDateFormat("EEEE");
        return dateformatter.format(calendar.getTime());
    }

}
