package com.example.sid.marwadishaadi.Signup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.Place;
import com.example.sid.marwadishaadi.PlacesAdapter;
import com.example.sid.marwadishaadi.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.example.sid.marwadishaadi.Login.LoginActivity.customer_id;


public class SignupDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static SignupDetailsActivity sd = new SignupDetailsActivity();
    private final String TAG = "SignupDetailsActivity";
    protected EditText name;
    protected EditText surname;
    protected EditText dob;
    protected EditText mobile;
    protected ArrayAdapter<String> casteSpinnerAdapter;
    protected Button next;
    protected List<Place> placeslist = new ArrayList<>();
    protected Spinner caste;
    protected AutoCompleteTextView location;
    protected PlacesAdapter placesAdapter;
    RadioGroup radioGroupGender;
    private String first_name, last_name, date_of_birth, mobile_number, user_caste, user_location, gender;

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
        this.mobile_number = mobile_number;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_signup_details);


        name = (EditText) findViewById(R.id.first_name);
        surname = (EditText) findViewById(R.id.last_name);
        dob = (EditText) findViewById(R.id.dob);
        mobile = (EditText) findViewById(R.id.mobile);

        caste = (Spinner) findViewById(R.id.user_caste);

        location = (AutoCompleteTextView) findViewById(R.id.signup_location);
        location.setThreshold(1);
        getData();
        placesAdapter = new PlacesAdapter(SignupDetailsActivity.this, R.layout.activity_signup_details, R.id.place_name, placeslist);
        location.setAdapter(placesAdapter);

        radioGroupGender = (RadioGroup) findViewById(R.id.radioGroupGender);


        next = (Button) findViewById(R.id.advnext);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                first_name = name.getText().toString();
                last_name = surname.getText().toString();
                date_of_birth = dob.getText().toString();
                Date date = new Date("13-Jun-2017");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                date_of_birth = formatter.format(date);
                mobile_number = mobile.getText().toString();
                user_caste = caste.getSelectedItem().toString();
                user_location = location.getText().toString();

//               find the radio button by returned id
                RadioButton radioButton = (RadioButton) findViewById(radioGroupGender.getCheckedRadioButtonId());

                Log.d(TAG, "onClick: valuese are --------------------- " + radioGroupGender.getCheckedRadioButtonId());
//                if (radioButton != null && radioButton.getText() == null || first_name.isEmpty() || last_name.isEmpty() || date_of_birth.isEmpty() || mobile_number.isEmpty() || user_caste.isEmpty() || user_location.isEmpty()) {
//                    Toast.makeText(SignupDetailsActivity.this, "All Fields are compulsory", Toast.LENGTH_SHORT).show();
//                } else {


                gender = radioButton.getText().toString();
                sd.setFirst_name(first_name);
                sd.setLast_name(last_name);
                sd.setDate_of_birth(date_of_birth);
                sd.setGender(gender);
                sd.setMobile_number(mobile_number);
                sd.setUser_location(user_location);
                sd.setUser_caste(user_caste);

//                Fragment fragment = new Signup_Basic_Info_Fragment();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.fragmentBasicInfo, fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();

                Intent i = new Intent(SignupDetailsActivity.this, AdvancedSignupDetailsActivity.class);
                startActivity(i);

//                }


            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerfragment DOBPICKER = new DatePickerfragment();
                DOBPICKER.show(getSupportFragmentManager(), "dob");
            }
        });

    }

    public void getData() {

        new FetchLocation().execute();

    }

    public class FetchLocation extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            AndroidNetworking.post("http://192.168.43.143:5050/fetchCityStateCountry")
                    .addBodyParameter("customerNo", customer_id)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Place place;
                            try {
                                for(int i = 0;i<response.length();i++) {
                                    JSONArray array = response.getJSONArray(i);
                                    place = new Place(array.getString(0), array.getString(2), array.getString(4));
                                    placeslist.add(place);
                                }
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
