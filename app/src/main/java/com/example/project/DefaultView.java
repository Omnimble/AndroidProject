package com.example.project;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

public class DefaultView extends AppCompatActivity {
    User user = new User();
    DayCounter dayc = new DayCounter();
    Calculator calc = new Calculator(user, dayc);
    Timer timer = new Timer(calc);
    Gson gson = new Gson();
    CountDownTimer countDownTimer;
    BroadcastReceiver dayChangeReceiver;
    boolean isRunning = false;
    boolean dontBeep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_screen);

        // https://stackoverflow.com/questions/4805269/programmatically-register-a-broadcast-receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_DATE_CHANGED");
        dayChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                calc.resetAmountUsedToday();
            }
        };
        registerReceiver(dayChangeReceiver, filter);

        Button statsbtn = findViewById(R.id.open_stats_button);
        statsbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                savePrefs();
                Intent intent = new Intent(DefaultView.this, Statistics.class);
                startActivity(intent);
            }
        });

        Button settingsbtn = findViewById(R.id.open_settings_button);
        settingsbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences userDetails = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor edit = userDetails.edit();
                edit.putBoolean("accessSettings", true);
                edit.apply();
                savePrefs();
                Intent intent = new Intent(DefaultView.this, Settings.class);
                startActivity(intent);
            }
        });

        Button un = findViewById(R.id.useNow);
        un.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                countDownTimer.cancel();
                isRunning = false;
                calc.useNow();
                timer.setInterval();
                updateUI();
                calc.setFirstOfTheDay(false);
                createTimer();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        updateUI();
    }

    @Override
    public void onResume() {
        super.onResume();

        loadPrefs();
        calc.setDailyAmount();
        calc.setAdjustedDailyAmount();

        updateUI();
        createTimer();
    }

    public void updateUI() {
        calc.setAdjustedDailyAmount();
        TextView d = findViewById(R.id.days);
        TextView da = findViewById(R.id.adjustedDailyAmount);
        TextView cd = findViewById(R.id.countdown);
        TextView cdt = findViewById(R.id.countdownText);
        TextView at = findViewById(R.id.adjustedText);
        TextView dt = findViewById(R.id.daysText);
        TextView un = findViewById(R.id.useNow);
        d.setText(String.valueOf(dayc.getDaysLeft()));
        da.setText(String.valueOf(calc.getAdjustedDailyAmount()));

        if (!isRunning && calc.getAdjustedDailyAmount() == 0) {
            if (user.getType() == 1) {
                cd.setText(":(");
                cdt.setText(R.string.noMoreSmoke);
            } else {
                cd.setText(":(");
                cdt.setText(R.string.noMoreSnus);
            }
        }
        if (dayc.getDaysLeft() < 1) {
            d.setVisibility(View.GONE);
            da.setVisibility(View.GONE);
            cd.setVisibility(View.GONE);
            un.setVisibility(View.GONE);
            cd.setVisibility(View.GONE);
            at.setVisibility(View.GONE);
            dt.setVisibility(View.GONE);
            cdt.setText(R.string.gratz);
        }
    }

    // sendNotificationiin
    // https://stackoverflow.com/questions/45462666/notificationcompat-builder-deprecated-in-android-o
    void sendNotification(String title, String body) {
        Intent intent = new Intent(this, DefaultView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(0, builder.build());
    }

    BroadcastReceiver dateChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            calc.resetAmountUsedToday();
            timer.setTimer(0);
        }
    };

    void loadPrefs() {
        // Ladataan jsonit prefeistä ja muunnetaan ne takaisin olioiksi
        SharedPreferences userDetails = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String userJson = userDetails.getString("userObject", "");
        String daycJson = userDetails.getString("daycObject", "");
        String calcJson = userDetails.getString("calcObject", "");
        String timerJson = userDetails.getString("timerObject", "");
        dontBeep = userDetails.getBoolean("dontBeep", true);
        user = gson.fromJson(userJson, User.class);
        dayc = gson.fromJson(daycJson, DayCounter.class);
        calc = gson.fromJson(calcJson, Calculator.class);
        timer = gson.fromJson(timerJson, Timer.class);
    }

    void savePrefs() {
        // Muutetaan oliot jsoniksi ja talletetaan prefeihin
        SharedPreferences userDetails = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor edit = userDetails.edit();
        String userJson = gson.toJson(user);
        String daycJson = gson.toJson(dayc);
        String calcJson = gson.toJson(calc);
        String timerJson = gson.toJson(timer);
        edit.putString("userObject", userJson);
        edit.putString("daycObject", daycJson);
        edit.putString("calcObject", calcJson);
        edit.putString("timerObject", timerJson);
        edit.putBoolean("dontBeep", false);
        edit.apply();
    }

    @Override
    public void onBackPressed() {

    }

    public void createTimer() {
        final TextView cd = findViewById(R.id.countdown);
        final TextView cdt = findViewById(R.id.countdownText);
        countDownTimer = new CountDownTimer(timer.getInterval(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isRunning = true;
                timer.setTimer(millisUntilFinished);
                if (user.getType() == 1) {
                    cdt.setText(R.string.minutesTillSmoke);
                } else {
                    cdt.setText(R.string.minutesTillSnus);
                }
                //cd.setText(String.valueOf(millisUntilFinished / 1000 / 60));
                cd.setText(convertMillis(millisUntilFinished));
                updateUI();
            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                if (calc.getAdjustedDailyAmount() > 0) {
                    cd.setText(":)");
                    if (!dontBeep) {
                        if (user.getType() == 1) {
                            cdt.setText(R.string.nowSmoke);
                            sendNotification("Röökitauko", "Ei muuta ku rööki huulee");
                        } else {
                            cdt.setText(R.string.nowSnus);
                            sendNotification("Nuuskatauko", "Ei muuta ku pussi huulee");
                        }
                    } else if (user.getType() == 1) {
                        cdt.setText(R.string.nowSmoke);
                    } else {
                        cdt.setText(R.string.nowSnus);
                    }
                } else if (user.getType() == 1) {
                    cd.setText(":(");
                    cdt.setText(R.string.noMoreSmoke);

                } else if (user.getType() == 2) {
                    cd.setText(":(");
                    cdt.setText(R.string.noMoreSnus);
                }
            }
        };
        if (calc.getAdjustedDailyAmount() > 0) {
            countDownTimer.start();
        } else if (user.getType() == 1) {
            cdt.setText(R.string.noMoreSmoke);
        } else if (user.getType() == 2) {
            cdt.setText(R.string.noMoreSnus);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        countDownTimer.cancel();
        timer.setTimerWasDestroyed();
        isRunning = false;
        savePrefs();
    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public void onDestroy() {
        if (dateChangeReceiver != null) { // https://stackoverflow.com/questions/4805269/programmatically-register-a-broadcast-receiver
            unregisterReceiver(dateChangeReceiver);
            dateChangeReceiver = null;
        }
        super.onDestroy();
    }

    public String convertMillis(long durationInMillis) {
        long millis = durationInMillis % 1000;
        long second = (durationInMillis / 1000) % 60;
        long minute = (durationInMillis / (1000 * 60)) % 60;
        long hour = (durationInMillis / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
}