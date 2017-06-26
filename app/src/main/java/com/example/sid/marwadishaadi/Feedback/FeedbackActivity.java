package com.example.sid.marwadishaadi.Feedback;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.R;
import com.google.firebase.analytics.FirebaseAnalytics;

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


        fftext=(EditText) findViewById(R.id.edt_feedback);

        send=(Button) findViewById(R.id.sendFeedback);
        email_response = (CheckBox) findViewById(R.id.email_response);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics,"Feedback","button");

                boolean email_response_required = false;
                if (email_response.isChecked()){
                    email_response_required = true;
                }

                String phone_details = getPhoneDetails();
                String bodyuser = "\n\n\n\n\nUser Device details\n-----------------------\n" + phone_details;
                String username = "Siddhesh Rane";
                String userid ="S123";
                String subject = "Feedback from user " + username + " ( " + userid + " ) ";
                String feedback=fftext.getText().toString() + bodyuser;
                if(!feedback.trim().isEmpty())
                {

                    String TO = "techteam@marwadishaadi.com";
                    Intent send = new Intent(Intent.ACTION_SENDTO);
                    String uriText = "mailto:" + Uri.encode(TO) +
                            "?subject=" + Uri.encode(subject) +
                            "&body=" + Uri.encode(feedback);
                    Uri uri = Uri.parse(uriText);
                    send.setData(uri);
                    startActivity(Intent.createChooser(send, "Send mail..."));
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Text in Feedback Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public String getPhoneDetails(){

        String  details =  "VERSION.RELEASE : "+Build.VERSION.RELEASE
                +"\nVERSION.INCREMENTAL : "+Build.VERSION.INCREMENTAL
                +"\nVERSION.SDK.NUMBER : "+Build.VERSION.SDK_INT
                +"\nBOARD : "+Build.BOARD
                +"\nBOOTLOADER : "+Build.BOOTLOADER
                +"\nBRAND : "+Build.BRAND
                +"\nCPU_ABI : "+Build.CPU_ABI
                +"\nCPU_ABI2 : "+Build.CPU_ABI2
                +"\nDISPLAY : "+Build.DISPLAY
                +"\nFINGERPRINT : "+Build.FINGERPRINT
                +"\nHARDWARE : "+Build.HARDWARE
                +"\nHOST : "+Build.HOST
                +"\nID : "+Build.ID
                +"\nMANUFACTURER : "+Build.MANUFACTURER
                +"\nMODEL : "+Build.MODEL
                +"\nPRODUCT : "+Build.PRODUCT
                +"\nSERIAL : "+Build.SERIAL
                +"\nTAGS : "+Build.TAGS
                +"\nTIME : "+Build.TIME
                +"\nTYPE : "+Build.TYPE
                +"\nUNKNOWN : "+Build.UNKNOWN
                +"\nUSER : "+Build.USER;
        return details;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        overridePendingTransition(R.anim.exit,0);
        return true;
    }
}
