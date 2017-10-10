package com.sid.marwadishaadi.Otp_Verification;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sid.marwadishaadi.Analytics_Util;
import com.sid.marwadishaadi.Constants;
import com.sid.marwadishaadi.R;
import com.sid.marwadishaadi.Upload_User_Photos.UploadPhotoActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.sid.marwadishaadi.Signup.SignupActivity.su;
import static com.sid.marwadishaadi.Signup.SignupDetailsActivity.mobile_number;
import static com.sid.marwadishaadi.Signup.SignupDetailsActivity.sd;
import static com.sid.marwadishaadi.Signup.Signup_Additional_Info_Fragment.ai;
import static com.sid.marwadishaadi.Signup.Signup_Basic_Info_Fragment.bi;
import static com.sid.marwadishaadi.Signup.Signup_Partner_Preferences_Fragment.pf;

// TO DO Please change mobile number , Use an internet connection try catch, Show the mobile number to user where OTP will be send and also give option to change mobile number
public class Otp_VerificationActivity extends AppCompatActivity {

    public static final int REQUEST_PERMISSION_SETTING = 105;
    public static final int CALL_PHONE_PERMISSION = 102;

    static int OTP = 0;
    protected EditText otp;
    protected Button submit;
    protected TextView otp_call, resend_otp;
    protected LinearLayout otp_contact;
    private FirebaseAnalytics mFirebaseAnalytics;
    private View view;
    private String sign_in_number;
    private String newCustomerNo;
    private TextView textViewEditNumber;
    private AlertDialog.Builder builder1;
    private Boolean wantToCloseDialog;
    private ProgressDialog progressDialog;


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
        view = getWindow().getDecorView().getRootView();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        otp = (EditText) findViewById(R.id.user_otp);
        submit = (Button) findViewById(R.id.Submit_otp);
        otp_call = (TextView) findViewById(R.id.otp_call_number);
        resend_otp = (TextView) findViewById(R.id.resend_otp);
        otp_contact = (LinearLayout) findViewById(R.id.otp_call_us);


        ImageView imageViewEditNumber = (ImageView) findViewById(R.id.imageViewEditNumber);
        textViewEditNumber = (TextView) findViewById(R.id.textViewEditNumber);
        textViewEditNumber.setText(sd.getMobile_number());

        builder1 = new AlertDialog.Builder(Otp_VerificationActivity.this);
        builder1.setMessage("Sending OTP on " + sd.getMobile_number());
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        new SendingSMS().execute(sd.getMobile_number());
                    }
                });

        builder1.setNegativeButton(
                "Edit Number",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });


        final AlertDialog alert11 = builder1.create();
        alert11.show();
        alert11.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wantToCloseDialog = false;

                final EditText taskEditText = new EditText(Otp_VerificationActivity.this);
                taskEditText.setText(sd.getMobile_number());
                taskEditText.setSelection(sd.getMobile_number().length());

                AlertDialog.Builder alertDialogBuiler = new AlertDialog.Builder(Otp_VerificationActivity.this)
                        .setMessage("Edit Number")
                        .setView(taskEditText)
                        .setCancelable(false)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("Cancel", null);

                final AlertDialog dialogBuilder = alertDialogBuiler.create();
                dialogBuilder.show();
                dialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (taskEditText.getText().toString().length() != 10 || !TextUtils.isDigitsOnly(taskEditText.getText())) {
                            Toast.makeText(Otp_VerificationActivity.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                        } else {

                            sd.setMobile_number(taskEditText.getText().toString());
                            textViewEditNumber.setText(sd.getMobile_number());
                            dialogBuilder.dismiss();
                            alert11.dismiss();
                            alert11.setMessage("Sending OTP on " + sd.getMobile_number());
                            alert11.show();

                        }
                    }
                });

                if (wantToCloseDialog)
                    alert11.dismiss();
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });

        imageViewEditNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText taskEditText = new EditText(Otp_VerificationActivity.this);
                taskEditText.setText(sd.getMobile_number());
                taskEditText.setSelection(sd.getMobile_number().length());

                AlertDialog dialogEditNumber = new AlertDialog.Builder(Otp_VerificationActivity.this)
                        .setMessage("Edit Number")
                        .setView(taskEditText)
                        .setCancelable(false)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (taskEditText.getText().toString().length() != 10 || !TextUtils.isDigitsOnly(taskEditText.getText())) {
                                    Toast.makeText(Otp_VerificationActivity.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                                } else {
                                    sd.setMobile_number(taskEditText.getText().toString());
                                    textViewEditNumber.setText(sd.getMobile_number());
                                    new SendingSMS().execute(sd.getMobile_number());
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialogEditNumber.show();

            }
        });

        sign_in_number = mobile_number;


        //TODO change number
