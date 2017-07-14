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
    public void onStart(Intent intent, int startId) {
        String registration_id = FirebaseInstanceId.getInstance().getToken();
        Log.d("registration-device -> ", registration_id);
    }

    @Override
    public void onTokenRefresh() {

        String registration_id = FirebaseInstanceId.getInstance().getToken();
        Log.d("registration-device -> ", registration_id);
        
        sendDeviceRegistrationToServer(registration_id);
    }

    public void sendDeviceRegistrationToServer(String device_id){
        
        // // TODO: 10-Jul-17  network call 
    }
}
