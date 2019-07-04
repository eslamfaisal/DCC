package com.ibnsaad.thedcc;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetStatusOfDoctorOpenedOrClosed {


    @Test
    public void printStatus() {

        int[] doctorDays = new int[]{0, 2, 4, 6};
        int[] fromHour = new int[]{5, 6, 7, 8};
        int[] toHour = new int[]{10, 12, 22, 16};
        System.out.println(GetStatusOfDoctorOpenedOrClosed(doctorDays, fromHour, toHour));
    }

    private String GetStatusOfDoctorOpenedOrClosed(int[] doctorDays, int[] fromHour, int[] toHour) {

        String message = null;
        String currenDate = getCurrentDayAndHour();
        String[] parts = currenDate.split(",");
        int hour = Integer.parseInt(parts[0]);
        String day = parts[1];


            System.out.println(day);

        for (int i = 0; i < doctorDays.length - 1; i++) {
            if (isValidDay(doctorDays[i]) && isValidHour(fromHour[i]) && isValidHour(toHour[i])) {
                if (getDay().get(doctorDays[i]).contains(day)) {
                    if (hour >= fromHour[i] && hour <= toHour[i]) {
                        message = "Opened: Closes at " + toHour[i] + " PM";
                    } else {
                        if (i + 1 <= doctorDays.length - 1) {
                            message = "Closed: Opens at " + getDay().get(i + 1) + " " + fromHour[i] + " PM";
                        } else {
                            message = "Closed: Opens at " + getDay().get(0) + " " + fromHour[i] + " PM";
                        }
                    }
                }


            }
        }


        return message;
    }

    private boolean isValidDay(int day) {
        return day >= 0 && day <= 6;
    }

    private boolean isValidHour(int hour) {
        return hour >= 1 && hour <= 24;
    }

    private String getCurrentDayAndHour() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("k,E,u");
        Date now = new Date();
        return sdfDate.format(now);
    }

    private List<String> getDay() {
        List<String> days = new ArrayList<>();
        days.add("Saturday");
        days.add("Sunday");
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        return days;
    }

    private List<String> getDayFromDate() {
        List<String> days = new ArrayList<>();
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        days.add("Saturday");
        days.add("Sunday");
        return days;
    }

}
