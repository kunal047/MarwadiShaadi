package com.sid.marwadishaadi.Services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by vivonk on 03-08-2017.
 */

public class ChatNotifyService extends Service {
    private ScheduledExecutorService scheduledExecutorService;
    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        scheduledExecutorService= new ScheduledThreadPoolExecutor(5);
//        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                new UnreadChat().execute();
//            }
//        },0,1200,TimeUnit.SECONDS);
//        new UnreadChat().execute();
        return START_STICKY;
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
