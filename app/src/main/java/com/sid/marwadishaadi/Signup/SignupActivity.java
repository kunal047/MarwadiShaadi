package com.sid.marwadishaadi.Signup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.sid.marwadishaadi.Analytics_Util;
import com.sid.marwadishaadi.R;

import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class
SignupActivity extends AppCompatActivity {

    public static SignupActivity su = new SignupActivity();
    protected EditText email;
    protected EditText pass;
    protected EditText confirm;
    protected Button next;
    protected LoginButton fblogin;
    CallbackManager callbackManager;
    private ImageView showPassword;
    private ImageView hidePassword;
    private ImageView confirmShowPassword;
    private ImageView confirmHidePassword;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String uemail, upass;

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getUpass() {
        return upass;
    }

    public void setUpass(String upass) {
        this.upass = upass;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        email = (EditText) findViewById(R.id.signup_email);
        pass = (EditText) findViewById(R.id.signup_password);
        confirm = (EditText) findViewById(R.id.confirm_password);

        showPassword = (ImageView) findViewById(R.id.show_password);
        hidePassword = (ImageView) findViewById(R.id.hide_password);

        confirmShowPassword = (ImageView) findViewById(R.id.confirm_show_password);
        confirmHidePassword = (ImageView) findViewById(R.id.confirm_hide_password);

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPassword.setVisibility(View.GONE);
                hidePassword.setVisibility(View.VISIBLE);
                pass.setTransformationMethod(null);
                pass.setSelection(pass.getText().length());

            }
        });

        hidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPassword.setVisibility(View.VISIBLE);
                hidePassword.setVisibility(View.GONE);
                pass.setTransformationMethod(new PasswordTransformationMethod());
                pass.setSelection(pass.getText().length());
            }
        });

        confirmShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmShowPassword.setVisibility(View.GONE);
                confirmHidePassword.setVisibility(View.VISIBLE);
                confirm.setTransformationMethod(null);
                confirm.setSelection(confirm.getText().length());

            }
        });

        confirmHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmShowPassword.setVisibility(View.VISIBLE);
                confirmHidePassword.setVisibility(View.GONE);
                confirm.setTransformationMethod(new PasswordTransformationMethod());
                confirm.setSelection(confirm.getText().length());

            }
        });


        callbackManager = CallbackManager.Factory.create();

//        fblogin = (LoginButton) findViewById(R.id.login_button);
//
//        fblogin.setReadPermissions(Arrays.asList("email"));
//        fblogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//
//
//                        try {
//                            String first_name = object.getString("first_name");
//                            String last_name = object.getString("last_name");
//                            String gender = object.getString("gender");
//                            String name = object.getString("name");
//
//                            // check must be performed here
//                            //String email = object.getString("email");
//                            String birthday = object.getString("birthday");
//                            Toast.makeText(getApplicationContext(), first_name + last_name + gender + birthday, Toast.LENGTH_LONG).show();
//
//                            // MUST GO TO dashboard
//                            Intent i = new Intent(SignupActivity.this, SignupActivity.class);
//                            startActivity(i);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,about,birthday,middle_name,first_name,last_name,email,gender,name,relationship_status");
//                request.setParameters(parameters);
//                request.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });

        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "SignupNext", "button");

                uemail = email.getText().toString();
                uemail = uemail.toUpperCase();
                upass = pass.getText().toString();
                String cpass = confirm.getText().toString();

                if (uemail.isEmpty() || upass.isEmpty() || cpass.isEmpty()) {

                    Toast.makeText(SignupActivity.this, "All fields are necessary", Toast.LENGTH_LONG).show();
                } else if (!isValidEmaillId(uemail.trim())) {

                    Toast.makeText(getApplicationContext(), "Invalid User Id or Email", Toast.LENGTH_LONG).show();
                } else if (upass.length() < 6 || cpass.length() < 6) {

                    Toast.makeText(getApplicationContext(), "Password length has to be atleast 6", Toast.LENGTH_LONG).show();
                } else if (!upass.equalsIgnoreCase(cpass)) {

                    Toast.makeText(SignupActivity.this, "Password does not match", Toast.LENGTH_LONG).show();
                } else {

                    su.setUemail(uemail);
                    su.setUpass(upass);

                    Intent i = new Intent(SignupActivity.this, SignupDetailsActivity.class);
                    startActivity(i);
                }

            }
        });
    }


    private boolean isValidEmaillId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
}
