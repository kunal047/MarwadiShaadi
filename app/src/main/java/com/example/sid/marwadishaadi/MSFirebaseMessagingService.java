package com.example.sid.marwadishaadi;

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


        Notifications_Util.createNotification(remoteMessage.getData().get("Type"), remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), this, 1);

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
