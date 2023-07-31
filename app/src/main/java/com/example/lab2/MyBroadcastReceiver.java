package com.example.lab2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getStringExtra("attribute1" + "attribute2" + "attribute3" + "attribute4" + "attribute5" + "attribute6"),
                Toast.LENGTH_SHORT).show();
    }
}
