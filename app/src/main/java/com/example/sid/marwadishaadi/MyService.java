package com.example.sid.marwadishaadi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Sid on 10-Jul-17.
 */

public class MyService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {

        // algo
        // if user logged in -> get userid and store device id
        // if not -> store it in prefs
        // after successfull -> login/signup -> then update in firebase
        String registration_id = FirebaseInstanceId.getInstance().getToken();


        SharedPreferences userinfo = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userid = userinfo.getString("customer_id",null);
        if(userid!=null){
            Notifications_Util.RegisterDevice(userid, registration_id);
        }else{
            SharedPreferences.Editor editor = userinfo.edit();
            editor.putString("device_token",registration_id);
            editor.apply();
        }


    }

}
