package com.example.sid.marwadishaadi;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Sid on 10-Jul-17.
 */

public class MyService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {

        String registration_id = FirebaseInstanceId.getInstance().getToken();
        Log.d("registration-device -> ", registration_id);

        // algo
        // if user logged in -> get userid and store device id
        // if not -> store it in prefs
        // after successfull -> login/signup -> then update in firebase
        Notifications_Util.RegisterDevice("A1001", registration_id);

    }

}
