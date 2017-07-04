package com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.User_Profile.UserProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.content.ContentValues.TAG;


public class EditPersonalDetailsActivity extends AppCompatActivity {
    Spinner maritalStatus,height,physcialStatus,complexion,built;
    EditText contactNumber, weight;
    AutoCompleteTextView location;
    String  ms,h,c,l,w,ps,co,b;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_details);

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
        location = (AutoCompleteTextView) findViewById(R.id.location);

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
        finish();
        return true;
    }

    class FetchPersonalIndividualDetails extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            AndroidNetworking.post("http://192.168.43.143:5050/profilePersonalDetails")
                    .addBodyParameter("customerNo","A1028")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, "onResponse: ******************in personal details");

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




                                Log.d(TAG, "onPostExecute: height is *******" +h);
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



                    AndroidNetworking.post("http://192.168.43.143:5050/editPersonalIndividualDetails")
                            .addBodyParameter("customerNo", "A1028")
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
                                    Log.d(TAG, "onResponse: height is *************"+h);
                                }

                                @Override
                                public void onError(ANError anError) {

                                }
                            });



            return null;
        }

    }
}