//        new SendingSMS().execute(sign_in_number);


        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "Resent OTP", "button");
                //TODO change number
                new SendingSMS().execute(sign_in_number);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(Otp_VerificationActivity.this);
                progressDialog.setMessage("Setting up your profile...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "OTP verification", "button");

                String user_otp = otp.getText().toString();

//                Toast.makeText(getApplicationContext(), "OTP created is" + Integer.toString(OTP), Toast.LENGTH_SHORT).show();
//                new SendSignUpDetails().execute();

                if (user_otp.equals(Integer.toString(OTP))) {
                    new SendSignUpDetails().execute();
                } else if (user_otp.equals("")) {

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please enter your OTP", Toast.LENGTH_LONG).show();

                } else if (!TextUtils.isDigitsOnly(user_otp) || user_otp.length() < 6) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Incorrect OTP", Toast.LENGTH_LONG).show();
                }

            }
        });


        otp_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // add permission here
                int permissionCheck = ContextCompat.checkSelfPermission(Otp_VerificationActivity.this, Manifest.permission.CALL_PHONE);

                if (permissionCheck == PackageManager.PERMISSION_DENIED) {

                    if (!getPermissionStatus()) {

                        Dexter.withActivity(Otp_VerificationActivity.this)
                                .withPermission(Manifest.permission.CALL_PHONE)
                                .withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse response) {
                                        Call();
                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse response) {

                                        setPermissionStatus();
                                        showSettings();
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                    }
                                }).check();
                    } else {
                        showSettings();
                    }
                } else {
                    Call();
                }

            }
        });

    }

    private void Call() {

        // analytics
        Analytics_Util.logAnalytic(mFirebaseAnalytics, "OTP_Call_US", "button");

        final Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + otp_call.getText().toString()));//change the number
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Permission_Util for Call Denied!", Toast.LENGTH_LONG).show();
            return;
        } else {
            AlertDialog.Builder discarduser = new AlertDialog.Builder(Otp_VerificationActivity.this);
            discarduser.setMessage("Do you want to call " + otp_call.getText().toString() + " ? ")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(Otp_VerificationActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
//                Toast.makeText(Otp_VerificationActivity.this, "coollll", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CALL_PHONE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Call();
            } else {
                Toast.makeText(Otp_VerificationActivity.this, "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showSettings() {
        Snackbar snackbar = Snackbar
                .make(view.getRootView(), "Go to settings and grant permission", Snackbar.LENGTH_LONG)
                .setAction("Settings", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });

        snackbar.show();
    }

    private Boolean getPermissionStatus() {
        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedpref.getBoolean("isPhonePermissionDenied", false);
    }

    private void setPermissionStatus() {

        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = sharedpref.edit();
        edit.putBoolean("isPhonePermissionDenied", true);
        edit.apply();
    }


    private class SendSignUpDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            AndroidNetworking.post(Constants.AWS_SERVER + "/signUpDetails")
                    .addBodyParameter("firstName", sd.getFirst_name())
                    .addBodyParameter("lastName", sd.getLast_name())
                    .addBodyParameter("dateOfBirth", sd.getDate_of_birth())
                    .addBodyParameter("gender", sd.getGender())
                    .addBodyParameter("mobileNumber", sd.getMobile_number())
                    .addBodyParameter("userLocation", sd.getUser_location())
                    .addBodyParameter("userCaste", sd.getUser_caste())
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new SendBasicInfo().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
    }


    private class SendBasicInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            AndroidNetworking.post(Constants.AWS_SERVER + "/basicInfo")
                    .addBodyParameter("height", bi.getHeight())
                    .addBodyParameter("built", bi.getBuilt())
                    .addBodyParameter("maritalStatus", bi.getMaritalStatus())
                    .addBodyParameter("education", bi.getEducation())
                    .addBodyParameter("userOccupation", bi.getOccupation())
                    .addBodyParameter("designation", bi.getDesignation())
                    .addBodyParameter("annualIncome", bi.getAnnualIncome())
                    .addBodyParameter("fatherOccupation", bi.getFatherOccupation())
                    .addBodyParameter("familyStatus", bi.getFamilyStatus())
                    .addBodyParameter("weight", bi.getWeight())
                    .addBodyParameter("highestDegree", bi.getHighestDegree())
                    .addBodyParameter("companyName", bi.getCompanyName())
                    .addBodyParameter("fatherName", bi.getFatherName())
                    .addBodyParameter("fatherOccupationDetails", bi.getFatherOccupationDetails())
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new SendAdditionalInfo().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
    }

    private class SendAdditionalInfo extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            AndroidNetworking.post(Constants.AWS_SERVER + "/additionalInfo")
                    .addBodyParameter("aboutMe", ai.getAboutMe())
                    .addBodyParameter("hobbies", ai.getHobbies())
                    .addBodyParameter("grandfatherName", ai.getGrandfatherName())
                    .addBodyParameter("mamaSurname", ai.getMamaSurname())
                    .addBodyParameter("nativePlace", ai.getNativePlace())
                    .addBodyParameter("subcaste", ai.getSubcaste())
                    .addBodyParameter("instituteName", ai.getInstituteName())
                    .addBodyParameter("workLocation", ai.getWorkLocation())
                    .addBodyParameter("gotra", ai.getGotra())
                    .addBodyParameter("birhtTime", ai.getBirthTime())
                    .addBodyParameter("birthPlace", ai.getBirthPlace())
                    .addBodyParameter("relationFirstName", ai.getRelationFirstName())
                    .addBodyParameter("relationOccupation", ai.getRelationOccupation())
                    .addBodyParameter("relationMobile", ai.getRelationMobile())
                    .addBodyParameter("relationLocation", ai.getRelationLocation())
                    .addBodyParameter("familyType", ai.getFamilyType())
                    .addBodyParameter("familyValues", ai.getFamilyValues())
                    .addBodyParameter("dietStatus", ai.getDietStatus())
                    .addBodyParameter("smokeStatus", ai.getSmokeStatus())
                    .addBodyParameter("drinkStatus", ai.getDrinkStatus())
                    .addBodyParameter("complexionStatus", ai.getComplexionStatus())
                    .addBodyParameter("physicalStatus", ai.getPhysicalStatus())
                    .addBodyParameter("manglikStatus", ai.getManglikStatus())
                    .addBodyParameter("horoscopeStatus", ai.getHoroscopeStatus())
                    .addBodyParameter("relationNameStatus", ai.getRelationNameStatus())
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {

                                newCustomerNo = response.getString(0);

                                SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpref.edit();
                                editor.putBoolean("isLoggedIn", true);
                                editor.putString("customer_id", newCustomerNo);
                                editor.putString("gender", sd.getGender());
                                editor.apply();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {


                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            new SendPartnerPreference().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

        }
    }

    private class SendPartnerPreference extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (pf.getPrefPhysicalStatus().contains("Matter")) {
                pf.setPrefPhysicalStatus("");
            }
            AndroidNetworking.post(Constants.AWS_SERVER + "/uploadPreferences")
                    .addBodyParameter("custNo", newCustomerNo)
                    .addBodyParameter("minAge", pf.getPrefMinAge())
                    .addBodyParameter("maxAge", pf.getPrefMaxAge())
                    .addBodyParameter("heightFrom", pf.getPrefHeightFrom())
                    .addBodyParameter("heightTo", pf.getPrefHeightTo())
                    .addBodyParameter("education", pf.getPreferenceEducation().toString().replace("[", "").replace("]", ""))
                    .addBodyParameter("maritalStatus", pf.getPrefMaritalStatus())
                    .addBodyParameter("annualIncome", pf.getPrefAnnualIncome())
                    .addBodyParameter("complexion", pf.getPreferenceComplexion().toString().replace("[", "").replace("]", ""))
                    .addBodyParameter("bodyType", pf.getPreferenceBodyType().toString().replace("[", "").replace("]", ""))
                    .addBodyParameter("physicalStatus", pf.getPrefPhysicalStatus())
                    .addBodyParameter("workLocation", pf.getPrefWorkLocation().replace("[", "").replace("]", ""))
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                        }

                        @Override
                        public void onError(ANError anError) {


                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new SendSignUp().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

        }
    }

    private class SendSignUp extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AndroidNetworking.post(Constants.AWS_SERVER + "/signUp")
                    .addBodyParameter("custNo", newCustomerNo)
                    .addBodyParameter("email", su.getUemail())
                    .addBodyParameter("password", su.getUpass())
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            Intent i = new Intent(Otp_VerificationActivity.this, UploadPhotoActivity.class);
            i.putExtra("from", "otp");
            startActivity(i);
        }
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
                String mobiles;
                mobiles = strings[0];

                //Sender ID,While using route4 sender id should be 6 characters  long.
                String senderId = "MRWSHD";
                //Your message to send, Add URL encoding here.
                String message = "";
                //define route
                String accusage = "1";

