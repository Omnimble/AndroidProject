package com.example.project;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;

public class Statistics extends AppCompatActivity {
    Gson gson = new Gson();
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        final SharedPreferences userDetails = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String userJson = userDetails.getString("userObject", "");
        user = gson.fromJson(userJson, User.class);

        // use.get.... komennolla voi hakea tietoja käyttäjästä
        //esim user.getTotalUsed(); --> returnaa yhteensä käytetyt nuuskat/röökit
    }
}