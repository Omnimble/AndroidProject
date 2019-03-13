package com.example.project;

class Calculator {
    private User user;
    private DayCounter dayc;
    private int dailyAmount;
    private int adjustedDailyAmount;
    private int amountUsedToday;
    private int totalUsed;

    Calculator(User user, DayCounter dayc) {
        this.user = user;
        this.dayc = dayc;
    }

    // Käytä nikotiinia
    void useNow() {
        if (adjustedDailyAmount > 0) {
            amountUsedToday += 1;
            totalUsed += 1;
            setAdjustedDailyAmount();
        }
    }

    void resetAmountUsedToday() {
        amountUsedToday = 0;
    }

    int getAmountUsedToday() {
        return amountUsedToday;

    }

    // Käyttäjän alkuperäinen nikotiinin kulutus päivässä
    void setDailyAmount() {
        if (user.getStrength() == 3 && user.getType() == 2) {
            dailyAmount = (int) ((double) user.getAmount() * 1.5 * 14 / 7);
        } else {
            dailyAmount = (int) ((double) user.getAmount() * 1.5 * 20 / 7);
        }
    }

    int getAdjustedDailyAmount() {
        if (adjustedDailyAmount >= 0) {
            return adjustedDailyAmount;
        } else {
            return 0;
        }
    }

    // Loppupäiväksi laskettu nikotiiniannos määrä
    void setAdjustedDailyAmount() {
        adjustedDailyAmount = dailyAmount - (int) (((double) dayc.getDaysPassed() / (double) dayc.getDaysTotal()) * (double) dailyAmount) - amountUsedToday;
    }

    // Montako nikotiiniannosta käyttänyt
    int getTotalUsed() {
        return totalUsed;
    }

    // Montako nikotiiniannosta vielä edessä
    int getTotalUnused() {
        int daysPassedNow = dayc.getDaysPassed();
        int daysTotal = dayc.getDaysTotal();
        int adjustedDailyAmount;
        int totalUnused = 0;
        for (int i = daysPassedNow; i < daysTotal; i++) {
           adjustedDailyAmount = dailyAmount - (int) (((double) daysPassedNow / (double) daysTotal) * (double) dailyAmount);
           totalUnused += adjustedDailyAmount;
        }
        return totalUnused;
    }
}
