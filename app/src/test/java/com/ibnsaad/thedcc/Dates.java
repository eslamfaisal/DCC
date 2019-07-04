package com.ibnsaad.thedcc;

import android.util.Pair;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class Dates {


    @Test
    public void DateTest(){

//
//        String desiredStringFormat = "yyyy-MM-dd HH:mm:ss.zzZ";
//
//        SimpleDateFormat outputFormat = new SimpleDateFormat(desiredStringFormat);
//
//        try {
//
//            System.out.println("\t eslam last "+outputFormat.format(new Date("2019-06-23 15:34:04.63")));
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }

        System.out.println(StringgetCurrentDayAndHour());

    }

    //return integer for the hour and string for the day
    private String  StringgetCurrentDayAndHour() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("k,E");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        System.out.println(strDate);
        String[] parts = strDate.split(",");
        String hour = parts[0]; // 004
        String day = parts[1]; // 034556
        Pair<Integer, String> pair = new Pair<>(Integer.parseInt(hour),day);
        Integer key = pair.first;
        String value = pair.second;
        return value;
    }
    public static long getTimeStampFromDateTime(String mDateTime, String mDateFormat) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(mDateFormat);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = dateFormat.parse(mDateTime);
        return date.getTime();
    }
}
