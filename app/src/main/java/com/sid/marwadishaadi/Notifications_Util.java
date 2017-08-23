package com.sid.marwadishaadi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sid.marwadishaadi.Chat.DefaultDialogsActivity;
import com.sid.marwadishaadi.Dashboard.DashboardActivity;
import com.sid.marwadishaadi.Dashboard_Interest.InterestActivity;
import com.sid.marwadishaadi.Membership.MembershipActivity;
import com.sid.marwadishaadi.Membership.UpgradeMembershipActivity;
import com.sid.marwadishaadi.Notifications.NotificationsActivity;
import com.sid.marwadishaadi.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by Sid on 30-Jun-17.
 */

public class Notifications_Util {

    // notification sound
    static final Uri notifsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    static final Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
    static String api_key = "key=AAAAUe8pB3Q:APA91bGbd9V8mrZ8dtvzXqjgcbtdqlRHXVzBcZpX1mM_f2jPe1EcH6p0Ksl4MjmORMRUGM7tCQUUhH9dAxHdvGEkQpwn11D5YQ9ag5ZGRRDI1UWX_G19UirKcSSbi9eAHf8nexG5jPd9";
    static DatabaseReference mDatabase;

    public static Notification.Builder createNotification(String type, String title, String message, Context context, int notifyid){

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setSmallIcon(getIcon(type))
                .setAutoCancel(true)
                .setContentText(message)
                .setSound(notifsound)
                .setContentIntent(getIntent(getType(type),context));

        notificationManager.notify(notifyid,notification.build());

        return notification;

    }

    public static void unRegisterDevice(String customer_id,String registration_id){

        mDatabase = FirebaseDatabase.getInstance().getReference(customer_id).child("Devices");
        mDatabase.child(registration_id).removeValue();
    }

    public static void RegisterDevice(String customer_id,String registration_id){

        mDatabase = FirebaseDatabase.getInstance().getReference(customer_id).child("Devices");
        DeviceRegistration deviceRegistration = new DeviceRegistration(registration_id,customer_id);
        mDatabase.child(deviceRegistration.getDevice_id()).setValue(deviceRegistration);
    }

    public static void SendNotification(String registration_id,String bodymsg,String titlemsg,String type){

        JSONObject notification = new JSONObject();
        try {
            notification.put("to",registration_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject body = new JSONObject();
        try {
            body.put("body",bodymsg);
            body.put("title",titlemsg);
            notification.put("notification",body);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject data = new JSONObject();
        try {
            data.put("Type",type);
            notification.put("data",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        AndroidNetworking.post("https://fcm.googleapis.com/fcm/send")
            .addHeaders("Content-Type","application/json")
            .addHeaders("Authorization",api_key)
            .addJSONObjectBody(notification)
            .setTag("test")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {

                }
                @Override
                public void onError(ANError error) {
                    // handle error

                }
            });

    }
    public static void stackNotification(Notification.Builder notification, int times, Context context,List<Notif_Message> allmsg,String type,int notifyid){

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notification.setStyle(inboxStyle);

        for (int i=0;i<times;i++){

            if (i==times-1){

                String summary = allmsg.size() +" "+"new " + type;
                String summary_usernames;

                if (allmsg.size() == 1){
                    if (type.equals("Interest")){
                        summary_usernames = allmsg.get(0).getFullname() + " sent you an interest";
                    }else if(type.equals("Interest Accepted")){
                        summary_usernames = allmsg.get(0).getFullname() + " accepted your interest";
                    }else{
                        summary_usernames = allmsg.get(0).getFrom() + " sent you a message";
                    }
                }else if(allmsg.size() == 2){
                    if (type.equals("Interest")){
                        summary_usernames = allmsg.get(0).getFrom() + " and " + allmsg.get(1).getFrom() +" sent you a interest";
                    }else if (type.equals("Interest Accepted")){
                        summary_usernames = allmsg.get(0).getFrom() + " and " + allmsg.get(1).getFrom() +" accepted your interest";
                    }else{
                        summary_usernames = allmsg.get(0).getFrom() + " and " + allmsg.get(1).getFrom() +" messaged you";
                    }
                }else{
                    if(type.equals("Interest")){
                        summary_usernames = allmsg.get(0).getFrom() + "," + allmsg.get(1).getFrom() + " and " + ((allmsg.size()) - 2) + " others sent you interest";
                    }else if(type.equals("Interest Accepted")){
                        summary_usernames = allmsg.get(0).getFrom() + "," + allmsg.get(1).getFrom() + " and " + ((allmsg.size()) - 2) + " others accepted your interest" + type;
                    }else{
                        summary_usernames = allmsg.get(0).getFrom() + "," + allmsg.get(1).getFrom() + " and " + ((allmsg.size()) - 2) + " others messaged you";
                    }
                }

                if (type.equals("Message")){
                    inboxStyle.addLine(allmsg.get(i).getFrom() + " : " + allmsg.get(i).getMsg());
                }else{
                    inboxStyle.addLine(allmsg.get(i).getFullname());
                }
                inboxStyle.setSummaryText(summary);
                notification.setContentTitle(summary);
                notification.setContentText(summary_usernames);
                notificationManager.notify(notifyid,notification.build());
            }else{
                if (type.equals("Message")){
                    inboxStyle.addLine(allmsg.get(i).getFrom() + " : " + allmsg.get(i).getMsg());
                }else{
                    inboxStyle.addLine(allmsg.get(i).getFullname());
                }
                notificationManager.notify(notifyid,notification.build());
            }
        }

    }


    public static int getIcon(String type){
        switch (type){
            case "Suggestions":
                return R.drawable.notif_suggestion;
            case "Interest Request":
                return R.drawable.notif_interest;
            case "Interest Accept":
                return R.drawable.notif_interest;
            case "Message":
                return R.drawable.notif_msg;
            case "Membership":
                return R.drawable.notif_membership;
            case "Membership Expired":
                return R.drawable.notif_membership;
            case "Reminders":
                return R.drawable.notif_reminder;
            case "Offers":
                return R.drawable.notif_offer;
            case "Birthday":
                return R.drawable.notif_birthday;
            default:
                return R.drawable.notif_reminder;
        }
    }


    public static int getType(String type){
        switch (type){
            case "Suggestions":
                return 0;
            case "Interest Request":
                return 1;
            case "Interest Accept":
                return 2;
            case "Message":
                return 3;
            case "Membership":
                return 4;
            case "Membership Expired":
                return 5;
            case "Reminders":
                return 6;
            case "Offers":
                return 7;
            case "Birthday":
                return 8;
            default:
                return 9;
        }
    }
    public static PendingIntent getIntent(int type, Context context){

        // notifications intent

        Intent i;
        switch (type){

            // suggestion
            case 0:
                i = new Intent(context, DashboardActivity.class);
                break;

            // interest request
            case 1:
                i = new Intent(context, InterestActivity.class);
                break;

            // interest accept
            case 2:
                i =  new Intent(context,InterestActivity.class);
                break;

            // message
            case 3:
                i = new Intent(context, DefaultDialogsActivity.class);
                break;

            // membership
            case 4:
                i = new Intent(context, MembershipActivity.class);
                break;

            // membership expired
            case 5:
                i = new Intent(context, UpgradeMembershipActivity.class);
                break;

            // reminders
            case 6:
                i = new Intent(context, NotificationsActivity.class);
                break;

            // offers
            case 7:
                i=new Intent(context,NotificationsActivity.class);
                break;

            // bday
            case 8:
                i = new Intent(context,NotificationsActivity.class);
                break;

            default:
                i = new Intent(context,NotificationsActivity.class);
                break;

        }
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

}