//                char[] otp = mobiles.toCharArray();
                OTP = 0;
                Random rnd = new Random(System.currentTimeMillis());
                OTP = 100000 + rnd.nextInt(900000);
//                for (int i = 0; i < otp.length; i += 2) {
//                    int var = (otp[i]);
//                    OTP += (Math.pow(10, i / 2)) * (var);
//                }

                message = "Welcome to Marwadishaadi.com \n Please enter the OTP " + Integer.toString(OTP) + " to complete the verification of your number.";

                //Prepare Url
                URLConnection myURLConnection = null;
                URL myURL = null;
                BufferedReader reader = null;

                //encoding message
                String encoded_message = URLEncoder.encode(message);

                //Send SMS API
                String mainUrl = "http://smspanel.marwadishaadi.com/submitsms.jsp?";

                //Prepare parameter string
                StringBuilder sbPostData = new StringBuilder(mainUrl);
                sbPostData.append("user=" + username);
                sbPostData.append("&key=" + authkey);
                //TODO change number
                sbPostData.append("&mobile=" + mobiles + "," + sign_in_number);
                sbPostData.append("&message=" + encoded_message);
                sbPostData.append("&accusage=" + accusage);
                sbPostData.append("&senderid=" + senderId);

                //final string
                mainUrl = sbPostData.toString();
                try {
                    //prepare connection

                    myURL = new URL(mainUrl);
                    myURLConnection = myURL.openConnection();
                    myURLConnection.connect();
                    reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));

                    //finally close connection
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }


}
