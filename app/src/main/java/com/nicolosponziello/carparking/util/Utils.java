package com.nicolosponziello.carparking.util;

import android.net.Uri;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    /**
     * formatta un tipo di dato Date in modo che sia leggibile
     * @param date
     * @return stringa della data
     */
    public static String formatDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY HH:mm", Locale.ITALIAN);
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    /**
     * crea un uri che permette di aprire l'applicazione di maps per la destinazione (lat, lon)
     * @param lat latitudine destinazione
     * @param lon longitudine destinazione
     * @return uri da condividere
     */
    public static Uri getMapsUrlFromLocation(double lat, double lon){
        Uri activityUri = Uri.parse("http://maps.google.com?daddr=" + lat + "," + lon);
        return activityUri;
    }
}
