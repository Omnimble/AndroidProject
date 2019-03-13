package com.example.project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.Gson;

public class Statistics extends AppCompatActivity {
    User user = new User();
    DayCounter dayc = new DayCounter();
    Calculator calc = new Calculator(user, dayc);
    Timer timer = new Timer(calc);
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        loadPrefs();

        TextView s1 = findViewById(R.id.stat1);
        TextView s2 = findViewById(R.id.stat2);
        TextView s3 = findViewById(R.id.stat3);
        TextView stop = findViewById(R.id.statTop);

        s1.setText(String.valueOf(calc.getAmountUsedToday()));
        s2.setText(String.valueOf(calc.getTotalUsed()));
        s3.setText(String.valueOf(calc.getTotalUnused()));

        if (user.getType() == 1) {
            stop.setText(R.string.statTop1);
        } else {
            stop.setText(R.string.statTop2);
        }
    }

    void loadPrefs() {
        // Ladataan jsonit prefeistä ja muunnetaan ne takaisin olioiksi
        SharedPreferences userDetails = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String userJson = userDetails.getString("userObject", "");
        String daycJson = userDetails.getString("daycObject", "");
        String calcJson = userDetails.getString("calcObject", "");
        String timerJson = userDetails.getString("timerObject", "");
        user = gson.fromJson(userJson, User.class);
        dayc = gson.fromJson(daycJson, DayCounter.class);
        calc = gson.fromJson(calcJson, Calculator.class);
        timer = gson.fromJson(timerJson, Timer.class);
    }
}