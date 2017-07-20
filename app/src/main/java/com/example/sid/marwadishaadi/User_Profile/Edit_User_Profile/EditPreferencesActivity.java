package com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.sid.marwadishaadi.App;
import com.example.sid.marwadishaadi.PlacesAdapter;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.Search.BottomSheet;
import com.example.sid.marwadishaadi.User_Profile.UserProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.content.ContentValues.TAG;


public class EditPreferencesActivity extends AppCompatActivity {

    public static  EditText prefannualincome;
    ImageView idoctor, iengineer, imbamca, icacs, ipg, ig, iug, illb;
    boolean intdoctor = false, intengineer = false, intmbamca = false, intcacs = false, intpg = false, intg = false, intug = false, intllb = false;
    TextView tdoctor, tengineer, tmbamca, tcacs, tpg, tg, tug, tllb;
    LinearLayout ldoctor, lengineer, lmbamca, lcacs, lpg, lg, lug, lllb;
    int colorg, colorb;
    CheckBox veryFair, fair, wheatish, wheatishBrown, dark, slim, athletic, heavy, average, professional, job, retired, business, notEmployed, studying, complexionDontMatter, occupationDontMatter, bodyTypeDontMatter;
    AutoCompleteTextView workLocation;
    Spinner heightTo, heightFrom;
    CrystalRangeSeekbar age;
    TextView tvMin;
    TextView tvMax;
    Button complete;
    String mina, maxa, hf, ht, wp, ps, ms, ai;
    String uai;
    Bundle bundle;
    List<String> cp = new ArrayList<>();
    List<String> bt = new ArrayList<>();
    List<String> ep = new ArrayList<>();
    List<String> op = new ArrayList<>();
    String strAnnual;
    String[] strArrayAnnual;
    private Spinner maritalstatus;
    private Spinner physicalstatus;
    private int casebreak;
    private String customer_id;


