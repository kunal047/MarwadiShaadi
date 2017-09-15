package com.sid.marwadishaadi.Services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sid.marwadishaadi.DeviceRegistration;
import com.sid.marwadishaadi.Notifications.NotificationsModel;
import com.sid.marwadishaadi.Notifications_Util;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by vivonk on 03-08-2017.
 */

public class ChatNotifyService extends Service {
    SharedPreferences sharedPreferences; // = getApplicationContext().getSharedPreferences("userinfo", MODE_PRIVATE);
    private String customer_no, customer_name;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabases;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        customer_no = sharedPreferences.getString("customer_id", "null");
        customer_name = sharedPreferences.getString("nameOfCustomer", customer_name);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                new UnreadChat().execute();

            }
        }, 10800000);
        return START_STICKY;
    }

    public void setData(DataSnapshot dataSnapshot) {

        // looping through all the devices and sending push notification to each of 'em
        DeviceRegistration device = dataSnapshot.getValue(DeviceRegistration.class);
        Notifications_Util.SendNotification(device.getDevice_id(), "You have a new message", "Marwadi Shaadi: New Message", "Message");
    }

    private class UnreadChat extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            AndroidNetworking.post("http://208.91.199.50:5000/checkForChat")
                    .addBodyParameter("customerNo", customer_no)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {

                                String isNewMsg = response.getString(0);

                                if (isNewMsg.contains("yes")) {
                                    String date = String.valueOf(DateFormat.format("dd-MM-yyyy", new Date()));
                                    mDatabase = FirebaseDatabase.getInstance().getReference(customer_no).child("Notifications");
                                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                                    Date currentDate = calendar.getTime();
                                    String hash = String.valueOf(currentDate.hashCode());

                                    final NotificationsModel notification = new NotificationsModel(hash, customer_name, date, 3, false, false, false, true, false, false, false, false, false, false);
                                    mDatabase.child(hash).setValue(notification);

                                    // sending push notification to her
                                    // get all devices

                                    mDatabases = FirebaseDatabase.getInstance().getReference(customer_no).child("Devices");
                                    mDatabases.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                            setData(dataSnapshot);
                                        }

                                        @Override
                                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
            return null;
        }
    }
//    private class UnreadChat extends AsyncTask<String,String,String>{

//        String query;
//        NotificationCompat.Builder notification;
//        @Override
//        protected String doInBackground(String... strings) {
//
//            sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//            query="select count(msg_id) from tbl_message where msg_to='"+sharedPreferences.getString("customer_id","A1001")+"' and msg_read='0';";
//
//            AndroidNetworking.post("http://208.91.199.50:5000/ResetPassword")
//                    .addBodyParameter("query",query)
//                    .build()
//                    .getAsJSONArray(new JSONArrayRequestListener() {
//                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//                        @Override
//                        public void onResponse(JSONArray response) {
//
//                            try {
//                                if (!response.getJSONArray(0).getString(0).equals("0")&!sharedPreferences.getString("lastChatCount","1").equals(response.getJSONArray(0).getString(0))) {
//                                    notification = new NotificationCompat.Builder(ChatNotifyService.this);
//                                    SharedPreferences.Editor editor=sharedPreferences.edit();
//                                    editor.putString("lastChatCount",response.getJSONArray(0).getString(0));
//                                    editor.apply();
//                                    try {
//                                        Intent intent = new Intent(ChatNotifyService.this, DefaultDialogsActivity.class);
//                                        PendingIntent pendingIntent = PendingIntent.getActivity(ChatNotifyService.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//                                        String notificationMessage="You are having " + response.getJSONArray(0).getString(0) + " new Messages";
//                                        notification.setContentTitle("MarwadiShaadi")
//                                                .setSmallIcon(R.drawable.ic_forum_black_48dp)
//                                                .setContentIntent(pendingIntent)
//                                        .setStyle(new NotificationCompat.BigTextStyle()
//                                                .bigText(notificationMessage))
//                                                .setContentText(notificationMessage).setAutoCancel(true);
//                                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                                        notificationManager.notify(0, notification.build());
//
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                                }catch(JSONException e){
//                                    e.printStackTrace();
//                                }
//                        }
//
//                        @Override
//                        public void onError(ANError anError) {
//
//                        }
//                    });
//            return null;
//        }
//    }
}
