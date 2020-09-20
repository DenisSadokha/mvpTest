package notfication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Objects;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
//            Log.d("receiver", "yea i am");
//
//        }
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Bundle bundle = intent.getExtras();
                assert bundle != null;
                int id = bundle.getInt("id");
                Log.d("ReceiveId", "" + id);
                String task = bundle.getString("task");
                NotificationHelper notificationHelper = new NotificationHelper(context);
                NotificationCompat.Builder nb = notificationHelper.createNotification("Дела не ждут! Задача на подходе!", task, id);
                Log.d("receiverTAG", "yea i am RECEIVE 3");
                // id equals null (0) after reboot device
                notificationHelper.getNotificationManager().notify(id, nb.build());
                Log.d("receiverTAG", "yea i am RECEIVE 4 ");

            }
        }catch (Exception e){
             Log.d("receiverTAG", "" +e);
        }
    }
}