    public EditPreferencesActivity() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_preferences);

        SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_prefs_toolbar);
        toolbar.setTitle("Edit Partner Preferences");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        age = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbar);
        bundle = getIntent().getExtras();

        complete = (Button) findViewById(R.id.complete);

        idoctor = (ImageView) findViewById(R.id.doctor);
        iengineer = (ImageView) findViewById(R.id.engineer);
        icacs = (ImageView) findViewById(R.id.ca_cs);
        ipg = (ImageView) findViewById(R.id.pg);
        ig = (ImageView) findViewById(R.id.g);
        iug = (ImageView) findViewById(R.id.ug);
        imbamca = (ImageView) findViewById(R.id.mba_mca);
        illb = (ImageView) findViewById(R.id.llb);

        tdoctor = (TextView) findViewById(R.id.text_doctor);
        tengineer = (TextView) findViewById(R.id.text_engineer);
        tmbamca = (TextView) findViewById(R.id.text_mba_mca);
        tcacs = (TextView) findViewById(R.id.text_ca_cs);
        tllb = (TextView) findViewById(R.id.text_llb);
        tpg = (TextView) findViewById(R.id.text_pg);
        tg = (TextView) findViewById(R.id.text_g);
        tug = (TextView) findViewById(R.id.text_ug);

        ldoctor = (LinearLayout) findViewById(R.id.list_doctor);
        lengineer = (LinearLayout) findViewById(R.id.list_engineer);
        lmbamca = (LinearLayout) findViewById(R.id.list_mab_mca);
        lcacs = (LinearLayout) findViewById(R.id.list_ca_cs);
        lllb = (LinearLayout) findViewById(R.id.list_llb);
        lpg = (LinearLayout) findViewById(R.id.list_pg);
        lg = (LinearLayout) findViewById(R.id.list_g);
        lug = (LinearLayout) findViewById(R.id.list_ug);

        workLocation = (AutoCompleteTextView) findViewById(R.id.work_location);

        heightFrom = (Spinner) findViewById(R.id.height_from);
        heightTo = (Spinner) findViewById(R.id.height_to);

        veryFair = (CheckBox) findViewById(R.id.very_fair);
        fair = (CheckBox) findViewById(R.id.fair);
        wheatish = (CheckBox) findViewById(R.id.wheatish);
        wheatishBrown = (CheckBox) findViewById(R.id.wheatish_brown);
        dark = (CheckBox) findViewById(R.id.dark);
        slim = (CheckBox) findViewById(R.id.slim);
        athletic = (CheckBox) findViewById(R.id.athletic);
        heavy = (CheckBox) findViewById(R.id.heavy);
        average = (CheckBox) findViewById(R.id.average);
        professional = (CheckBox) findViewById(R.id.professional);
        job = (CheckBox) findViewById(R.id.job);
        retired = (CheckBox) findViewById(R.id.retired);
        business = (CheckBox) findViewById(R.id.business);
        notEmployed = (CheckBox) findViewById(R.id.not_employed);
        studying = (CheckBox) findViewById(R.id.studying_not_employed);
        complexionDontMatter = (CheckBox) findViewById(R.id.complexion_doesntMatter);
        occupationDontMatter = (CheckBox) findViewById(R.id.occup_doesntMatter);
        bodyTypeDontMatter = (CheckBox) findViewById(R.id.bodyType_doesntMatter);


        tvMin = (TextView) findViewById(R.id.textMin);
        tvMax = (TextView) findViewById(R.id.textMax);

        new FetchEditPartnerPreferences().execute();

        workLocation.setThreshold(1);
        PlacesAdapter placesAdapter = new PlacesAdapter(EditPreferencesActivity.this, R.layout.activity_edit_preferences, R.id.work_location, App.placeslist);
        workLocation.setAdapter(placesAdapter);

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mina = tvMin.getText().toString();
                maxa = tvMax.getText().toString();
                hf = heightFrom.getSelectedItem().toString();
                ht = heightTo.getSelectedItem().toString();


                if (veryFair.isChecked()) {
                    cp.add(veryFair.getText().toString());
                }

                if (fair.isChecked()) {
                    cp.add(fair.getText().toString());
                }
                if (wheatish.isChecked()) {
                    cp.add(wheatish.getText().toString());
                }
                if (wheatishBrown.isChecked()) {
                    cp.add(wheatishBrown.getText().toString());
                }
                if (dark.isChecked()) {
                    cp.add(veryFair.getText().toString());
                }


                if (slim.isChecked()) {
                    bt.add(slim.getText().toString());
                }
                if (athletic.isChecked()) {
                    bt.add(athletic.getText().toString());
                }
                if (average.isChecked()) {
                    bt.add(average.getText().toString());
                }
                if (heavy.isChecked()) {
                    bt.add(heavy.getText().toString());
                }


                if (professional.isChecked()) {
                    op.add(professional.getText().toString());
                }
                if (retired.isChecked()) {
                    op.add(retired.getText().toString());
                }
                if (job.isChecked()) {
                    op.add(job.getText().toString());
                }
                if (business.isChecked()) {
                    op.add(business.getText().toString());
                }
                if (notEmployed.isChecked()) {
                    op.add(notEmployed.getText().toString());
                }
                if (studying.isChecked()) {
                    op.add(studying.getText().toString());
                }

                wp = workLocation.getText().toString();

                if (intdoctor) {
                    ep.add(tdoctor.getText().toString());
                }
                if (intengineer) {
                    ep.add(tengineer.getText().toString());
                }
                if (intmbamca) {
                    ep.add(tmbamca.getText().toString());
                }
                if (intcacs) {
                    ep.add(tcacs.getText().toString());
                }
                if (intllb) {
                    ep.add(tllb.getText().toString());
                }
                if (intpg) {
                    ep.add(tpg.getText().toString());
                }
                if (intg) {
                    ep.add(tg.getText().toString());
                }
                if (intug) {
                    ep.add(tug.getText().toString());
                }

                ps = physicalstatus.getSelectedItem().toString();
                if (ps.equals("Select Physical Status"))
                    ps = "";

                ms = maritalstatus.getSelectedItem().toString();
                if (ms.equals("Select Marital Status"))
                    ms = "";

                SharedPreferences sharedpref = getSharedPreferences("prefai", MODE_PRIVATE);
                uai = sharedpref.getString("ai", null);
                ai = uai;
                
                strArrayAnnual = prefannualincome.getText().toString().split(",");

                new EditPartnerPreferences().execute();


            }
        });

        // get seekbar from view


// get min and max text view

        age.setMinValue(18);
        age.setMaxValue(71);
// set listener
        age.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(minValue));
                tvMax.setText(String.valueOf(maxValue));
            }
        });

