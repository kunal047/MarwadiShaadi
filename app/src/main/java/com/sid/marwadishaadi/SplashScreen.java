package com.sid.marwadishaadi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sid.marwadishaadi.Dashboard.DashboardActivity;
import com.sid.marwadishaadi.Login.LoginActivity;
import com.sid.marwadishaadi.User_Profile.UserProfileActivity;

public class SplashScreen extends AppCompatActivity {

    public static final int duration = 1000;
    private static final String TAG = "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
                            setUpIntent(0, deepLink.toString());

                        } else {

                            setUpIntent(1, null);
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });


    }

    public void setUpIntent(final int activitycode, final String deeplink) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;

                // from deeplink -> go to profile
                if (activitycode == 0) {
                    if (isUserLoggedIn()) {
                        i = new Intent(SplashScreen.this, UserProfileActivity.class);
                        if (deeplink != null) {
                            i.putExtra("deeplink", deeplink);
                        }
                    } else {
                        i = new Intent(SplashScreen.this, LoginActivity.class);
                        i.putExtra("deeplink", deeplink);
                    }

                } else {
                    SharedPreferences userfor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editorFor = userfor.edit();

                    if (isUserLoggedIn()) {
                        i = new Intent(SplashScreen.this, DashboardActivity.class);
                        startActivity(i);


                    } else if (!userfor.getBoolean("firstTime", false)) {

                        editorFor.putBoolean("firstTime", true);
                        editorFor.apply();

                        LoginManager.getInstance().logOut();
                        AccessToken.setCurrentAccessToken(null);

                        SharedPreferences userinfo = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editors = userinfo.edit();

                        editors.putBoolean("isLoggedIn", false);
                        editors.putBoolean("firstTime", false);
                        editors.apply();

                        SharedPreferences sharedPre = getSharedPreferences("userinfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPre.edit();
                        editor.putBoolean("isLoggedIn", false);
                        editor.putString("email", "");
                        editor.putString("password", "");
                        editor.putString("customer_id", "");
                        editor.apply();

//                        Notifications_Util.unRegisterDevice(customer_id, FirebaseInstanceId.getInstance().getToken());
//
//                        // analytics
//                        Analytics_Util.logAnalytic(mFirebaseAnalytics, "Logout", "textview");

                        i = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(i);
                    } else {
                        if (isFirstLaunch()) {
                            i = new Intent(SplashScreen.this, MainActivity.class);
                        } else {
                            i = new Intent(SplashScreen.this, LoginActivity.class);

                        }
                    }
                }
                startActivity(i);
                finish();
            }
        }, duration);
    }

    private boolean isUserLoggedIn() {
        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedpref.getBoolean("isLoggedIn", false);
    }

    private boolean isFirstLaunch() {
        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedpref.getBoolean("isFirstTime", false);
    }
}

