package com.sheridan.capstone.sheridanmobile20;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Anthony Lionti on 2017-08-28.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {//not used yet

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("msg", "onMessageReceived: " + remoteMessage.getData().get("message"));
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("test")
                .setContentText(remoteMessage.getData().get("message"));
        NotificationManager manager = (NotificationManager)     getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }
}