// set final value listener
        age.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                
            }
        });

        colorg = Color.parseColor("#" + "fb6542");
        colorb = Color.parseColor("#" + "000000");
        tdoctor.setTextColor(colorb);
        tengineer.setTextColor(colorb);
        tmbamca.setTextColor(colorb);
        tcacs.setTextColor(colorb);
        tllb.setTextColor(colorb);
        tpg.setTextColor(colorb);
        tg.setTextColor(colorb);
        tug.setTextColor(colorb);


        maritalstatus = (Spinner) findViewById(R.id.edit_marital_status);

        prefannualincome = (EditText) findViewById(R.id.prefedit_annual_income);
        prefannualincome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialogFragment btm = new BottomSheet(112, strArrayAnnual);
                btm.show(getSupportFragmentManager(), btm.getTag());

            }
        });
        prefannualincome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                
//                strArrayAnnual = s.toString().split(",");
            }

            @Override
            public void afterTextChanged(Editable s) {
                strArrayAnnual = s.toString().replace(", ", ",").split(",");
            }
        });
        physicalstatus = (Spinner) findViewById(R.id.edit_physical_status);


        ldoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (intdoctor) {
                    intdoctor = false;
                    tdoctor.setTextColor(colorb);
                    Toast.makeText(getApplicationContext(), "Doctor Removed", Toast.LENGTH_SHORT).show();
                    idoctor.setImageResource(R.drawable.doctor_black);
                } else if (!intdoctor) {
                    intdoctor = true;
                    tdoctor.setTextColor(colorg);
                    Toast.makeText(getApplicationContext(), "Doctor Added", Toast.LENGTH_SHORT).show();
                    idoctor.setImageResource(R.drawable.doctor);
                }
            }
        });
        lengineer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (intengineer) {
                    intengineer = false;
                    tengineer.setTextColor(colorb);
                    Toast.makeText(getApplicationContext(), "Engineer Removed", Toast.LENGTH_SHORT).show();
                    iengineer.setImageResource(R.drawable.engineer_black);
                } else if (!intengineer) {
                    intengineer = true;
                    tengineer.setTextColor(colorg);
                    Toast.makeText(getApplicationContext(), "Engineer Added", Toast.LENGTH_SHORT).show();
                    iengineer.setImageResource(R.drawable.engineer);
                }
            }
        });

        lmbamca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (intmbamca) {
                    intmbamca = false;
                    tmbamca.setTextColor(colorb);
                    Toast.makeText(getApplicationContext(), "MBA/MCA Removed", Toast.LENGTH_SHORT).show();
                    imbamca.setImageResource(R.drawable.mba_black);
                } else if (!intmbamca) {
                    intmbamca = true;
                    tmbamca.setTextColor(colorg);
                    Toast.makeText(getApplicationContext(), "MBA/MCA Added", Toast.LENGTH_SHORT).show();
                    imbamca.setImageResource(R.drawable.mba);
                }
            }
        });
        lcacs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (intcacs) {
                    intcacs = false;
                    tcacs.setTextColor(colorb);
                    Toast.makeText(getApplicationContext(), "CA/CS Removed", Toast.LENGTH_SHORT).show();
                    icacs.setImageResource(R.drawable.ca_black);
                } else if (!intcacs) {
                    intcacs = true;
                    tcacs.setTextColor(colorg);
                    Toast.makeText(getApplicationContext(), "CA/CS Added", Toast.LENGTH_SHORT).show();
                    icacs.setImageResource(R.drawable.ca);
                }
            }
        });
        lpg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (intpg) {
                    intpg = false;
                    tpg.setTextColor(colorb);
                    Toast.makeText(getApplicationContext(), "PostGraduate Removed", Toast.LENGTH_SHORT).show();
                    ipg.setImageResource(R.drawable.mba_black);
                } else if (!intpg) {
                    intpg = true;
                    tpg.setTextColor(colorg);
                    Toast.makeText(getApplicationContext(), "PostGraduate Added", Toast.LENGTH_SHORT).show();
                    ipg.setImageResource(R.drawable.mba);
                }
            }
        });
        lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (intg) {
                    intg = false;
                    tg.setTextColor(colorb);
                    Toast.makeText(getApplicationContext(), "Graduate Removed", Toast.LENGTH_SHORT).show();
                    ig.setImageResource(R.drawable.grad_black);
                } else if (!intg) {
                    intg = true;
                    tg.setTextColor(colorg);
                    Toast.makeText(getApplicationContext(), "Graduate Added", Toast.LENGTH_SHORT).show();
                    ig.setImageResource(R.drawable.grad);
                }
            }
        });
        lug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (intug) {
                    intug = false;
                    tug.setTextColor(colorb);
                    Toast.makeText(getApplicationContext(), "UnderGraduate Removed", Toast.LENGTH_SHORT).show();
                    iug.setImageResource(R.drawable.undergrad_black);
                } else if (!intug) {
                    intug = true;
                    Toast.makeText(getApplicationContext(), "UnderGraduate Added", Toast.LENGTH_SHORT).show();
                    tug.setTextColor(colorg);
                    iug.setImageResource(R.drawable.undergrad);
                }
            }
        });
        lllb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (intllb) {
                    intllb = false;
                    tllb.setTextColor(colorb);
                    Toast.makeText(getApplicationContext(), "LLB Removed", Toast.LENGTH_SHORT).show();
                    illb.setImageResource(R.drawable.llb_black);
                } else if (!intllb) {
                    intllb = true;
                    Toast.makeText(getApplicationContext(), "LLB Added", Toast.LENGTH_SHORT).show();
                    tllb.setTextColor(colorg);
                    illb.setImageResource(R.drawable.llb);
                }
            }
        });
        idoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (intdoctor) {
                    intdoctor = false;
                    tdoctor.setTextColor(colorb);
                    Toast.makeText(getApplicationContext(), "Doctor Removed", Toast.LENGTH_SHORT).show();
                    idoctor.setImageResource(R.drawable.doctor_black);
                } else if (!intdoctor) {
                    intdoctor = true;
                    tdoctor.setTextColor(colorg);
                    Toast.makeText(getApplicationContext(), "Doctor Added", Toast.LENGTH_SHORT).show();
                    idoctor.setImageResource(R.drawable.doctor);
                }
            }
        });
        iengineer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (intengineer) {
                    intengineer = false;
                    tengineer.setTextColor(colorb);
                    Toast.makeText(getApplicationContext(), "Engineer Removed", Toast.LENGTH_SHORT).show();
                    iengineer.setImageResource(R.drawable.engineer_black);
                } else if (!intengineer) {
                    intengineer = true;
                    tengineer.setTextColor(colorg);
                    Toast.makeText(getApplicationContext(), "Engineer Added", Toast.LENGTH_SHORT).show();
                    iengineer.setImageResource(R.drawable.engineer);
                }
            }
        });
        imbamca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (intmbamca) {
                    intmbamca = false;
                    tmbamca.setTextColor(colorb);
                    Toast.makeText(getApplicationContext(), "MBA/MCA Removed", Toast.LENGTH_SHORT).show();
                    imbamca.setImageResource(R.drawable.mba_black);
                } else if (!intmbamca) {
                    intmbamca = true;
                    tmbamca.setTextColor(colorg);
                    Toast.makeText(getApplicationContext(), "MBA/MCA Added", Toast.LENGTH_SHORT).show();
                    imbamca.setImageResource(R.drawable.mba);
                }
            }
        });
        icacs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (intcacs) {
                    intcacs = false;
                    tcacs.setTextColor(colorb);
                    Toast.makeText(getApplicationContext(), "CA/CS Removed", Toast.LENGTH_SHORT).show();
                    icacs.setImageResource(R.drawable.ca_black);
                } else if (!intcacs) {
                    intcacs = true;
                    tcacs.setTextColor(colorg);
                    Toast.makeText(getApplicationContext(), "CA/CS Added", Toast.LENGTH_SHORT).show();
                    icacs.setImageResource(R.drawable.ca);
                }
            }
        });
        ipg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (intpg) {
                    intpg = false;
                    tpg.setTextColor(colorb);
                    Toast.makeText(getApplicationContext(), "PostGraduate Removed", Toast.LENGTH_SHORT).show();
                    ipg.setImageResource(R.drawable.mba_black);
                } else if (!intpg) {
                    intpg = true;
                    tpg.setTextColor(colorg);
                    Toast.makeText(getApplicationContext(), "PostGraduate Added", Toast.LENGTH_SHORT).show();
                    ipg.setImageResource(R.drawable.mba);
                }
            }
        });
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (intg) {
                    intg = false;
                    tg.setTextColor(colorb);
                    Toast.makeText(getApplicationContext(), "Graduate Removed", Toast.LENGTH_SHORT).show();
                    ig.setImageResource(R.drawable.grad_black);
                } else if (!intg) {
                    intg = true;
                    tg.setTextColor(colorg);
                    Toast.makeText(getApplicationContext(), "Graduate Added", Toast.LENGTH_SHORT).show();
                    ig.setImageResource(R.drawable.grad);
                }
            }
        });
        iug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (intug) {
                    intug = false;
                    tug.setTextColor(colorb);
                    Toast.makeText(getApplicationContext(), "UnderGraduate Removed", Toast.LENGTH_SHORT).show();
                    iug.setImageResource(R.drawable.undergrad_black);
                } else if (!intug) {
                    intug = true;
                    Toast.makeText(getApplicationContext(), "UnderGraduate Added", Toast.LENGTH_SHORT).show();
                    tug.setTextColor(colorg);
                    iug.setImageResource(R.drawable.undergrad);
                }
            }
        });
        illb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (intllb) {
                    intllb = false;
                    tllb.setTextColor(colorb);
                    Toast.makeText(getApplicationContext(), "LLB Removed", Toast.LENGTH_SHORT).show();
                    illb.setImageResource(R.drawable.llb_black);
                } else if (!intllb) {
                    intllb = true;
                    Toast.makeText(getApplicationContext(), "LLB Added", Toast.LENGTH_SHORT).show();
                    tllb.setTextColor(colorg);
                    illb.setImageResource(R.drawable.llb);
                }
            }
        });
    }

    public int getCasebreak() {
        return this.casebreak;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    private class FetchEditPartnerPreferences extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            AndroidNetworking.post("http://208.91.199.50:5000/profilePartnerPreferences")
                    .addBodyParameter("customerNo", customer_id)
                    .setTag(this)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {

                                tvMin.setText(response.getString(0));
                                tvMax.setText(response.getString(1));
                                age.setMinStartValue(Float.valueOf(response.getString(0)));
                                age.setMaxStartValue(Float.valueOf(response.getString(1)));


                                String[] hf = getResources().getStringArray(R.array.height_array);

                                for (String s : hf) {

                                    if (s.contains(response.getString(2))) {
                                        heightFrom.setSelection(Arrays.asList(hf).indexOf(s));
                                    }
                                }

                                for (String s : hf) {

                                    if (s.contains(response.getString(3))) {
                                        heightTo.setSelection(Arrays.asList(hf).indexOf(s));
                                    }
                                }

                                String str = response.getString(4).replace("[", "").replace("]", "");
                                String[] strArray = str.split(",");


                                if (strArray == null) {

                                    complexionDontMatter.setChecked(true);
                                } else
                                    for (int j = 0; j < strArray.length; j++) {
                                        {

                                            if (strArray[j].contains(veryFair.getText().toString())) {
                                                veryFair.setChecked(true);

                                            } else if (strArray[j].contains(fair.getText().toString())) {
                                                fair.setChecked(true);

                                            } else if (strArray[j].contains(dark.getText().toString())) {
                                                dark.setChecked(true);

                                            } else if (strArray[j].contains(wheatishBrown.getText().toString())) {
                                                wheatishBrown.setChecked(true);

                                            } else if (strArray[j].contains(wheatish.getText().toString())) {
                                                wheatish.setChecked(true);

                                            }

                                        }
                                    }


                                str = response.getString(5).replace("[", "").replace("]", "");
                                strArray = str.split(",");

                                if (strArray == null) {

                                    bodyTypeDontMatter.setChecked(true);
                                } else
                                    for (int j = 0; j < strArray.length; j++) {


                                        if (strArray[j].contains(slim.getText().toString())) {
                                            slim.setChecked(true);

                                        } else if (strArray[j].contains(athletic.getText().toString())) {
                                            athletic.setChecked(true);

                                        } else if (strArray[j].contains(heavy.getText().toString())) {
                                            heavy.setChecked(true);

                                        } else if (strArray[j].contains(average.getText().toString())) {
                                            average.setChecked(true);

                                        }

                                    }


                                workLocation.setText(response.getString(7));


                                str = response.getString(8).replace("[", "").replace("]", "");
                                strArray = str.split(",");

                                if (strArray == null) {


                                } else
                                    for (int j = 0; j < strArray.length; j++) {

                                        if (strArray[j].contains(tdoctor.getText().toString())) {
                                            intdoctor = true;
                                            tdoctor.setTextColor(colorg);
                                            idoctor.setImageResource(R.drawable.doctor);
                                        } else if (strArray[j].contains(tengineer.getText().toString())) {
                                            intengineer = true;
                                            tengineer.setTextColor(colorg);
                                            iengineer.setImageResource(R.drawable.engineer);
                                        } else if (strArray[j].contains(tcacs.getText().toString())) {
                                            intcacs = true;
                                            tcacs.setTextColor(colorg);
                                            icacs.setImageResource(R.drawable.ca);
                                        } else if (strArray[j].contains(tmbamca.getText().toString())) {
                                            intmbamca = true;
                                            tmbamca.setTextColor(colorg);
                                            imbamca.setImageResource(R.drawable.mba);

                                        } else if (strArray[j].contains(tllb.getText().toString())) {
                                            intllb = true;
                                            tllb.setTextColor(colorg);
                                            illb.setImageResource(R.drawable.llb);

                                        } else if (strArray[j].contains(tpg.getText().toString())) {
                                            intpg = true;
                                            tpg.setTextColor(colorg);
                                            ipg.setImageResource(R.drawable.mba);

                                        } else if (strArray[j].contains(tg.getText().toString())) {
                                            intg = true;
                                            tg.setTextColor(colorg);
                                            ig.setImageResource(R.drawable.grad);

                                        } else if (strArray[j].contains(tug.getText().toString())) {
                                            intug = true;
                                            tug.setTextColor(colorg);
                                            iug.setImageResource(R.drawable.undergrad);

                                        }

                                    }


                                str = response.getString(9).replace("[", "").replace("]", "");
                                strArray = str.split(",");
                                
                                if (strArray == null) {

                                    occupationDontMatter.setChecked(true);
                                } else
                                    for (int j = 0; j < strArray.length; j++) {
                                        {

                                            if (professional.getText().toString().contains(strArray[j])) {
                                                professional.setChecked(true);

                                            } else if (strArray[j].contains(job.getText().toString())) {
                                                job.setChecked(true);

                                            } else if (strArray[j].contains(retired.getText().toString())) {
                                                retired.setChecked(true);

                                            } else if (strArray[j].contains(business.getText().toString())) {
                                                business.setChecked(true);

                                            } else if (strArray[j].contains(notEmployed.getText().toString())) {
                                                notEmployed.setChecked(true);

                                            } else if (strArray[j].contains(studying.getText().toString())) {
                                                studying.setChecked(true);

                                            }
                                        }
                                    }
                                String[] physicalArray = getResources().getStringArray(R.array.physicalstatus_array);

                                for (String s : physicalArray) {


                                    if (s.equals(response.getString(6))) {
                                        physicalstatus.setSelection(Arrays.asList(physicalArray).indexOf(s));
                                    }
                                }

                                String[] maritalArray = getResources().getStringArray(R.array.status_array);

                                for (String s : maritalArray) {


                                    if (s.equals(response.getString(10))) {
                                        maritalstatus.setSelection(Arrays.asList(maritalArray).indexOf(s));
                                    }
                                }



                                prefannualincome.setText(response.getString(11).replace("[", "").replace("]", "").replace("\"", "").replace("000000","0L").replace("00000", "L"));


                                strArrayAnnual = prefannualincome.getText().toString().split(",");



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        @Override
                        public void onError(ANError error) {

                        }
                    });

            return null;

        }

    }

    private class EditPartnerPreferences extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
            startActivity(i);
        }

        @Override
        protected Void doInBackground(Void... params) {

            String complexion = cp.toString();
            String education = ep.toString();
            String occupation = op.toString();
            String bodytype = bt.toString();
            String annualincome;
            if (ai == null) {
                annualincome = "";
            } else {
                annualincome = ai.replace("[", "").replace("]", "");
            }
            

            AndroidNetworking.post("http://208.91.199.50:5000/editPreferences")
                    .addBodyParameter("customerNo", customer_id)
                    .addBodyParameter("minAge", mina)
                    .addBodyParameter("maxAge", maxa)
                    .addBodyParameter("heightFrom", hf)
                    .addBodyParameter("heightTo", ht)
                    .addBodyParameter("complexionPref", complexion)
                    .addBodyParameter("bodyType", bodytype)
                    .addBodyParameter("physicalStatus", ps)
                    .addBodyParameter("workLocationPref", wp)
                    .addBodyParameter("educationPref", education)
                    .addBodyParameter("occupationPref", occupation)
                    .addBodyParameter("maritalStatus", ms)
                    .addBodyParameter("annualIncome", annualincome)
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
