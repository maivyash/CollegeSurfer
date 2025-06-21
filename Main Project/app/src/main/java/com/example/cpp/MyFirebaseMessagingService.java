package com.example.cpp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    protected String serverkey = "AAAAcQMei34:APA91bENyNsTZAfLIEXAl3ZVJIOCK1VbZ3DHGLfl98cuoNIXDqlZ47ENC7yuzHCoBlO2shcL3SjSXlPzLuacuxUk5ILqkS-_MNC9rGkz75dftbhj3RChdZIZY3oGcEbV_-fxlwtFPi6s";
    protected String url = "https://fcm.googleapis.com/fcm/send";

    public MyFirebaseMessagingService() {
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String text = remoteMessage.getNotification().getBody();

        String CHANNEL_ID = "MESSAGE";
        CharSequence name="DEFAULT";
        NotificationChannel channel = null;
        Notification.Builder notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);//getting error here
            Context context;
            if (remoteMessage.getNotification().getImageUrl()==null){
                notification = new Notification.Builder(this, CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setSmallIcon(R.drawable.logo3)
                        // .setSmallIcon(Icon.createWithContentUri(remoteMessage.getNotification().getImageUrl()))
                        .setAutoCancel(true);
            }else {
                notification = new Notification.Builder(this, CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setSmallIcon(Icon.createWithContentUri(remoteMessage.getNotification().getImageUrl()))
                        .setAutoCancel(true);
            }


        }else {
            getSystemService(NotificationManager.class);
            Context context;

            notification = new Notification.Builder(this)

                    .setContentText(text)
                    .setSmallIcon(Icon.createWithContentUri(remoteMessage.getNotification().getImageUrl()))
                    .setAutoCancel(true);
        }


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NotificationManagerCompat.from(this).notify(1, notification.build());
        super.onMessageReceived(remoteMessage);


    }

    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("MyFirebaseMsgService", "Refreshed token: " + token);
    }
}
