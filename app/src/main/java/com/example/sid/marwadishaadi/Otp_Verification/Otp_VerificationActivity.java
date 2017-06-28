package com.example.sid.marwadishaadi.Otp_Verification;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.Upload_User_Photos.UploadPhotoActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

// TO DO Please change mobile number , Use an internet connection try catch, Show the mobile number to user where OTP will be send and also give option to change mobile number
public class Otp_VerificationActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    private static final String TAG = "";
    static int OTP=0;
    protected EditText otp;
    protected Button submit;
    protected TextView otp_call,resend_otp;
    protected LinearLayout otp_contact;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otp__verification);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        otp = (EditText) findViewById(R.id.user_otp);
        submit = (Button) findViewById(R.id.Submit_otp);
        otp_call = (TextView) findViewById(R.id.otp_call_number);
        resend_otp=(TextView)findViewById(R.id.resend_otp);
        otp_contact=(LinearLayout)findViewById(R.id.otp_call_us);
        new SendingSMS().execute("8006467951");


        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics,"Resent OTP","button");
                new SendingSMS().execute("8006467951");
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics,"OTP verification","button");

                String user_otp = otp.getText().toString();
                Toast.makeText(getApplicationContext(), "OTP created is"+ Integer.toString(OTP), Toast.LENGTH_SHORT).show();
                if(user_otp.equals(Integer.toString(OTP)))
                {
                    Intent i = new Intent(Otp_VerificationActivity.this,UploadPhotoActivity.class);
                    startActivity(i);
                }
                else if(user_otp.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please enter your OTP",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please enter correct OTP",Toast.LENGTH_LONG).show();
                }
                // rest
            }
        });

        otp_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics,"OTP_Call_US","button");

                final Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + otp_call.getText().toString()));//change the number
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getApplicationContext(),"Permission for Call Denied!",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    AlertDialog.Builder discarduser = new AlertDialog.Builder(Otp_VerificationActivity.this);
                    discarduser.setMessage("Do you want to call " + otp_call.getText().toString() + " ? ")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    startActivity(callIntent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    // setting up dialog box
                    AlertDialog alertbox = discarduser.create();
                    alertbox.setTitle("Contact Us");
                    alertbox.show();

                }
            }
        });

    }


    class SendingSMS extends AsyncTask<String, Object, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            {
                //Your user name

                String username = "Rishi1";
                //Your authentication key
                String authkey = "d808a22243XX";
                //Multiple mobiles numbers separated by comma (max 200)
                String mobiles = "8006467951";
                mobiles = strings[0];
                //Sender ID,While using route4 sender id should be 6 characters  long.
                String senderId = "MRWSHD";
                //Your message to send, Add URL encoding here.
                String message = "";
                //define route
                String accusage="1";

                char[] otp=mobiles.toCharArray();
                OTP=0;
                for(int i=0;i<otp.length;i+=2)
                {
                    int var=(otp[i]);
                    OTP+=(Math.pow(10,i/2))*(var);
                }
                message = "Welcome to Marwadishaadi.com \n Please enter the OTP " + Integer.toString(OTP) + " to complete the verification of your number.";

                //Prepare Url
                URLConnection myURLConnection=null;
                URL myURL=null;
                BufferedReader reader=null;

                //encoding message
                String encoded_message= URLEncoder.encode(message);

                //Send SMS API
                String mainUrl="http://smspanel.marwadishaadi.com/submitsms.jsp?";

                //Prepare parameter string
                StringBuilder sbPostData= new StringBuilder(mainUrl);
                sbPostData.append("user="+username);
                sbPostData.append("&key="+authkey);
                sbPostData.append("&mobile="+mobiles+",9820923040");
                sbPostData.append("&message="+encoded_message);
                sbPostData.append("&accusage="+accusage);
                sbPostData.append("&senderid="+senderId);
                Log.d("Kuch bhi---------",mainUrl);
                //final string
                mainUrl = sbPostData.toString();
                try
                {
                    //prepare connection
                    Log.d(TAG, "OTPSender: mainurl is " + mainUrl);
                    myURL = new URL(mainUrl);
                    myURLConnection = myURL.openConnection();
                    myURLConnection.connect();
                    reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
                    //reading response
                    String response;
                    while ((response = reader.readLine()) != null)
                        //print response
                        System.out.println(response);

                    //finally close connection
                    reader.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }




}
