package com.example.project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

// En saa toimimaan, pitäisi saada apille tieto kulkemaan kun päivä vaihtuu

public class DateChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_DATE_CHANGED)) {
            Log.e("DateChangeReceiver", "Date has changed");
            context.sendBroadcast(new Intent("ACTION_DATE_CHANGED"));
        }
    }
}

