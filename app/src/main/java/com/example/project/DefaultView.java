package com.example.project;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

public class DefaultView extends AppCompatActivity {

    User user = new User();
    DayCounter dayc = new DayCounter();
    NicotineCalculator calc = new NicotineCalculator(user, dayc);
    Timer timer = new Timer(dayc, calc);

    Gson gson = new Gson();
    CountDownTimer countDownTimer;
    BroadcastReceiver dayChangeReceiver;

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

        final SharedPreferences userDetails = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String userJson = userDetails.getString("userObject", "");
        String daycJson = userDetails.getString("daycObject", "");
        String calcJson = userDetails.getString("calcObject", "");
        String timerJson = userDetails.getString("timerObject", "");
        user = gson.fromJson(userJson, User.class);
        dayc = gson.fromJson(daycJson, DayCounter.class);
        calc = gson.fromJson(calcJson, NicotineCalculator.class);
        timer = gson.fromJson(timerJson, Timer.class);

        calc.setDaílyAmount();
        calc.setAdjustedDailyAmount();
        updateUI();
        if (dayc.getDaysLeft() < 1) {
            Button un = findViewById(R.id.useNow);
            TextView cd = findViewById(R.id.countdown);
            TextView cdt = findViewById(R.id.countdownText);
            TextView at = findViewById(R.id.adjustedDailyAmount);
            TextView dt = findViewById(R.id.daysLeft);
            un.setVisibility(View.GONE);
            cd.setVisibility(View.GONE);
            at.setVisibility(View.GONE);
            dt.setVisibility(View.GONE);
            cdt.setText(R.string.gratz);

        }

        createTimer();
        Button un = findViewById(R.id.useNow);
        un.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                calc.useNow();
                timer.setInterval();
                countDownTimer.cancel();
                updateUI();
                if (calc.getAdjustedDailyAmount() == calc.getAmountUsedToday()) {
                    TextView cd = findViewById(R.id.countdown);
                    cd.setText("");
                    updateUI();
                } else {
                    createTimer();
                    updateUI();
                }

            }
        });

        Button statsbtn = findViewById(R.id.statsbtn);
        statsbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final SharedPreferences.Editor edit = userDetails.edit();
                String userJson = gson.toJson(user);
                edit.putString("userObject", userJson);
                edit.apply();
                Intent intent = new Intent(DefaultView.this, Statistics.class);
                startActivity(intent);
            }
        });
    }

    // sendNotificationiin
    // https://stackoverflow.com/questions/45462666/notificationcompat-builder-deprecated-in-android-o
    private void sendNotification(String title, String body) {
        Intent i = new Intent(this, Settings.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(this,
                0 /* Request code */,
                i,
                PendingIntent.FLAG_ONE_SHOT);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(pi);

        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(0, builder.build());
    }

    BroadcastReceiver dateChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            calc.resetAmountUsedToday();
            timer.setMillisLeftOver(0);
        }
    };

    @Override
    public void onBackPressed() {

    }

    public void createTimer() {
        final TextView cd = findViewById(R.id.countdown);
        final TextView cdt = findViewById(R.id.countdownText);
        countDownTimer = new CountDownTimer(timer.getInterval(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setMillisLeftOver(millisUntilFinished);
                timer.setTimer(millisUntilFinished);
                updateUI();
                if (user.getType() == 1) {
                    cdt.setText(R.string.minutesTillSmoke);
                } else {
                    cdt.setText(R.string.minutesTillSnus);
                }
                cd.setText(String.valueOf(millisUntilFinished / 1000 / 60));
            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                cd.setText("");
                if (calc.getAdjustedDailyAmount() > 0) {
                    if (user.getType() == 1) {
                        cdt.setText(R.string.nowSmoke);
                        sendNotification("Röökitauko", "Ei muuta ku rööki huulee");
                    } else {
                        cdt.setText(R.string.nowSnus);
                        sendNotification("Nuuskatauko", "Ei muuta ku pussi huulee");
                    }
                } else if (user.getType() == 1) {
                    cdt.setText(R.string.noMoreSmoke);

                } else if (user.getType() == 2) {
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

    public void updateUI() {
        calc.setAdjustedDailyAmount();
        TextView d = findViewById(R.id.days);
        TextView da = findViewById(R.id.adjustedDailyAmount);
        d.setText(String.valueOf(dayc.getDaysLeft()));
        da.setText(String.valueOf(calc.getAdjustedDailyAmount()));
        if (calc.getAmountUsedToday() == calc.getAdjustedDailyAmount()) {
            TextView cd = findViewById(R.id.countdown);
            cd.setText("");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        timer.setTimerDestroyed(false);
        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();

        final SharedPreferences userDetails = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final SharedPreferences.Editor edit = userDetails.edit();

        timer.setTimerDestroyed(true);
        timer.setTimeOnDestroyed();
        String userJson = gson.toJson(timer);
        String daycJson = gson.toJson(timer);
        String calcJson = gson.toJson(timer);
        String timerJson = gson.toJson(timer);
        edit.putString("userObject", userJson);
        edit.putString("daycObject", daycJson);
        edit.putString("calcObject", calcJson);
        edit.putString("timerObject", timerJson);
        edit.apply();
    }

    @Override
    public void onDestroy() {
        if (dateChangeReceiver != null) { // https://stackoverflow.com/questions/4805269/programmatically-register-a-broadcast-receiver
            unregisterReceiver(dateChangeReceiver);
            dateChangeReceiver = null;
        }
        super.onDestroy();
    }
}