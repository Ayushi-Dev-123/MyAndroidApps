package com.example.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Enumeration;

public class MissedCallReceiver extends BroadcastReceiver {
    static boolean ringing = false;
    static boolean idle = false;
    static boolean busy = false;
    static String number = "";
    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
