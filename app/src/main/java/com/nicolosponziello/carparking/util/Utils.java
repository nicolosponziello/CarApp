package com.nicolosponziello.carparking.util;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String formatDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY HH:mm", Locale.ITALIAN);
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    public static Uri getMapsUrlFromLocation(double lat, double lon){
        Uri activityUri = Uri.parse("http://maps.google.com?daddr=" + lat + "," + lon);
        return activityUri;
    }
}
