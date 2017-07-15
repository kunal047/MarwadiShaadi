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
import com.example.sid.marwadishaadi.Login.LoginActivity;
import com.example.sid.marwadishaadi.User_Profile.UserProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);


   /*     // registering device
        String registration_id = FirebaseInstanceId.getInstance().getToken();


        // sending notification
        Notifications_Util.SendNotification(registration_id,"Mervin sent you an Interest","New Interest","Interest Request");

*/
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

                // from deeplink -> go to profile
                if (activitycode == 0) {
                    if (isUserLoggedIn()){
                        i = new Intent(SplashScreen.this,UserProfileActivity.class);
                        if(deeplink!=null){
                            i.putExtra("deeplink",deeplink);
                        }
                    }else{
                        i = new Intent(SplashScreen.this,LoginActivity.class);
                        i.putExtra("deeplink",deeplink);
                    }

                }else{
                    if (isUserLoggedIn()){
                        i = new Intent(SplashScreen.this, DashboardActivity.class);
                    }else{
                        if (isFirstLaunch()){
                            i = new Intent(SplashScreen.this,MainActivity.class);
                        }else{
                            i = new Intent(SplashScreen.this,LoginActivity.class);

                        }
                    }
                }
                startActivity(i);
                finish();
            }
        },duration);
    }

    private boolean isUserLoggedIn(){
        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedpref.getBoolean("isLoggedIn", false);
    }

    private boolean isFirstLaunch(){
        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedpref.getBoolean("isFirstTime",false);
    }
}

