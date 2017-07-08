package com.example.sid.marwadishaadi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.sid.marwadishaadi.Dashboard.DashboardActivity;
import com.example.sid.marwadishaadi.User_Profile.UserProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class SplashScreen extends AppCompatActivity {

    public static final int duration = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

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
                            setUpIntent(0,deepLink.toString());
                            
                        } else {
                            
                            setUpIntent(1,null);
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
                        startActivity(i);
                    }
                }else{
                    SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    boolean check = saved_values.getBoolean("isLoggedIn", false);
//                Toast.makeText(getApplicationContext(),"Please check your Internet Connection",Toast.LENGTH_LONG);
                    Log.d(":", "onDonePressed:--------------------------- bool is  " + check);
                    if (check) {
                        Intent intnt = new Intent(getApplicationContext(), DashboardActivity.class);
                        intnt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intnt);
                    }
                    else {
                        Intent intnt = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intnt);
                        finish();
                    }
                }

                finish();
            }
        },duration);
    }
}

