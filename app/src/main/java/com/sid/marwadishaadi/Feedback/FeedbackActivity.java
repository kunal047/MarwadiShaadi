package com.sid.marwadishaadi.Feedback;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.sid.marwadishaadi.Analytics_Util;
import com.sid.marwadishaadi.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FeedbackActivity extends AppCompatActivity {

    protected EditText fftext;
    protected Button send;
    protected CheckBox email_response;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.feedback_toolbar);
        toolbar.setTitle("Feedback");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        fftext = (EditText) findViewById(R.id.edt_feedback);

        send = (Button) findViewById(R.id.sendFeedback);
        email_response = (CheckBox) findViewById(R.id.email_response);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "Feedback", "button");

                boolean email_response_required = false;
                if (email_response.isChecked()) {
                    email_response_required = true;
                }

                String phone_details = getPhoneDetails();
                String bodyuser = "\n\n\n\n\nUser Device details\n-----------------------\n" + phone_details;
                String username = "Reply Not Required";
                if (email_response_required) {
                    username = "Reply Required";
                }
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String userid = "S123";
                userid = sharedPreferences.getString("customer_id", null);
                String subject = "Feedback from user " + username + " ( " + userid + " ) ";
                String feedback = fftext.getText().toString() + bodyuser;
                if (!feedback.trim().isEmpty()) {

                    String TO = "tech@marwadishaadi.com";
                    Intent send = new Intent(Intent.ACTION_SENDTO);
                    String uriText = "mailto:" + Uri.encode(TO) +
                            "?subject=" + Uri.encode(subject) +
                            "&body=" + Uri.encode(feedback);
                    Uri uri = Uri.parse(uriText);
                    send.setData(uri);
                    startActivity(Intent.createChooser(send, "Send mail..."));
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter Text in Feedback Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public String getPhoneDetails() {

        String details = "VERSION.RELEASE : " + Build.VERSION.RELEASE
                + "\nVERSION.INCREMENTAL : " + Build.VERSION.INCREMENTAL
                + "\nVERSION.SDK.NUMBER : " + Build.VERSION.SDK_INT
                + "\nBOARD : " + Build.BOARD
                + "\nBRAND : " + Build.BRAND
                + "\nDISPLAY : " + Build.DISPLAY
                + "\nHARDWARE : " + Build.HARDWARE
                + "\nHOST : " + Build.HOST
                + "\nID : " + Build.ID
                + "\nMANUFACTURER : " + Build.MANUFACTURER
                + "\nMODEL : " + Build.MODEL
                + "\nPRODUCT : " + Build.PRODUCT
                + "\nSERIAL : " + Build.SERIAL
                + "\nTIME : " + Build.TIME
                + "\nTYPE : " + Build.TYPE
                + "\nUSER : " + Build.USER;
        return details;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
