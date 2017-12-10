package com.sid.marwadishaadi.Signup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.sid.marwadishaadi.App;
import com.sid.marwadishaadi.PlacesAdapter;
import com.sid.marwadishaadi.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class SignupDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "SignupDetailsActivity";
    public static SignupDetailsActivity sd = new SignupDetailsActivity();
    public static String mobile_number;
    protected EditText name;
    protected EditText surname;
    protected EditText dob;
    protected EditText mobile;
    protected ArrayAdapter<String> casteSpinnerAdapter;
    protected Button next;
    protected Spinner caste;
    protected AutoCompleteTextView location;
    protected PlacesAdapter placesAdapter;
    RadioGroup radioGroupGender;
    private String first_name, last_name, date_of_birth, user_caste, user_location, gender;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        SignupDetailsActivity.mobile_number = mobile_number;
    }

    public String getUser_caste() {
        return user_caste;
    }

    public void setUser_caste(String user_caste) {
        this.user_caste = user_caste;
    }

    public String getUser_location() {
        return user_location;
    }

    public void setUser_location(String user_location) {
        this.user_location = user_location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_signup_details);


        name = findViewById(R.id.first_name);
        surname = findViewById(R.id.last_name);
        dob = findViewById(R.id.dob);
        mobile = findViewById(R.id.mobile);
        caste = findViewById(R.id.user_caste);

        // autcomplete location
        location = findViewById(R.id.signup_location);
        placesAdapter = new PlacesAdapter(SignupDetailsActivity.this, R.layout.activity_signup_details, R.id.place_name, App.placeslist);
        location.setAdapter(placesAdapter);

        radioGroupGender = findViewById(R.id.radioGroupGender);
        next = findViewById(R.id.advnext);

        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 10) {
                    mobile.startAnimation(shakeError());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                first_name = name.getText().toString();
                last_name = surname.getText().toString();
                date_of_birth = dob.getText().toString();

                Date date = new Date();
                String userDateFormat = "MMM dd, yyyy";
                DateFormat sdf = new SimpleDateFormat(userDateFormat);
                Date datey = null;
                try {
                    datey = sdf.parse(date_of_birth);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                date_of_birth = formatter.format(datey);

                int diffInYears = (int) TimeUnit.MILLISECONDS.toDays(date.getTime() - datey.getTime()) / 365;

//                date_of_birth = dob.toString();

                mobile_number = mobile.getText().toString();
                user_caste = caste.getSelectedItem().toString();
                user_location = location.getText().toString();

//               find the radio button by returned id
                RadioGroup radioGroup = findViewById(R.id.radioGroupGender);
                radioGroup.check(R.id.radioButtonMale);

                if (first_name.trim().isEmpty() || last_name.trim().isEmpty() || date_of_birth.isEmpty() || mobile_number.isEmpty() || user_caste.isEmpty() || user_caste.contains("Select") || user_location.trim().isEmpty()) {

                    Toast.makeText(SignupDetailsActivity.this, "All fields are necessary", Toast.LENGTH_LONG).show();

                } else if (mobile_number.length() != 10 || !TextUtils.isDigitsOnly(mobile_number)) {

                    Toast.makeText(SignupDetailsActivity.this, "Mobile number is incorrect", Toast.LENGTH_LONG).show();

                } else if (diffInYears < 18) {

                    Toast.makeText(SignupDetailsActivity.this, "You must be at-least 18 years to register", Toast.LENGTH_LONG).show();

                } else {

                    gender = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                    sd.setFirst_name(first_name);
                    sd.setLast_name(last_name);
                    sd.setDate_of_birth(date_of_birth);
                    sd.setGender(gender);
                    sd.setMobile_number(mobile_number);
                    sd.setUser_location(user_location);
                    sd.setUser_caste(user_caste);


                    Intent i = new Intent(SignupDetailsActivity.this, AdvancedSignupDetailsActivity.class);
                    startActivity(i);

                }
            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                DatePickerfragment DOBPICKER = new DatePickerfragment();
                DOBPICKER.show(getSupportFragmentManager(), "dob");
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setDate(final Calendar calendar) {
        final DateFormat dateformat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        dob.setText(dateformat.format(calendar.getTime()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar cal = new GregorianCalendar(i, i1, i2);
        setDate(cal);
    }

    public static class DatePickerfragment extends DialogFragment {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year1 = calendar.get(Calendar.YEAR);
            int month1 = calendar.get(Calendar.MONTH);
            int day1 = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year1, month1, day1);
        }
    }
}
