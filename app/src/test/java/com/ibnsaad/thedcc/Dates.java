package com.ibnsaad.thedcc;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Dates {


    @Test
    public void DateTest(){


        String desiredStringFormat = "yyyy-MM-dd HH:mm:ss.zzZ";

        SimpleDateFormat outputFormat = new SimpleDateFormat(desiredStringFormat);

        try {

            System.out.println("\t eslam last "+outputFormat.format(new Date("2019-06-23 15:34:04.63")));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


    }

    public static long getTimeStampFromDateTime(String mDateTime, String mDateFormat) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(mDateFormat);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = dateFormat.parse(mDateTime);
        return date.getTime();
    }
}
