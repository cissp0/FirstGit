package com.android.settings.accessibility.assistivetouch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AssistiveTouchServiceEnableBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("resumet", "Settings Package : AssistiveTouchServiceEnableBroadcastReceiver!");
    }
}