package com.example.sid.marwadishaadi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.sid.marwadishaadi.Dashboard.DashboardActivity;
import com.example.sid.marwadishaadi.Forgot_Password.ForgotPasswordActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sid on 19-Jun-17.
 */

public class MSFirebaseMessagingService extends FirebaseMessagingService {


    final List<Notif_Message> allmsg = new ArrayList<>();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        Log.d("notification",remoteMessage.getNotification().getTitle());

        Notifications_Util.createNotification(remoteMessage.getData().get("Type"),remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),this,1);

       /* Notif_Message message = new Notif_Message("Mervin","first");
        Notif_Message message1 = new Notif_Message("Maitree","second");
        Notif_Message message2 = new Notif_Message("Siddhesh","third");

        allmsg.add(message);
        allmsg.add(message1);
        allmsg.add(message2);


        Notification.Builder notification = Notifications_Util.createNotification("Message","first","hello",this,1);
        Notifications_Util.stackNotification(notification,3,this,allmsg,"Message",1);
*/
    }

}
