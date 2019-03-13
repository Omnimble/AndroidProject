package com.example.project;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
class User implements Serializable {
    private int type;
    private int amount;
    private int strength;
    private int timespan;
    private int daysTotal;
    private int daysLeft;
    private int dayAmount;
    private int adjustedDayAmount;
    private int amountUsedToday = 0;
    private int totalUsed = 0;
    private long millisLeftOver;
    private long timer;
    private long interval = 0;
    private long timeOnDestroy;
    private boolean timerDestroyed = false;
    private Date endDate = new Date();
    private Date currentDate = new Date();

    User() {

    }

    void setType(int x) {
        type = x;
    }

    int getType() {
        return type;
    }

    void setAmount(int x) {
        amount = x;
    }

    int getAmount() {
        if (amount == 0) {
            return 10;
        } else return amount;
    }

    void setStrength(int x) {
        strength = x;
    }

    int getStrength() {
        return strength;
    }

    void setTimespan(int x) {
        timespan = x;
    }

    int getTimespan() {
        return timespan;
    }
}