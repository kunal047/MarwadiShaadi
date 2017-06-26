package com.example.sid.marwadishaadi.LoginHistory;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;



import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class OnClearFromRecentService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("ClearFromRecentService-", "-----------------------------------------Service Started");
        SharedPreferences sharedPreferences=getSharedPreferences("userinfo",MODE_PRIVATE);
        SharedPreferences.Editor edtr=sharedPreferences.edit();
        String id=sharedPreferences.getString("customer_id","");

        Log.e(TAG, "onStartCommand: .........................."+id);
        new History().execute();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("ClearFromRecentService-", "-----------------------------------Service Destroyed");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("Clearvi--------------", "---------------------------------END");
        //Code here

        stopSelf();
    }
    class History extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}