package com.wgustudent.SamuelCrumplerC196MobileApp.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class Broadcaster extends BroadcastReceiver {
    String message;


    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Message for the notification was sent!");

        Bundle b = intent.getExtras();
        message = b.getString("message");
        System.out.println("Message: "+message);

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

    }
}
