package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import com.google.gson.Gson;
import java.util.Calendar;
import java.util.Date;

public class Settings extends AppCompatActivity {

    User user = new User();
    DayCounter dayc = new DayCounter();
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final SharedPreferences userDetails = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final SharedPreferences.Editor edit = userDetails.edit();

        boolean userFirstLogin = userDetails.getBoolean("userFirstLogin", true);
        if (!userFirstLogin) { // Ei avata tätä activitya, jos vanha käyttäjä
            Intent intent = new Intent(Settings.this, DefaultView.class);
            startActivity(intent);
        }

        RadioGroup radioGroupType = findViewById(R.id.radioType);
        RadioGroup radioGroupAmount = findViewById(R.id.radioAmount);
        RadioGroup radioGroupStrength = findViewById(R.id.radioStrength);
        RadioGroup radioGroupTimespan = findViewById(R.id.radioTimespan);

        Button btn = findViewById(R.id.open_activity_button);
        btn.setVisibility(View.GONE);
        updateUI();

        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == (R.id.type1)) {
                    user.setType(1);
                }
                else if (checkedId == (R.id.type2)) {
                    user.setType(2);
                }
                updateUI();
            }
        });

        radioGroupAmount.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == (R.id.amount1)) {
                    user.setAmount(1);
                }
                else if (checkedId == (R.id.amount2)) {
                    user.setAmount(2);
                }
                else if (checkedId == (R.id.amount3)) {
                    user.setAmount(3);
                }
                else if (checkedId == (R.id.amount4)) {
                    user.setAmount(4);
                }
                else if (checkedId == (R.id.amount5)) {
                    user.setAmount(5);
                }
                updateUI();
            }
        });

        radioGroupStrength.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == (R.id.strength1)) {
                    user.setStrength(1);
                }
                else if (checkedId == (R.id.strength2)) {
                    user.setStrength(2);
                }
                else if (checkedId == (R.id.strength3)) {
                    user.setStrength(3);
                }
                updateUI();
            }
        });

        radioGroupTimespan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == (R.id.timespan1)) {
                    user.setTimespan(1);
                }
                else if (checkedId == (R.id.timespan2)) {
                    user.setTimespan(2);
                }
                else if (checkedId == (R.id.timespan3)) {
                    user.setTimespan(3);
                }
                updateUI();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lasketaan nykyinen ja tuleva "lopetus" päivä, sekä niiden erotus
                Date startDate = Calendar.getInstance().getTime();
                Date endDate = new Date();
                Calendar myCal = Calendar.getInstance();
                myCal.setTime(endDate);
                myCal.add(Calendar.MONTH, +user.getTimespan());
                endDate = myCal.getTime();
                long dif = endDate.getTime() - startDate.getTime();
                long sec = dif / 1000;
                long min = sec / 60;
                long hrs = min / 60;
                dayc.setDaysTotal((int)(hrs / 24) + 1);
                dayc.setEndDate(endDate);

                //Avataan päänäkymä & Talletetaan user-olio jsoniksi siirtoa varten
                String userJson = gson.toJson(user);
                edit.putString("userObject", userJson);
                // Ei ole enää käyttäjän ensimmäinen login
                boolean userFirstLogin = false;
                edit.putBoolean("userFirstLogin", userFirstLogin);
                edit.apply();
                Intent intent = new Intent(Settings.this, DefaultView.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void updateUI() {
        Button btn = findViewById(R.id.open_activity_button);
        // "Valmis"-nappi näkyviin, kun kaikki valinnat tehty
        if (user.getType() != 0 && user.getAmount() != 0 && user.getStrength() != 0 && user.getTimespan() != 0) {
            btn.setVisibility(View.VISIBLE);
        }
    }
}