package com.example.project;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
class Timer implements Serializable {
    private DayCounter dayc;
    private NicotineCalculator calc;

    private Date currentDate = new Date();
    private long millisLeftOver;
    private long timer;
    private long interval = 0;
    private long timeOnDestroy;
    private boolean timerDestroyed = false;

    Timer(DayCounter dayc, NicotineCalculator calc) {
        this.dayc = dayc;
        this.calc = calc;
    }

    long getTimerGap() {
        dayc.setCurrentDate();
        return currentDate.getTime() - getTimeOnDestroyed();
    }

    long getInterval() {
        if (calc.getAdjustedDailyAmount() == 0) {
            interval = 0;
        }
        return interval;
    }

    void setInterval() {
        if (!timerDestroyed) {
            long millInDay = 57600000;
            interval = (millInDay / (long) (calc.getAdjustedDailyAmount() + calc.getAmountUsedToday())) + getMillisLeftOver();
        } else {
            timerDestroyed = false;
            interval = (getTimer() - getTimerGap());
        }
    }

    long getMillisLeftOver() {
        if (millisLeftOver >= 0) {
            return millisLeftOver;
        } else {
            return 0;
        }
    }

    void setMillisLeftOver(long x) {
        millisLeftOver = x;
    }

    void setTimer(long x) {
        timer = x;
    }

    long getTimer() {
        return timer;
    }

    void setTimerDestroyed(boolean x) {
        timerDestroyed = x;
    }

    void setTimeOnDestroyed() {
        timeOnDestroy = dayc.getCurrentDate().getTime();
    }

    long getTimeOnDestroyed() {
        return timeOnDestroy;
    }
}
