package com.example.project;

import java.util.Date;

class DayCounter {
    private int daysTotal;
    private Date endDate = new Date();


    DayCounter() {

    }

    void setDaysTotal(int x) {
        daysTotal = x;
    }

    int getDaysTotal() {
        return daysTotal;
    }

    int getDaysLeft() {
        long dif = endDate.getTime() - System.currentTimeMillis();
        return (millisToDays(dif));
    }

    int millisToDays(long millis) {
        long sec = millis / 1000;
        long min = sec / 60;
        long hrs = min / 60;
        return ((int) (hrs / 24) + 1);
    }

    int getDaysPassed() {
        return getDaysTotal() - getDaysLeft();
    }

    void setEndDate(Date date) {
        endDate = date;
    }
}