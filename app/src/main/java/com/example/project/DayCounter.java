package com.example.project;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("serial")
class DayCounter implements Serializable {

    private int daysTotal;
    private Date endDate = new Date();
    private Date currentDate = new Date();

    DayCounter() {

    }

    void setDaysTotal(int x) {
        daysTotal = x;
    }

    int getDaysTotal() {
        return daysTotal;
    }

    int getDaysLeft() {
        setCurrentDate();
        long dif = endDate.getTime() - getCurrentDate().getTime();
        long sec = dif / 1000;
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

    void setCurrentDate() {
        currentDate = Calendar.getInstance().getTime();
    }

    Date getCurrentDate() {
        return currentDate;
    }
}