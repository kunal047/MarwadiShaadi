package com.example.sid.marwadishaadi;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.example.sid.marwadishaadi.Dashboard.DashboardActivity;
import com.example.sid.marwadishaadi.LoginHistory.OnClearFromRecentService;
import com.example.sid.marwadishaadi.User_Profile.UserProfileActivity;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by Sid on 14-Jun-17.
 */

public class App extends Application{

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cl=Calendar.getInstance();
        String formattedDate = df.format( Calendar.getInstance().getTime());
        cl.add(Calendar.DATE,90);
        String s2= df.format(cl.getTime());
        Log.e(TAG, "onCreate:--- "+ formattedDate+ "update is ------"+s2);

        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        OnClearFromRecentService onClearFromRecentService=new OnClearFromRecentService();
            SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
            boolean check = sharedpref.getBoolean("isLoggedIn", false);
            Toast.makeText(getApplicationContext(),"Please check your Internet Connection",Toast.LENGTH_LONG);
            Log.d(":", "onDonePressed:--------------------------- bool is  " + check);
            if (check) {
                Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
                startActivity(i);
            }
        }


}
