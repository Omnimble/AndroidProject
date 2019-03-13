package com.example.project;

import java.util.Calendar;

class Timer {
    private Calculator calc;
    private long timer;
    private long interval = 0;
    private long previousTimeMillis;
    private boolean timerWasDestroyed = false;

    Timer(Calculator calc) {
        this.calc = calc;
    }

    long getInterval() {
        if (calc.getAdjustedDailyAmount() == 0) {
            interval = 0;
        }
        return interval;
    }

    void setInterval() {
        if (!timerWasDestroyed) {
            if (calc.getAmountUsedToday() == 0) {
                Calendar mn = Calendar.getInstance();
                mn.add(Calendar.DAY_OF_MONTH, 1);
                mn.set(Calendar.HOUR_OF_DAY, 0);
                mn.set(Calendar.MINUTE, 0);
                mn.set(Calendar.SECOND, 0);
                mn.set(Calendar.MILLISECOND, 0);
                long timeTillMidnight = (mn.getTimeInMillis() - System.currentTimeMillis());
                interval = (timeTillMidnight / (long) (calc.getAdjustedDailyAmount())) + timer;
            } else {
                long millInDay = 57600000;
                interval = (millInDay / (long) (calc.getAdjustedDailyAmount() + calc.getAmountUsedToday())) + timer;
            }
        } else if (calc.getAmountUsedToday() == 0) {
            timerWasDestroyed = false;
            interval = (timer - (System.currentTimeMillis() - previousTimeMillis));
        } else {
            timerWasDestroyed = false;
            interval = (timer - (System.currentTimeMillis() - previousTimeMillis)) + interval;
        }
    }

    void setTimer(long x) {
        timer = x;
        previousTimeMillis = System.currentTimeMillis();
    }

    void setTimerWasDestroyed() {
        timerWasDestroyed = true;
    }
}
