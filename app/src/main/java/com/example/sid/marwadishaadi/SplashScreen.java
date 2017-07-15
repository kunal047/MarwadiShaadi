package com.example.sid.marwadishaadi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.sid.marwadishaadi.Notifications.NotificationsActivity;
import com.example.sid.marwadishaadi.Notifications.NotificationsModel;
import com.example.sid.marwadishaadi.User_Profile.UserProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class SplashScreen extends AppCompatActivity {

    public static final int duration = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

      /* final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("A1001").child("Notifications");
        final NotificationsModel notification= new NotificationsModel("Mervin","12-9-17",3,true,false,false,false,false,false,false,false,false);
        String hash = String.valueOf(notification.hashCode());
        mDatabase.child(hash).setValue(notification);
*/
        // sending notification
       // Notifications_Util.SendNotification(registration_id, "Mervin sent you an Interest", "New Interest", "Interest Request");

        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(getIntent())
            .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                @Override
                public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                    }
                    if (deepLink != null) {

                        // sending deeplink
                        setUpIntent(0, deepLink.toString());

                    } else {

                        setUpIntent(1, null);
                    }
                }
            })
            .addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("Error", "getDynamicLink:onFailure", e);
                }
            });


    }
    
    public void setUpIntent(final int activitycode, final String deeplink){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                if (activitycode == 0) {
                     i = new Intent(SplashScreen.this,UserProfileActivity.class);
                    if(deeplink!=null){
                        i.putExtra("deeplink",deeplink);
                    }
                }else{
                     i = new Intent(SplashScreen.this,NotificationsActivity.class);
                }
                startActivity(i);
                finish();
            }
        },duration);
    }
}

