package com.example.bgctub_transport_tracker_app_information.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.bgctub_transport_tracker_app_information.R;
import com.example.bgctub_transport_tracker_app_information.SignInActivity;

import java.nio.channels.Channels;

public class BuildNotificationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildAppNotification();
    }


    //App Notification Builder**

    private void buildAppNotification() {
        //      String stop = "stop";
        //      registerReceiver(stopReceiver, new IntentFilter(stop));


        //      PendingIntent broadcastIntent = PendingIntent.getBroadcast(
        //             this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId="";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            channelId=(createNotificationChannel("notification service","App Notification"));

        }


        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder appNotificationBuilder = new NotificationCompat.Builder(this,channelId )
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.notification_text)))
                .setSmallIcon(R.drawable.logo1);
        startForeground(1, appNotificationBuilder.build());
    }

    //Create Channel for api 26+**
    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName){
        NotificationChannel channel=new NotificationChannel(channelId,channelName, NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager=getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }

  /*  protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //   unregisterReceiver(stopReceiver);                       // app stop after tapped notifications

            stopSelf();
        }
    };
   */

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
