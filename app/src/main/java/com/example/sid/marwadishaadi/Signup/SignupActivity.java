package com.example.sid.marwadishaadi.Signup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class
SignupActivity extends AppCompatActivity {

    protected EditText email;
    protected EditText pass;
    protected EditText confirm;
    protected Button next;

    protected LoginButton fblogin;
    CallbackManager callbackManager;
    private FirebaseAnalytics mFirebaseAnalytics;

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

        callbackManager = CallbackManager.Factory.create();

        fblogin = (LoginButton) findViewById(R.id.login_button);

        fblogin.setReadPermissions(Arrays.asList("email"));
        fblogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        Log.d("object", object.toString());

                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String gender = object.getString("gender");
                            String name = object.getString("name");

                            // check must be performed here
                            //String email = object.getString("email");
                            String birthday = object.getString("birthday");
                            Toast.makeText(getApplicationContext(), first_name + last_name + gender + birthday, Toast.LENGTH_LONG).show();

                            // MUST GO TO dashboard
                            Intent i = new Intent(SignupActivity.this, SignupActivity.class);
                            startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,about,birthday,middle_name,first_name,last_name,email,gender,name,relationship_status");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "SignupNext", "button");

                String uemail = email.getText().toString();
                String upass = pass.getText().toString();
                String cpass = confirm.getText().toString();

                Intent i = new Intent(SignupActivity.this, SignupDetailsActivity.class);
                startActivity(i);

                // rest
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
}
