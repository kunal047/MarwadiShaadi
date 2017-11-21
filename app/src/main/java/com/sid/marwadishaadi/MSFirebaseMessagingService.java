package com.sid.marwadishaadi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sid.marwadishaadi.Dashboard.DashboardActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sid on 19-Jun-17.
 */

public class MSFirebaseMessagingService extends FirebaseMessagingService {


    final List<Notif_Message> allmsg = new ArrayList<>();
    private static final String TAG = "MSFirebaseMessagingServ";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // checking if user has notification ON/OFF in settings
        SharedPreferences userinfo = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean notificationStatus = userinfo.getBoolean("Notification_Status", true);
        if (notificationStatus) {

            Log.d(TAG, "onMessageReceived: title is ------------------------------------------ " + remoteMessage.getNotification().getTitle());
            if (remoteMessage.getNotification().getTitle().contains("MarwadiShaadi")) {

                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.notif_suggestion)
                                .setContentTitle("Marwadi Shaadi")
                                .setContentText(remoteMessage.getNotification().getBody());

                Intent notificationIntent = new Intent(this, DashboardActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(contentIntent);

                // Add as notification
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, builder.build());
                builder.setAutoCancel(true);


            } else {
                Notifications_Util.createNotification(remoteMessage.getData().get("Type"), remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), this, 1);

            }
        }

        // checking if rgt user is logged in

       /* Notif_Message message = new Notif_Message("Mervin","first");
        Notif_Message message1 = new Notif_Message("Maitree","second");
        Notif_Message message2 = new Notif_Message("Siddhesh","third");

        allmsg.add(message);
        allmsg.add(message1);
        allmsg.add(message2);


*/

    }



}
