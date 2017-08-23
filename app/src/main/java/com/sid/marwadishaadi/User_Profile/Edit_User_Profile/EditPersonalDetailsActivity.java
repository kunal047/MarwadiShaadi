package com.sid.marwadishaadi.User_Profile.Edit_User_Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.sid.marwadishaadi.App;
import com.sid.marwadishaadi.PlacesAdapter;
import com.sid.marwadishaadi.R;
import com.sid.marwadishaadi.User_Profile.UserProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class EditPersonalDetailsActivity extends AppCompatActivity {
    Spinner maritalStatus,height,physcialStatus,complexion,built;
    EditText contactNumber, weight;
    AutoCompleteTextView location;
    String customer_id;

    String  ms,h,c,l,w,ps,co,b;
    private PlacesAdapter placesAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_details);

        SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_pdetails_toolbar);
        toolbar.setTitle("Edit Personal Details");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Button update=(Button) findViewById(R.id.advnext);
        maritalStatus=(Spinner) findViewById(R.id.marital_status);
        height=(Spinner) findViewById(R.id.edit_height);

        physcialStatus=(Spinner) findViewById(R.id.physical_status);
        complexion=(Spinner) findViewById(R.id.edit_complexion);
        built=(Spinner)findViewById(R.id.built);
        contactNumber=(EditText) findViewById(R.id.mobile);
        weight=(EditText) findViewById(R.id.weight);


        // autocomplete location fetch
        location = (AutoCompleteTextView) findViewById(R.id.location);
        location.setThreshold(1);
        placesAdapter = new PlacesAdapter(EditPersonalDetailsActivity.this, R.layout.activity_edit_personal_details, R.id.location, App.placeslist);
        location.setAdapter(placesAdapter);

        new FetchPersonalIndividualDetails().execute();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ms = maritalStatus.getSelectedItem().toString();
                h = height.getSelectedItem().toString();
                //Toast.makeText(EditPersonalDetailsActivity.this, h, Toast.LENGTH_SHORT).show();
                c = contactNumber.getText().toString();
                l = location.getText().toString();
                w = weight.getText().toString();

                ps = physcialStatus.getSelectedItem().toString();
                co = complexion.getSelectedItem().toString();
                b = built.getSelectedItem().toString();

                new EditIndividualDetails().execute();
                Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(i);
            }
        });



    }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        finish();
        return true;
    }




    class FetchPersonalIndividualDetails extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            AndroidNetworking.post("http://208.91.199.50:5000/profilePersonalDetails")
                    .addBodyParameter("customerNo",customer_id)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {

                        @Override
                        public void onResponse(JSONArray response) {


                            try {
                                JSONArray array = response.getJSONArray(0);

                                String[] marital_array = getResources().getStringArray(R.array.status_array);
                                String[] height_array = getResources().getStringArray(R.array.height_array);
                                String[] physicalStatus_array = getResources().getStringArray(R.array.physicalstatus_array);
                                String[] complexion_array = getResources().getStringArray(R.array.complexion_array);
                                String[] built_array = getResources().getStringArray(R.array.built_array);

                                for (String s:marital_array){
                                    if (array.getString(3).equals(s))

                                    {
                                        maritalStatus.setSelection(Arrays.asList(marital_array).indexOf(s));
                                    }
                                }


                                for (String s:height_array){
                                    if (array.getString(8).equals(s))

                                    {
                                        height.setSelection(Arrays.asList(height_array).indexOf(s));
                                    }
                                }


                                for (String s:complexion_array){
                                    if (array.getString(10).equals(s))

                                    {
                                        complexion.setSelection(Arrays.asList(complexion_array).indexOf(s));
                                    }
                                }


                                for (String s:built_array){
                                    if (array.getString(11).equals(s))

                                    {
                                        built.setSelection(Arrays.asList(built_array).indexOf(s));
                                    }
                                }


                                for (String s:physicalStatus_array){
                                    if (array.getString(12).equals(s))

                                    {
                                        physcialStatus.setSelection(Arrays.asList(physicalStatus_array).indexOf(s));
                                    }
                                }

                                contactNumber.setText(array.getString(6));
                                location.setText(array.getString(5));
                                weight.setText(array.getString(9));





                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ANError error){}

                    });
                    return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }

    public class EditIndividualDetails extends AsyncTask<Void,Void,Void>{
                @Override
                protected Void doInBackground(Void... params){



                    AndroidNetworking.post("http://208.91.199.50:5000/editPersonalIndividualDetails")
                            .addBodyParameter("customerNo", customer_id)
                            .addBodyParameter("marrital_status",ms)
                            .addBodyParameter("height",h)
                            .addBodyParameter("mobile_phone",c)
                            .addBodyParameter("address",l)
                            .addBodyParameter("weight",w)
                            .addBodyParameter("special_cases",ps)
                            .addBodyParameter("complexion",co)
                            .addBodyParameter("body_structure",b)
                            .setTag(this)
                            .setPriority(Priority.HIGH)
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

    }


}




