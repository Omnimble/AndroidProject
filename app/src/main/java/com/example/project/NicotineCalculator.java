package com.example.project;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
class NicotineCalculator implements Serializable {
    private User user;
    private DayCounter dayc;

    private int dailyAmount;
    private int adjustedDailyAmount;
    private int amountUsedToday = 0;
    private int totalUsed = 0;
    private Date endDate = new Date();
    private Date currentDate = new Date();

    NicotineCalculator(User user, DayCounter dayc) {
        this.user = user;
        this.dayc = dayc;
    }

    void useNow() {
        amountUsedToday += 1;
        totalUsed +=1;
        setAdjustedDailyAmount();
    }

    int getTotalUsed() {
        return totalUsed;
    }

    void resetAmountUsedToday() {
        amountUsedToday = 0;
    }

    int getAmountUsedToday() {
        if (amountUsedToday >= 0) {
            return amountUsedToday;
        } else {
            return 0;
        }

    }

    void setDaílyAmount() {

        if (user.getStrength() == 3 && user.getType() == 2) {
            dailyAmount = (int) ((double) user.getAmount() * 1.5 * 14 / 7);
        } else {
            dailyAmount = (int) ((double) user.getAmount() * 1.5 * 20 / 7);
        }
    }

    int getDailyAmount() {
        return dailyAmount;
    }

    int getAdjustedDailyAmount() {
        if (adjustedDailyAmount >= 0) {
            return adjustedDailyAmount;
        } else {
            return 0;
        }
    }

    void setAdjustedDailyAmount() {
        if (getDailyAmount() == getAmountUsedToday()) {
            adjustedDailyAmount = 0;
        } else {
            adjustedDailyAmount = getDailyAmount() - (int) (((double)dayc.getDaysPassed() / (double)dayc.getDaysTotal()) * (double) getDailyAmount()) - getAmountUsedToday();
        }
    }
}
