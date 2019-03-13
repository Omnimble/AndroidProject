package com.example.project;

class User {
    private int type;
    private int amount;
    private int strength;
    private int timespan;

    User() {
    }

    int getType() {
        return type;
    }

    void setType(int x) {
        type = x;
    }

    int getAmount() {
        if (amount == 0) {
            return 10;
        } else return amount;
    }

    void setAmount(int x) {
        amount = x;
    }

    int getStrength() {
        return strength;
    }

    void setStrength(int x) {
        strength = x;
    }

    int getTimespan() {
        return timespan;
    }

    void setTimespan(int x) {
        timespan = x;
    }
}