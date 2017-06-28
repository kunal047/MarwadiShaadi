package com.example.sid.marwadishaadi.Signup;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import android.widget.DatePicker;

import com.example.sid.marwadishaadi.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class SignupDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    protected EditText name;
    protected EditText surname;
    protected EditText dob;
    protected EditText mobile;
    protected ArrayAdapter<String> casteSpinnerAdapter;
    protected Button next;

    protected  Spinner caste;

    protected AutoCompleteTextView location;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_signup_details);


        name = (EditText) findViewById(R.id.first_name);
        surname = (EditText) findViewById(R.id.last_name);
        dob=(EditText)findViewById(R.id.dob);
        mobile = (EditText) findViewById(R.id.mobile);

        caste = (Spinner) findViewById(R.id.user_caste);


        location = (AutoCompleteTextView) findViewById(R.id.location);

        next = (Button) findViewById(R.id.advnext);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String first_name = name.getText().toString();
                String last_name = surname.getText().toString();
                String date_of_birth = dob.getText().toString();
                String mobile_number = mobile.getText().toString();
                String user_caste = caste.getSelectedItem().toString();
                String user_location = location.getText().toString();

                Intent i = new Intent(SignupDetailsActivity.this,AdvancedSignupDetailsActivity.class);
                startActivity(i);


            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerfragment DOBPICKER   = new DatePickerfragment();
                DOBPICKER.show(getSupportFragmentManager(),"dob");
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setDate(final Calendar calendar)
    {
        final DateFormat dateformat= DateFormat.getDateInstance(DateFormat.MEDIUM);
        dob.setText(dateformat.format(calendar.getTime()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar cal= new GregorianCalendar(i,i1,i2);
        setDate(cal);
    }
    public static class DatePickerfragment extends DialogFragment{
        @RequiresApi(api = Build.VERSION_CODES.N)
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year1 = calendar.get(Calendar.YEAR);
            int month1 = calendar.get(Calendar.MONTH);
            int day1 = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener)getActivity(),year1,month1,day1);
        }
    }
}