package com.example.lab2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
    public static final String SMS_FILTER = "MySMS";
    public static final String SMS_MSG_KEY = "SMS_MSG_KEY";
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (int i = 0; i < messages.length; i++) {
            SmsMessage currentMessage = messages[i];
            String senderNum = currentMessage.getDisplayOriginatingAddress();
            String message = currentMessage.getMessageBody();
            Toast.makeText(context, "Sender: " + senderNum + ", Message: " + message, Toast.LENGTH_LONG).show();

            Intent myIntent = new Intent();
            myIntent.setAction(SMS_FILTER);
            myIntent.putExtra(SMS_MSG_KEY, message);
            context.sendBroadcast(myIntent);
        }
    }
}
