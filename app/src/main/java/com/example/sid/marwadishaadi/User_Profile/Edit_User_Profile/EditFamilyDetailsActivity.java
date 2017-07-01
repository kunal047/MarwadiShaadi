package com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.User_Profile.ProfileFamilyDetailsFragment;
import com.example.sid.marwadishaadi.User_Profile.UserProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EditFamilyDetailsActivity extends AppCompatActivity {

    EditText fatherName, mamaSurname, fatherOccupationDetails, grandfatherName, nativePlace, subcaste;
    Spinner fatherOccupation, familyStatus, familyType, familyValues;
    Button updateFamily;
    String f, fo, fod, fs, ft, fv, gn, ms, n, sc;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_family_details);

        fatherName = (EditText) findViewById(R.id.edit_father_name);
        mamaSurname = (EditText) findViewById(R.id.edit_mama_surname);
        grandfatherName = (EditText) findViewById(R.id.edit_grandfather_name);
        nativePlace = (EditText) findViewById(R.id.edit_native_place);
        subcaste = (EditText) findViewById(R.id.edit_subcaste);
        fatherOccupationDetails = (EditText) findViewById(R.id.edit_father_occupation_details);

        fatherOccupation = (Spinner) findViewById(R.id.edit_father_occupation);
        familyStatus = (Spinner) findViewById(R.id.edit_family_status);
        familyType = (Spinner) findViewById(R.id.edit_family_type);
        familyValues = (Spinner) findViewById(R.id.edit_family_values);

        updateFamily = (Button) findViewById(R.id.update_family);

        new FetchFamilyDetailsFamily().execute();

        updateFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                f = fatherName.getText().toString();
                fo = fatherOccupation.getSelectedItem().toString();
                fod = fatherOccupationDetails.getText().toString();
                fs = familyStatus.getSelectedItem().toString();
                ft = familyType.getSelectedItem().toString();
                fv = familyValues.getSelectedItem().toString();
                ms = mamaSurname.getText().toString();
                gn = grandfatherName.getText().toString();
                n = nativePlace.getText().toString();
                sc = subcaste.getText().toString();


                new EditFamilyDetailsFamily().execute();

                Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(i);



            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_fdetails_toolbar);
        toolbar.setTitle("Edit Family Details");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    class FetchFamilyDetailsFamily extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            AndroidNetworking.post("http://192.168.43.143:5050/profileFamilyDetails")
                    .addBodyParameter("customerNo", "A1028")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {

                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray(0);

                                fatherName.setText(jsonArray.getString(0));
                                mamaSurname.setText(jsonArray.getString(9));
                                grandfatherName.setText(jsonArray.getString(8));
                                fatherOccupationDetails.setText(jsonArray.getString(2));
                                nativePlace.setText(jsonArray.getString(6));
                                subcaste.setText(jsonArray.getString(7));


                                String[] fatherOccupationArray = getResources().getStringArray(R.array.father_occupation_array);


                                for (String s : fatherOccupationArray) {
                                    if (jsonArray.getString(1).equals(s)) {
                                        fatherOccupation.setSelection(Arrays.asList(fatherOccupationArray).indexOf(s));
                                    }
                                }

                                String[] familyStatusArray = getResources().getStringArray(R.array.fstatus_array);

                                for (String s : familyStatusArray) {
                                    if (jsonArray.getString(3).equals(s)) {
                                        familyStatus.setSelection(Arrays.asList(familyStatusArray).indexOf(s));

                                    }
                                }

                                String[] familyTypeArray = getResources().getStringArray(R.array.ftype_array);

                                for (String s : familyTypeArray) {
                                    if (jsonArray.getString(4).equals(s)) {
                                        familyType.setSelection(Arrays.asList(familyTypeArray).indexOf(s));

                                    }
                                }

                                String[] familyValueArray = getResources().getStringArray(R.array.fvalues_array);

                                for (String s : familyValueArray) {
                                    if (jsonArray.getString(5).equals(s)) {
                                        familyValues.setSelection(Arrays.asList(familyValueArray).indexOf(s));

                                    }
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

    class EditFamilyDetailsFamily extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            AndroidNetworking.post("http://192.168.43.143:5050/editFamilyDetailsFamily")
                    .addBodyParameter("customerNo", "A1028")
                    .addBodyParameter("fatherName", f)
                    .addBodyParameter("fatherOccupation", fo)
                    .addBodyParameter("fatherOccupationDetails", fod)
                    .addBodyParameter("familyStatus", fs)
                    .addBodyParameter("familyType", ft)
                    .addBodyParameter("familyValues", fv)
                    .addBodyParameter("grandfatherName", gn)
                    .addBodyParameter("mamaSurname", ms)
                    .addBodyParameter("native", n)
                    .addBodyParameter("subcaste", sc)
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
