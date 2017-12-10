package com.sid.marwadishaadi.Search;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
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
import com.sid.marwadishaadi.App;
import com.sid.marwadishaadi.CityAdapter;
import com.sid.marwadishaadi.Constants;
import com.sid.marwadishaadi.Dashboard_Suggestions.SuggestionModel;
import com.sid.marwadishaadi.Place;
import com.sid.marwadishaadi.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Search extends AppCompatActivity {

    public static EditText maritalstatus;
    public static EditText familystatus;
    public static EditText annualincome;
    public static EditText physicalstatus;
    public static EditText spinnerCastSearch;
    public static int countmaritalstatus = 0, countfamilystatus = 0, countannualincome = 0, countphysicalstatus = 0, countspinnerCastSearch = 0;
    public static List<SuggestionModel> suggestionModelList2;
    public static List<String> CastList = new ArrayList<>();
    public static List<String> familystatusList = new ArrayList<>();
    public static List<String> maritalstatusList = new ArrayList<>();
    public static List<String> AIList = new ArrayList<>();
    public static List<String> physicalstatusList = new ArrayList<>();
    static String addTextState, addPrevious = "";
    static String addTextcity, addPreviousc = "";
    private static int casebreak;
    public ProgressDialog dialog;
    public List<String> complexion = new ArrayList<>();
    public List<String> occupation = new ArrayList<>();
    public List<String> statesList = new ArrayList<>();
    public List<String> cityList = new ArrayList<>();
    public List<String> education = new ArrayList<>();
    public List<String> bodyType = new ArrayList<>();
    public List<String> complexionAll = new ArrayList<>();
    public List<String> occupationAll = new ArrayList<>();
    public List<String> educationAll = new ArrayList<>();
    public List<String> bodyTypeAll = new ArrayList<>();
    ImageView idoctor, iengineer, imbamca, icacs, ipg, ig, iug, illb;
    boolean intdoctor = false, intengineer = false, intmbamca = false, intcacs = false, intpg = false, intg = false, intug = false, intllb = false;
    TextView tdoctor, tengineer, tmbamca, tcacs, tpg, tg, tug, tllb;
    LinearLayout ldoctor, lengineer, lmbamca, lcacs, lpg, lg, lug, lllb;
    int colorg, colorb;
    Button mOpenIDSearchButton;
    TextView statetextView, citytextview;
    CardView advCV;
    TextView tvMin, tvMax;
    Button addButton, searchaddbutton, searchRemoveButtonForCity, stateRemoveButton;
    AutoCompleteTextView autoCompleteState, autocompletecity;
    Bundle bundle;
    boolean int_very_fair, int_fair, int_wheatish, int_wheatish_brown, int_dark, int_doesnt_matter = true, int_profession, int_job, int_retired, int_business, int_not_employed, int_studying, int_dont_matter = true, intSlim, intAthletic, intHeavy, intAverage, intDoesntMatter = true;
    CheckBox very_fair, fair, wheatish, wheatish_brown, dark, doesnt_matter, profession, job, retired, business, not_employed, studying, dont_matter, slim, athletic, average, heavy, doesntMatter;
    List<String> cityAutoCompleteList = new ArrayList<>();
    List<String> stateAutoCompleteList = new ArrayList<>();
    CityAdapter cityAdapter;
    Spinner height_from, height_to, sort_by, manglik, children;
    private String customer_id;

    public Search() {

    }

    public void init() {
        String[] ar = getResources().getStringArray(R.array.complexion_array);
        for (int i = 1; i < ar.length; i++) {
            complexionAll.add(ar[i]);
        }
        ar = getResources().getStringArray(R.array.built_array);
        for (int i = 1; i < ar.length; i++) {
            bodyTypeAll.add(ar[i]);
        }
        ar = getResources().getStringArray(R.array.education_array);
        for (int i = 1; i < ar.length; i++) {
            educationAll.add(ar[i]);
        }
        ar = getResources().getStringArray(R.array.occupation_array);
        for (int i = 1; i < ar.length; i++) {
            occupationAll.add(ar[i]);
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        suggestionModelList2 = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.search_toolbar);
        toolbar.setTitle("Search");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String[] community = getResources().getStringArray(R.array.communities);
        SharedPreferences communityPackage = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        CastList.clear();
        final SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);

        SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);

        for (int i = 0; i < 5; i++) {
            if (community[i].trim().toCharArray()[0] == customer_id.trim().toCharArray()[0]) {
                CastList.add(community[i]);
            } else if (communityPackage.getString(community[i], null).contains("Yes")) {
                CastList.add(community[i]);
            }

        }

        spinnerCastSearch = findViewById(R.id.search_user_caste);
        String community_text = CastList.toString().replace("[", "").replace("]", "");
        spinnerCastSearch.setText(community_text);
        init();
        height_from = findViewById(R.id.height_from);
        height_to = findViewById(R.id.height_to);
        sort_by = findViewById(R.id.spinner_sort_by);
        manglik = findViewById(R.id.search_manglik_status);
        children = findViewById(R.id.search_children);

        idoctor = findViewById(R.id.doctor);
        iengineer = findViewById(R.id.engineer);
        icacs = findViewById(R.id.ca_cs);
        ipg = findViewById(R.id.pg);
        ig = findViewById(R.id.g);
        iug = findViewById(R.id.ug);
        imbamca = findViewById(R.id.mba_mca);
        illb = findViewById(R.id.llb);

        tdoctor = findViewById(R.id.text_doctor);
        tengineer = findViewById(R.id.text_engineer);
        tmbamca = findViewById(R.id.text_mba_mca);
        tcacs = findViewById(R.id.text_ca_cs);
        tllb = findViewById(R.id.text_llb);
        tpg = findViewById(R.id.text_pg);
        tg = findViewById(R.id.text_g);
        tug = findViewById(R.id.text_ug);

        ldoctor = findViewById(R.id.list_doctor);
        lengineer = findViewById(R.id.list_engineer);
        lmbamca = findViewById(R.id.list_mab_mca);
        lcacs = findViewById(R.id.list_ca_cs);
        lllb = findViewById(R.id.list_llb);
        lpg = findViewById(R.id.list_pg);
        lg = findViewById(R.id.list_g);
        lug = findViewById(R.id.list_ug);
        occupation.add("Does not matter");
        bodyType.add("Does not matter");
        complexion.add("Does not matter");
        education.add("Does not matter");

        maritalstatus = findViewById(R.id.search_Marital_status);
        maritalstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak = 3;
                BottomSheetDialogFragment btm = null;
                if (sharedPreferences != null) {
                    if (sharedPreferences.getString("maritalStatusArray", null) != null && sharedPreferences.getString("maritalStatusArray", null).trim().length() > 0) {
                        String[] arr = sharedPreferences.getString("maritalStatusArray", null).replace("[", "").replace("]", "").split(", ");

                        btm = new BottomSheet(0, arr);
                    } else
                        btm = new BottomSheet(0);
                } else
                    btm = new BottomSheet(0);
                btm.show(getSupportFragmentManager(), btm.getTag());
            }
        });

        familystatus = findViewById(R.id.search_Family_status);
        familystatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak = 4;
                BottomSheetDialogFragment btm = null;
                if (sharedPreferences != null) {
                    if (sharedPreferences.getString("familyStatusArray", null) != null && sharedPreferences.getString("familyStatusArray", null).trim().length() > 0) {
                        String[] arr = sharedPreferences.getString("familyStatusArray", null).replace("[", "").replace("]", "").split(", ");
                        btm = new BottomSheet(0, arr);
                    } else
                        btm = new BottomSheet(0);
                } else
                    btm = new BottomSheet(0);
                btm.show(getSupportFragmentManager(), btm.getTag());
            }
        });

        annualincome = findViewById(R.id.search_Annual_income);
        annualincome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak = 5;
                BottomSheetDialogFragment btm = null;
                if (sharedPreferences != null) {
                    if (sharedPreferences.getString("annualArray", null) != null && sharedPreferences.getString("annualArray", null).trim().length() > 0) {
                        String[] arr = sharedPreferences.getString("annualArray", null).replace("[", "").replace("]", "").split(", ");
                        btm = new BottomSheet(0, arr);
                    } else
                        btm = new BottomSheet(0);
                } else
                    btm = new BottomSheet(0);
                btm.show(getSupportFragmentManager(), btm.getTag());

            }
        });

        physicalstatus = findViewById(R.id.search_physical_status);
        physicalstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak = 6;
                BottomSheetDialogFragment btm = null;
                if (sharedPreferences != null) {
                    if (sharedPreferences.getString("physicalStatusArray", null) != null && sharedPreferences.getString("physicalStatusArray", null).trim().length() > 0) {
                        String[] arr = sharedPreferences.getString("physicalStatusArray", null).replace("[", "").replace("]", "").split(", ");
                        btm = new BottomSheet(0, arr);
                    } else
                        btm = new BottomSheet(0);
                } else
                    btm = new BottomSheet(0);
                btm.show(getSupportFragmentManager(), btm.getTag());

            }
        });


        addButton = findViewById(R.id.search_add_state);
        stateRemoveButton = findViewById(R.id.search_remove_state);

        searchaddbutton = findViewById(R.id.search_add_city);
        searchRemoveButtonForCity = findViewById(R.id.search_remove_city);


        statetextView = findViewById(R.id.text_view_search_add_state);
        citytextview = findViewById(R.id.text_view_search_add_city);
        autoCompleteState = findViewById(R.id.search_state);
        autocompletecity = findViewById(R.id.search_city);

        for (Place place : App.placeslist) {
            if (!stateAutoCompleteList.contains(place.getState())) {
                stateAutoCompleteList.add(place.getState());
            }
            cityAutoCompleteList.add(place.getCity());
        }


        //autocomplete city and state
        autocompletecity.setThreshold(1);
        cityAdapter = new CityAdapter(getApplicationContext(), R.layout.activity_search, R.id.search_city, cityAutoCompleteList);
        autocompletecity.setAdapter(cityAdapter);

        autoCompleteState.setThreshold(1);
        cityAdapter = new CityAdapter(getApplicationContext(), R.layout.activity_search, R.id.search_state, stateAutoCompleteList);
        autoCompleteState.setAdapter(cityAdapter);

        advCV = findViewById(R.id.advanced_search);
        final CrystalRangeSeekbar rangeSeekbar = findViewById(R.id.rangeSeekbar);
        // get min and max text view
        tvMin = findViewById(R.id.textMin);
        tvMax = findViewById(R.id.textMax);
        rangeSeekbar.setMinValue(18);
        rangeSeekbar.setMaxValue(71);


        // set listener


        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(minValue));
                tvMax.setText(String.valueOf(maxValue));
            }
        });


        // set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {

            }
        });

        stateRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] state = statetextView.getText().toString().split("\\n");
                statesList.remove(state[0]);
                state = Arrays.copyOfRange(state, 1, state.length);
                String results = TextUtils.join("\n", state);
                statetextView.setText(results);
                addPrevious = results;

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTextState = autoCompleteState.getText().toString();
                if (addTextState.trim().isEmpty()) {
                    autoCompleteState.setText("");
                    Toast.makeText(getApplicationContext(), "Please click + button after state selection ", Toast.LENGTH_SHORT).show();
                } else {
                    if (!statesList.contains(addTextState)) {

                        statesList.add(addTextState);
                        statetextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        statetextView.setText((addTextState + "\n" + addPrevious));
                        addPrevious = statetextView.getText().toString();
                        stateRemoveButton.setVisibility(View.VISIBLE);

                    } else {
                        Toast.makeText(Search.this, "Already added", Toast.LENGTH_SHORT).show();
                    }

                    autoCompleteState.setText("");
                }
            }
        });

        searchaddbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTextcity = autocompletecity.getText().toString();
                if (addTextcity.trim().isEmpty()) {
                    autocompletecity.setText("");
                    Toast.makeText(getApplicationContext(), "Please click + button after city selection ", Toast.LENGTH_SHORT).show();
                } else {
                    if (!cityList.contains(addTextcity)) {
                        cityList.add(addTextcity);
                        citytextview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        citytextview.setText(addTextcity + "\n" + addPreviousc);
                        addPreviousc = citytextview.getText().toString();
                        searchRemoveButtonForCity.setVisibility(View.VISIBLE);

                    } else {
                        Toast.makeText(Search.this, "Already added", Toast.LENGTH_SHORT).show();
                    }
                    autocompletecity.setText("");
                }
            }
        });

        searchRemoveButtonForCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] city = citytextview.getText().toString().split("\\n");
                cityList.remove(city[0]);
                city = Arrays.copyOfRange(city, 1, city.length);
                String result = TextUtils.join("\n", city);
                citytextview.setText(result);
                addPreviousc = result;
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


        ldoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (intdoctor) {
                    intdoctor = false;
                    tdoctor.setTextColor(colorb);
                    education.remove(tdoctor.getText().toString());

                    Toast.makeText(getApplicationContext(), "Doctor Removed", Toast.LENGTH_SHORT).show();
                    idoctor.setImageResource(R.drawable.doctor_black);
                } else if (!intdoctor) {
                    intdoctor = true;
                    tdoctor.setTextColor(colorg);
                    education.add(tdoctor.getText().toString());
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
                    education.remove(tengineer.getText().toString());
                    Toast.makeText(getApplicationContext(), "Engineer Removed", Toast.LENGTH_SHORT).show();
                    iengineer.setImageResource(R.drawable.engineer_black);
                } else if (!intengineer) {
                    intengineer = true;
                    tengineer.setTextColor(colorg);
                    education.add(tengineer.getText().toString());

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
                    education.remove(tmbamca.getText().toString());

                    Toast.makeText(getApplicationContext(), "MBA/MCA Removed", Toast.LENGTH_SHORT).show();
                    imbamca.setImageResource(R.drawable.mba_black);
                } else if (!intmbamca) {
                    intmbamca = true;
                    tmbamca.setTextColor(colorg);
                    education.add(tmbamca.getText().toString() + "/MS/MA/MSC/M.Arch");

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
                    education.remove(tcacs.getText().toString() + "/ICWA");

                    Toast.makeText(getApplicationContext(), "CA/CS Removed", Toast.LENGTH_SHORT).show();
                    icacs.setImageResource(R.drawable.ca_black);
                } else if (!intcacs) {
                    intcacs = true;
                    tcacs.setTextColor(colorg);
                    education.add(tcacs.getText().toString() + "/ICWA");

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
                    education.remove(tpg.getText().toString());

                    Toast.makeText(getApplicationContext(), "PostGraduate Removed", Toast.LENGTH_SHORT).show();
                    ipg.setImageResource(R.drawable.mba_black);
                } else if (!intpg) {
                    intpg = true;
                    tpg.setTextColor(colorg);
                    education.add(tpg.getText().toString());

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
                    education.remove(tg.getText().toString());

                    Toast.makeText(getApplicationContext(), "Graduate Removed", Toast.LENGTH_SHORT).show();
                    ig.setImageResource(R.drawable.grad_black);
                } else if (!intg) {
                    intg = true;
                    tg.setTextColor(colorg);
                    education.add(tg.getText().toString());

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
                    education.remove(tug.getText().toString());

                    Toast.makeText(getApplicationContext(), "UnderGraduate Removed", Toast.LENGTH_SHORT).show();
                    iug.setImageResource(R.drawable.undergrad_black);
                } else if (!intug) {
                    intug = true;
                    education.add(tug.getText().toString());

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
                    education.remove(tllb.getText().toString());

                    Toast.makeText(getApplicationContext(), "LLB Removed", Toast.LENGTH_SHORT).show();
                    illb.setImageResource(R.drawable.llb_black);
                } else if (!intllb) {
                    intllb = true;
                    education.add(tllb.getText().toString());

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
                    education.remove(tdoctor.getText().toString());

                    Toast.makeText(getApplicationContext(), "Doctor Removed", Toast.LENGTH_SHORT).show();
                    idoctor.setImageResource(R.drawable.doctor_black);
                } else if (!intdoctor) {
                    intdoctor = true;
                    tdoctor.setTextColor(colorg);
                    education.add(tdoctor.getText().toString());

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
                    education.remove(tengineer.getText().toString());

                    Toast.makeText(getApplicationContext(), "Engineer Removed", Toast.LENGTH_SHORT).show();
                    iengineer.setImageResource(R.drawable.engineer_black);
                } else if (!intengineer) {
                    intengineer = true;
                    tengineer.setTextColor(colorg);
                    education.add(tengineer.getText().toString());

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
                    education.remove(tmbamca.getText().toString() + "/MS/MA/MSC/M.Arch");

                    Toast.makeText(getApplicationContext(), "MBA/MCA Removed", Toast.LENGTH_SHORT).show();
                    imbamca.setImageResource(R.drawable.mba_black);
                } else if (!intmbamca) {
                    intmbamca = true;
                    tmbamca.setTextColor(colorg);
                    education.add(tmbamca.getText().toString() + "/MS/MA/MSC/M.Arch");

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
                    education.remove(tcacs.getText().toString() + "/ICWA");

                    Toast.makeText(getApplicationContext(), "CA/CS Removed", Toast.LENGTH_SHORT).show();
                    icacs.setImageResource(R.drawable.ca_black);
                } else if (!intcacs) {
                    intcacs = true;
                    tcacs.setTextColor(colorg);
                    education.add(tcacs.getText().toString() + "/ICWA");

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
                    education.remove(tpg.getText().toString());

                    Toast.makeText(getApplicationContext(), "PostGraduate Removed", Toast.LENGTH_SHORT).show();
                    ipg.setImageResource(R.drawable.mba_black);
                } else if (!intpg) {
                    intpg = true;
                    tpg.setTextColor(colorg);
                    education.add(tpg.getText().toString());

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
                    education.remove(tg.getText().toString());

                    Toast.makeText(getApplicationContext(), "Graduate Removed", Toast.LENGTH_SHORT).show();
                    ig.setImageResource(R.drawable.grad_black);
                } else if (!intg) {
                    intg = true;
                    tg.setTextColor(colorg);
                    education.add(tg.getText().toString());

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
                    education.remove(tug.getText().toString());

                    Toast.makeText(getApplicationContext(), "UnderGraduate Removed", Toast.LENGTH_SHORT).show();
                    iug.setImageResource(R.drawable.undergrad_black);
                } else if (!intug) {
                    intug = true;
                    education.add(tug.getText().toString());

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
                    education.remove(tllb.getText().toString());

                    Toast.makeText(getApplicationContext(), "LLB Removed", Toast.LENGTH_SHORT).show();
                    illb.setImageResource(R.drawable.llb_black);
                } else if (!intllb) {
                    intllb = true;
                    education.add(tllb.getText().toString());

                    Toast.makeText(getApplicationContext(), "LLB Added", Toast.LENGTH_SHORT).show();
                    tllb.setTextColor(colorg);
                    illb.setImageResource(R.drawable.llb);
                }
            }
        });

        mOpenIDSearchButton = findViewById(R.id.search_by_id_name_open);
        mOpenIDSearchButton.setFocusable(true);
        spinnerCastSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                casebreak = 1;
                BottomSheetDialogFragment btm = null;
                if (sharedPreferences != null) {
                    if (sharedPreferences.getString("communities", null) != null && sharedPreferences.getString("communities", null).trim().length() > 0) {
                        String[] arr = sharedPreferences.getString("communities", null).replace("[", "").replace("]", "").split(", ");
                        btm = new BottomSheet(0, arr);
                    } else
                        btm = new BottomSheet(0);
                } else
                    btm = new BottomSheet(0);
                btm.show(getSupportFragmentManager(), btm.getTag());
            }
        });

        mOpenIDSearchButton = findViewById(R.id.search_by_id_name_open);
        mOpenIDSearchButton.setFocusable(true);
        mOpenIDSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                casebreak = 2;
                //  Toast.makeText(getApplicationContext(),Integer.toString(getCasebreak())+" does this worked or not", Toast.LENGTH_LONG).show();
                BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheet(0);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

            }
        });


        FloatingActionButton search = findViewById(R.id.search_Submit);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                 new BackEnd().execute("","","","","","","","","");
//                Intent i=new Intent(Search.this,SearchResultsActivity.class);
             /*  i.putStringArrayListExtra("value",);
                Toast.makeText(getApplicationContext(),"Education are :------" + education.toString(),Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"Complexion are :------" + complexion.toString(),Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"Body Type are :------" + bodyType.toString(),Toast.LENGTH_LONG).show();

                Toast.makeText(getApplicationContext(),"Occupation are :------" + occupation.toString(),Toast.LENGTH_LONG).show();
                */
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String gender = sharedPreferences.getString("gender", null);
                if (gender.contains("Male")) {

                    gender = "Female";
                } else {

                    gender = "Male";
                }
                String query = "";
                query = "select tbl_user.birthdate,tbl_user.first_name,tbl_user.customer_no,tbl_user.edu_degree, tbl_user.occup_location,tbl_user.height,tbl_user.occup_company,tbl_user.anuual_income,tbl_user.marrital_status,tbl_city.City_name,tbl_user.occup_designation,tbl_user.surname from tbl_user INNER join tbl_state on tbl_state.state_id=tbl_user.state INNER JOIN tbl_login ON tbl_user.customer_no=tbl_login.customer_no inner join tbl_city on tbl_user.city=tbl_city.City_id where ( tbl_login.user_active ='Yes') and (tbl_login.user_deleted='0') and ( tbl_user.gender='" + gender + "' ) ";
//                ON tbl_user.customer_no=tbl_user_files.customer_no
                int year = Calendar.getInstance().get(Calendar.YEAR);
                query += "and (YEAR(tbl_user.birthdate) >=" + Integer.toString(year - Integer.parseInt(tvMax.getText().toString())) + " and YEAR(tbl_user.birthdate)<=" + Integer.toString(year - Integer.parseInt(tvMin.getText().toString())) + ")";
                String s1, s2;
                s1 = height_from.getSelectedItem().toString();
                s2 = height_to.getSelectedItem().toString();


                if (s1.contains("matter") && s2.contains("matter")) {
                    //no code is here *********** remove space from cm in first entry

                } else {
                    if (s1.contains("matter") & !s2.contains("matter")) {
                        query += "and  tbl_user.height<=" + s2.substring(s2.length() - 5, s2.length() - 2);

                    } else if (!s1.contains("matter") & s2.contains("matter")) {
                        query += "and tbl_user.height>=" + s1.substring(s1.length() - 5, s1.length() - 2);
                    } else {
                        query += "and ( tbl_user.height<=" + s2.substring(s2.length() - 5, s2.length() - 2) + " and tbl_user.height>=" + s1.substring(s1.length() - 5, s1.length() - 2) + ")";
                    }
                }


                String str = "";

                str = spinnerCastSearch.getText().toString();
                if (str.contains("[]") || str.equals("")) {
                    //no code is here
//                    String [] community =getResources.getStringArray
                    /*String[] community= getResources().getStringArray(R.array.communities);
                    SharedPreferences communityPackage= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    query+=" and (";
                    for(int i=0;i<4;i++){
                        if(communityPackage.getString(community[i],null).contains("Yes")){
                            query+="tbl_user.customer_no like '"+communityPackage.getString(community[i],null).toCharArray()[0]+"%' or ";
                        }
                    }
                    if(communityPackage.getString(community[4],null).contains("Yes")){
                        query+="tbl_user.customer_no like '"+communityPackage.getString(community[4],null).toCharArray()[0]+"%' ) ";
                    }*/

                } else if (str.contains("matter")) {
                    query += " and (";
                    String[] arrayString = getResources().getStringArray(R.array.caste_array);
                    for (int i = 1; i < arrayString.length - 1; i++) {

                        query += "tbl_user.customer_no like \"" + arrayString[i].toCharArray()[0] + "%\" or ";
                    }
                    query += "tbl_user.customer_no like \"" + arrayString[arrayString.length - 1].toCharArray()[0] + "%\" )  ";
                } else {
                    query += "and ( ";
                    for (int i = 0; i < CastList.size() - 1; i++) {
                        query += "tbl_user.customer_no like \"" + CastList.get(i).toCharArray()[0] + "%\" or ";
                    }
                    query += "tbl_user.customer_no like \"" + CastList.get(CastList.size() - 1).toCharArray()[0] + "%\" )  ";
                }

                str = maritalstatus.getText().toString();

                if (str.equals("[]") || str.equals("")) {
                    //no code is here
                } else if (str.contains("matter")) {
                    query += " and (";
                    String[] arrayString = getResources().getStringArray(R.array.status_search_array);
                    for (int i = 1; i < arrayString.length - 1; i++) {
                        query += "tbl_user.marrital_status =\"" + arrayString[i] + "\" or ";
                    }
                    query += "tbl_user.marrital_status = \"" + arrayString[arrayString.length - 1] + "\")";
                } else {
                    query += "and ( ";
                    for (int i = 0; i < countmaritalstatus - 1; i++) {
                        query += "tbl_user.marrital_status = \"" + maritalstatusList.get(i) + "\" or ";
                    }
                    query += "tbl_user.marrital_status = \"" + maritalstatusList.get(countmaritalstatus - 1) + "\") ";
                }
                str = familystatus.getText().toString();

                if (str.equals("[]") || str.equals("")) {
                    //no code is here
                } else if (str.contains("matter")) {
                    query += " and (";
                    String[] arrayString = getResources().getStringArray(R.array.fstatus_array);
                    for (int i = 1; i < arrayString.length - 1; i++) {
                        query += "tbl_user.family_status = \"" + arrayString[i] + "\" or ";
                    }
                    query += "tbl_user.family_status = \"" + arrayString[arrayString.length - 1] + "\") ";

                } else {
                    query += "and ( ";
                    for (int i = 0; i < countfamilystatus - 1; i++) {
                        query += "tbl_user.family_status = \"" + familystatusList.get(i) + "\" or ";
                    }
                    query += "tbl_user.family_status = \"" + familystatusList.get(countfamilystatus - 1) + "\") ";
                }
                str = annualincome.getText().toString();

                if (str.equals("[]") || str.equals("")) {
                    //no code is here
                } else if (str.contains("matter")) {
                    query += " and (";
                    String[] arrayString = getResources().getStringArray(R.array.aincome_search_array);
                    for (int i = 0; i < arrayString.length; i++) {
                        String string = arrayString[i];
                        String s = string.replace("L", "00000");
                        arrayString[i] = s;

                    }
                    for (int i = 1; i < arrayString.length - 1; i++) {
                        query += "tbl_user.anuual_income = \"" + arrayString[i] + "\" or ";
                    }
                    query += "tbl_user.anuual_income = \"" + arrayString[arrayString.length - 1] + "\") ";
                } else {
                    query += "and ( ";
                    for (int i = 0; i < countannualincome; i++) {
                        String string = AIList.get(i);
                        String s = string.replace("L", "00000");
                        AIList.set(i, s);

                    }
                    for (int i = 0; i < countannualincome - 1; i++) {
                        query += "tbl_user.anuual_income = \"" + AIList.get(i) + "\" or ";
                    }
                    query += "tbl_user.anuual_income = \"" + AIList.get(countannualincome - 1) + "\") ";
                }
                str = physicalstatus.getText().toString();

                if (str.equals("[]") || str.equals("")) {
                    //no code is here
                } else if (str.contains("Does not matter")) {
                    query += " and (";
                    String[] arrayString = getResources().getStringArray(R.array.physicalstatus_search_array);
                    for (int i = 1; i < arrayString.length - 1; i++) {
                        query += "tbl_user.special_cases = \"" + arrayString[i] + "\" or ";
                    }
                    query += "tbl_user.special_cases = \"" + arrayString[arrayString.length - 1] + "\") ";

                } else {
                    query += "and ( ";
                    for (int i = 0; i < countphysicalstatus - 1; i++) {
                        query += "tbl_user.special_cases = \"" + physicalstatusList.get(i) + "\" or ";
                    }
                    query += "tbl_user.special_cases = \"" + physicalstatusList.get(countphysicalstatus - 1) + "\") ";
                }
                if (education.indexOf("matter") != -1 && education.size() != 1) {
                    query += (" and  ( tbl_user.education=\"" + education.get(1).toString() + "\"");
                    for (int i = 2; i < education.size(); i++) {
                        query += (" or tbl_user.education =\"" + education.get(i).toString() + "\"");
                    }

                } else {
                    query += (" and ( tbl_user.education=\"" + educationAll.get(0).toString() + "\"");

                    for (int i = 1; i < educationAll.size(); i++) {
                        query += (" or tbl_user.education=\"" + educationAll.get(i).toString() + "\"");
                    }

                }
                query += ")";

                if (complexion.contains("matter")) {
                    query += (" and ( tbl_user.complexion=\"" + complexion.get(0).toString() + "\"");

                    for (int i = 1; i < complexion.size(); i++) {
                        query += (" or tbl_user.complexion=\"" + complexion.get(i).toString() + "\"");
                    }

                } else {
                    query += (" and ( tbl_user.complexion=\"" + complexionAll.get(0).toString() + "\"");

                    for (int i = 1; i < complexionAll.size(); i++) {
                        query += (" or tbl_user.complexion= \"" + complexionAll.get(i).toString() + "\"");
                    }

                }
                query += ")";

                if (bodyType.contains("matter")) {
                    query += (" and (  tbl_user.body_structure=\"" + complexion.get(0).toString() + "\"");

                    for (int i = 1; i < bodyType.size(); i++) {
                        query += (" or tbl_user.body_structure=\"" + bodyType.get(i).toString() + "\"");
                    }

                } else {
                    query += (" and  ( tbl_user.body_structure=\"" + bodyTypeAll.get(0).toString() + "\"");

                    for (int i = 1; i < bodyTypeAll.size(); i++) {
                        query += (" or tbl_user.body_structure= \"" + bodyTypeAll.get(i).toString() + "\"");
                    }

                }
                query += ")";

                if (occupation.contains("matter")) {
                    query += (" and ( tbl_user.current_occup=\"" + occupation.get(0).toString() + "\"");
                    for (int i = 1; i < occupation.size(); i++) {
                        query += (" or tbl_user.current_occup= \"" + occupation.get(i).toString() + "\"");
                    }

                } else {
                    query += (" and ( tbl_user.current_occup=\"" + occupationAll.get(0).toString() + "\"");
                    for (int i = 1; i < occupationAll.size(); i++) {
                        query += (" or tbl_user.current_occup=\"" + occupationAll.get(i).toString() + "\"");
                    }

                }
                query += ")";

                if (statesList.size() == 0) {
                    //no code here
                } else {
                    query += " and ( ";

                    for (int i = 0; i < statesList.size() - 1; i++) {

                        query += " tbl_user.state=" + "(select tbl_state.state_id from tbl_state where tbl_state.state_name=\"" + statesList.get(i) + "\") or ";
                    }
                    query += " tbl_user.state=" + "(select tbl_state.state_id from tbl_state where tbl_state.state_name=\"" + statesList.get(statesList.size() - 1) + "\" ) ) ";
                }
                if (cityList.size() == 0) {
                    //no code here
                } else {
                    query += " and ( ";

                    for (int i = 0; i < cityList.size() - 1; i++) {

                        query += " tbl_user.city=" + "(select tbl_city.City_id from tbl_city where tbl_city.City_name=\"" + cityList.get(i) + "\") or ";
                    }
                    query += " tbl_user.city=" + "(select tbl_city.City_id from tbl_city where tbl_city.City_name=\"" + cityList.get(cityList.size() - 1) + "\" ) ) ";
                }
                String mangli = manglik.getSelectedItem().toString();
                if (mangli.contains("matter")) {
                    //nothig is here for you
                } else {
                    query += " and ( tbl_user.manglik=\"" + mangli + "\" ) ";
                }
                String childs = children.getSelectedItem().toString();
                if (childs.contains("matter")) {
                    //nthg is here for you
                } else {
                    query += " and ( tbl_user.children=\"" + childs + "\")";
                }

                String itm = sort_by.getSelectedItem().toString();

                if (itm.contains("matter")) {
                    //nothing is here for you
                } else if (itm.equalsIgnoreCase("Recent Profiles")) {
                    query += " order by tbl_user.created_on asc ";
                } else {
                    query += " order by tbl_login.last_activity_on asc ";
                }

                // Adding Bundles
                /* bundle = new Bundle();
                bundle.putString("ageLow",tvMin.getText().toString());
                bundle.putString("ageHigh",tvMax.getText().toString());
                bundle.putString("heightLow",s1);
                bundle.putString("heightHigh",s2);
                bundle.putString("communities",spinnerCastSearch.getText().toString());
                bundle.putStringArrayList("stateArray",(ArrayList<String>)statesList);
                bundle.putStringArrayList("educationArray", (ArrayList<String>) education);
                bundle.putString("sortBy",itm);
                bundle.putStringArrayList("cityArray", (ArrayList<String>) cityList);
                bundle.putString("maritalStatusArray",maritalstatus.getText().toString());
                bundle.putString("familyStatusArray",familystatus.getText().toString());
                bundle.putString("manglik",mangli);
                bundle.putString("children",childs);
                bundle.putStringArrayList("occupationArray", (ArrayList<String>) occupation);
                bundle.putString("annualArray",annualincome.getText().toString());
                bundle.putStringArrayList("complexionArray", (ArrayList<String>) complexion);
                bundle.putStringArrayList("bodyTypeArray", (ArrayList<String>) bodyType);
                bundle.putString("physicalStatusArray",physicalstatus.getText().toString());
*/
                SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedpref.edit();
                editor.putString("ageLow", tvMin.getText().toString());
                editor.putString("ageHigh", tvMax.getText().toString());
                editor.putString("heightLow", s1);
                editor.putString("heightHigh", s2);
                editor.putString("communities", spinnerCastSearch.getText().toString());
                editor.putString("educationArray", education.toString());
                editor.putString("sortBy", itm);
                editor.putString("maritalStatusArray", maritalstatus.getText().toString());
                editor.putString("familyStatusArray", familystatus.getText().toString());
                editor.putString("manglik", mangli);
                editor.putString("children", childs);
                editor.putString("occupationArray", occupation.toString());
                editor.putString("annualArray", annualincome.getText().toString());
                editor.putString("complexionArray", complexion.toString());
                editor.putString("bodyTypeArray", bodyType.toString());
                editor.putString("physicalStatusArray", physicalstatus.getText().toString());
                editor.apply();


                new BackEnd().execute(query);



              /*  Toast.makeText(getApplicationContext(),"Education are :------" + education.toString(),Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"Education are :------" + education.toString(),Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"Education are :------" + education.toString(),Toast.LENGTH_SHORT).show();
*/


            }
        });


        very_fair = findViewById(R.id.search_check_very_fair);
        fair = findViewById(R.id.search_check_fair);
        wheatish = findViewById(R.id.search_check_wheatish);
        wheatish_brown = findViewById(R.id.search_check_wheatish_brown);
        dark = findViewById(R.id.search_check_dark);
        doesnt_matter = findViewById(R.id.complexion_doesnt_matter);

        profession = findViewById(R.id.check_profession);
        job = findViewById(R.id.check_job);
        retired = findViewById(R.id.check_retired);
        business = findViewById(R.id.check_business);
        studying = findViewById(R.id.check_studying_not_employed);
        not_employed = findViewById(R.id.check_not_employed);
        dont_matter = findViewById(R.id.occupation_doesnt_matter);

        slim = findViewById(R.id.search_check_slim);
        athletic = findViewById(R.id.search_check_athletic);
        heavy = findViewById(R.id.search_check_heavy);
        average = findViewById(R.id.search_check_average);
        doesntMatter = findViewById(R.id.bodytype_doesnt_matter);


        //setting old search values

        if (sharedPreferences != null) {

            if (sharedPreferences.getString("ageLow", null) != null) {
                tvMin.setText(sharedPreferences.getString("ageLow", null));
                rangeSeekbar.setMinStartValue(Float.valueOf(sharedPreferences.getString("ageLow", null)));
            }
            if (sharedPreferences.getString("ageHigh", null) != null) {
                tvMax.setText(sharedPreferences.getString("ageHigh", null));
                rangeSeekbar.setMaxStartValue(Float.valueOf(sharedPreferences.getString("ageHigh", null)));
            }
            String[] hf = getResources().getStringArray(R.array.height_array);

            if (sharedPreferences.getString("heightLow", null) != null) {

                for (String s : hf) {

                    if (s.contains(sharedPreferences.getString("heightLow", null))) {
                        height_from.setSelection(Arrays.asList(hf).indexOf(s));
                    }
                }
            }

            if (sharedPreferences.getString("heightHigh", null) != null) {
                for (String s : hf) {

                    if (s.contains(sharedPreferences.getString("heightHigh", null))) {
                        height_to.setSelection(Arrays.asList(hf).indexOf(s));
                    }
                }
            }
            if (sharedPreferences.getString("communities", null) != null) {
                String[] arr = sharedPreferences.getString("communities", null).replace("[", "").replace("]", "").split(",");
                for (String s : arr)
                    CastList.add(s);
                countspinnerCastSearch = CastList.size();
                spinnerCastSearch.setText(sharedPreferences.getString("communities", null));
            }


            if (sharedPreferences.getString("educationArray", null) != null) {
                if (sharedPreferences.getString("educationArray", null).contains("Engineer")) {
                    intengineer = true;
                    tengineer.setTextColor(colorg);
                    education.add(tengineer.getText().toString());
                    iengineer.setImageResource(R.drawable.engineer);

                }
                if (sharedPreferences.getString("educationArray", null).contains("Doctor")) {
                    intdoctor = true;
                    tdoctor.setTextColor(colorg);
                    education.add(tdoctor.getText().toString());

                    idoctor.setImageResource(R.drawable.doctor);

                }
                if (sharedPreferences.getString("educationArray", null).contains(tcacs.getText().toString())) {
                    intcacs = true;
                    tcacs.setTextColor(colorg);
                    education.add(tcacs.getText().toString());
                    icacs.setImageResource(R.drawable.ca);

                }
                if (sharedPreferences.getString("educationArray", null).contains(tllb.getText().toString())) {
                    intllb = true;
                    tllb.setTextColor(colorg);
                    education.add(tllb.getText().toString());

                    illb.setImageResource(R.drawable.llb);

                }
                if (sharedPreferences.getString("educationArray", null).contains(tpg.getText().toString())) {
                    intpg = true;
                    tpg.setTextColor(colorg);
                    education.add(tpg.getText().toString());

                    ipg.setImageResource(R.drawable.mba);

                }
                if (sharedPreferences.getString("educationArray", null).contains(tg.getText().toString())) {
                    intg = true;
                    tg.setTextColor(colorg);
                    education.add(tg.getText().toString());

                    ig.setImageResource(R.drawable.grad);

                }
                if (sharedPreferences.getString("educationArray", null).contains(tug.getText().toString())) {
                    intug = true;
                    tug.setTextColor(colorg);
                    education.add(tug.getText().toString());

                    iug.setImageResource(R.drawable.undergrad);

                }
                if (sharedPreferences.getString("educationArray", null).contains(tmbamca.getText().toString())) {
                    intmbamca = true;
                    tmbamca.setTextColor(colorg);
                    education.add(tmbamca.getText().toString());

                    imbamca.setImageResource(R.drawable.mba);

                }
            }
            if (sharedPreferences.getString("sortBy", null) != null) {
                hf = getResources().getStringArray(R.array.sort_by_array);

                for (String s : hf) {

                    if (sharedPreferences.getString("sortBy", null).contains(s)) {
                        sort_by.setSelection(Arrays.asList(hf).indexOf(s));
                    }
                }
            }

            if (sharedPreferences.getString("maritalStatusArray", null) != null) {
                String[] arr = sharedPreferences.getString("maritalStatusArray", null).replace("[", "").replace("]", "").split(",");
                for (String s : arr)
                    maritalstatusList.add(s);
                countmaritalstatus = maritalstatusList.size();
                maritalstatus.setText(sharedPreferences.getString("maritalStatusArray", null));
            }
            if (sharedPreferences.getString("familyStatusArray", null) != null) {
                String[] arr = sharedPreferences.getString("familyStatusArray", null).replace("[", "").replace("]", "").split(",");
                for (String s : arr)
                    familystatusList.add(s);
                countfamilystatus = familystatusList.size();

                familystatus.setText(sharedPreferences.getString("familyStatusArray", null));
            }
            if (sharedPreferences.getString("manglik", null) != null) {
                hf = getResources().getStringArray(R.array.manglik_search_array);
                for (String s : hf) {

                    if (s.contains(sharedPreferences.getString("manglik", null))) {
                        manglik.setSelection(Arrays.asList(hf).indexOf(s));
                    }
                }
            }
            if (sharedPreferences.getString("children", null) != null) {
                hf = getResources().getStringArray(R.array.children_array);
                for (String s : hf) {

                    if (s.contains(sharedPreferences.getString("children", null))) {
                        children.setSelection(Arrays.asList(hf).indexOf(s));
                    }
                }
            }
            if (sharedPreferences.getString("occupationArray", null) != null) {
                if (sharedPreferences.getString("occupationArray", null).contains("Profession")) {
                    profession.setChecked(true);
                }
                if (sharedPreferences.getString("occupationArray", null).contains("Job")) {
                    job.setChecked(true);
                }
                if (sharedPreferences.getString("occupationArray", null).contains("Retired")) {
                    retired.setChecked(true);
                }
                if (sharedPreferences.getString("occupationArray", null).contains("Business")) {
                    business.setChecked(true);
                }
                if (sharedPreferences.getString("occupationArray", null).contains("Not Employed")) {
                    not_employed.setChecked(true);
                }
                if (sharedPreferences.getString("occupationArray", null).contains("Studying-Not Employed")) {
                    studying.setChecked(true);
                }
                if (sharedPreferences.getString("occupationArray", null).contains("Does not matter")) {
                    dont_matter.setChecked(true);
                }
            }
            if (sharedPreferences.getString("annualArray", null) != null) {
                String[] arr = sharedPreferences.getString("annualArray", null).replace("[", "").replace("]", "").split(",");
                for (String s : arr)
                    AIList.add(s);
                countannualincome = AIList.size();

                annualincome.setText(sharedPreferences.getString("annualArray", null));
            }
            if (sharedPreferences.getString("complexionArray", null) != null) {
                if (sharedPreferences.getString("complexionArray", null).contains("Very Fair")) {
                    very_fair.setChecked(true);
                }
                if (sharedPreferences.getString("complexionArray", null).contains("Fair")) {
                    fair.setChecked(true);
                }
                if (sharedPreferences.getString("complexionArray", null).contains("Wheatish")) {
                    wheatish.setChecked(true);
                }
                if (sharedPreferences.getString("complexionArray", null).contains("Wheatish Brown")) {
                    wheatish_brown.setChecked(true);
                }
                if (sharedPreferences.getString("complexionArray", null).contains("Dark")) {
                    dark.setChecked(true);
                }
                if (sharedPreferences.getString("complexionArray", null).contains("Does not matter")) {
                    doesnt_matter.setChecked(true);
                }

            }
            if (sharedPreferences.getString("bodyTypeArray", null) != null) {
                if (sharedPreferences.getString("bodyTypeArray", null).contains("Slim")) {
                    slim.setChecked(true);
                }
                if (sharedPreferences.getString("bodyTypeArray", null).contains("Athletic")) {
                    athletic.setChecked(true);
                }
                if (sharedPreferences.getString("bodyTypeArray", null).contains("Average")) {
                    average.setChecked(true);
                }
                if (sharedPreferences.getString("bodyTypeArray", null).contains("Heavy")) {
                    heavy.setChecked(true);
                }
                if (sharedPreferences.getString("bodyTypeArray", null).contains("Does not matter")) {
                    doesntMatter.setChecked(true);
                }


            }
            if (sharedPreferences.getString("physicalStatusArray", null) != null) {
                String[] arr = sharedPreferences.getString("physicalStatusArray", null).replace("[", "").replace("]", "").split(",");
                for (String s : arr)
                    physicalstatusList.add(s);
                countphysicalstatus = physicalstatusList.size();

                physicalstatus.setText(sharedPreferences.getString("physicalStatusArray", null));
            }


        }


        //doesnt matter all deselect anything selected doesnt matter deselected
        doesnt_matter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (doesnt_matter.isChecked()) {
                    very_fair.setChecked(false);
                    fair.setChecked(false);
                    wheatish.setChecked(false);
                    wheatish_brown.setChecked(false);
                    dark.setChecked(false);
                }
            }
        });


        // pizza
        very_fair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (int_very_fair) {
                    int_very_fair = false;
                    complexion.remove(very_fair.getText().toString());
                    Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                } else if (!int_very_fair) {
                    int_very_fair = true;
                    complexion.add(very_fair.getText().toString());
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();

                }
                if (very_fair.isChecked() || fair.isChecked() || dark.isChecked() || wheatish.isChecked() || wheatish_brown.isChecked()) {
                    doesnt_matter.setChecked(false);
                    int_doesnt_matter = false;
                    complexion.remove(doesnt_matter.getText().toString());
                }
                if (!very_fair.isChecked() && !fair.isChecked() && !dark.isChecked() && !wheatish.isChecked() && !wheatish_brown.isChecked()) {
                    doesnt_matter.setChecked(true);
                    int_doesnt_matter = true;
                    complexion.add(doesnt_matter.getText().toString());
                }
            }
        });
        fair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (int_fair) {
                    int_fair = false;
                    complexion.remove(fair.getText().toString());
                    Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                } else if (!int_fair) {
                    int_fair = true;
                    complexion.add(fair.getText().toString());
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();

                }
                if (very_fair.isChecked() || fair.isChecked() || dark.isChecked() || wheatish.isChecked() || wheatish_brown.isChecked()) {
                    doesnt_matter.setChecked(false);
                    int_doesnt_matter = false;
                    complexion.remove(doesnt_matter.getText().toString());
                }
                if (!very_fair.isChecked() && !fair.isChecked() && !dark.isChecked() && !wheatish.isChecked() && !wheatish_brown.isChecked()) {
                    doesnt_matter.setChecked(true);
                    int_doesnt_matter = true;
                    complexion.add(doesnt_matter.getText().toString());
                }
            }
        });
        wheatish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (int_wheatish) {
                    int_wheatish = false;
                    complexion.remove(wheatish.getText().toString());
                    Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                } else if (!int_wheatish) {
                    int_wheatish = true;
                    complexion.add(wheatish.getText().toString());
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();

                }
                if (very_fair.isChecked() || fair.isChecked() || dark.isChecked() || wheatish.isChecked() || wheatish_brown.isChecked()) {
                    doesnt_matter.setChecked(false);
                    int_doesnt_matter = false;
                    complexion.remove(doesnt_matter.getText().toString());
                }
                if (!very_fair.isChecked() && !fair.isChecked() && !dark.isChecked() && !wheatish.isChecked() && !wheatish_brown.isChecked()) {
                    doesnt_matter.setChecked(true);
                    int_doesnt_matter = true;
                    complexion.add(doesnt_matter.getText().toString());
                }
            }
        });
        wheatish_brown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (int_wheatish_brown) {
                    int_wheatish_brown = false;
                    complexion.remove(wheatish_brown.getText().toString());
                    Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                } else if (!int_wheatish_brown) {
                    int_wheatish_brown = true;
                    complexion.add(wheatish_brown.getText().toString());
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();

                }
                if (very_fair.isChecked() || fair.isChecked() || dark.isChecked() || wheatish.isChecked() || wheatish_brown.isChecked()) {
                    doesnt_matter.setChecked(false);
                    int_doesnt_matter = false;
                    complexion.remove(doesnt_matter.getText().toString());
                }
                if (!very_fair.isChecked() && !fair.isChecked() && !dark.isChecked() && !wheatish.isChecked() && !wheatish_brown.isChecked()) {
                    doesnt_matter.setChecked(true);
                    int_doesnt_matter = true;
                    complexion.add(doesnt_matter.getText().toString());
                }
            }
        });
        dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (int_dark) {
                    int_dark = false;
                    complexion.remove(dark.getText().toString());
                    Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                } else if (!int_dark) {
                    int_dark = true;
                    complexion.add(dark.getText().toString());
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();

                }
                if (very_fair.isChecked() || fair.isChecked() || dark.isChecked() || wheatish.isChecked() || wheatish_brown.isChecked()) {
                    doesnt_matter.setChecked(false);
                    int_doesnt_matter = false;
                    complexion.remove(doesnt_matter.getText().toString());
                }
                if (!very_fair.isChecked() && !fair.isChecked() && !dark.isChecked() && !wheatish.isChecked() && !wheatish_brown.isChecked()) {
                    doesnt_matter.setChecked(true);
                    int_doesnt_matter = true;
                    complexion.add(doesnt_matter.getText().toString());
                }
            }
        });
        doesnt_matter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (int_doesnt_matter) {
                    int_doesnt_matter = false;

                    complexion.remove(doesnt_matter.getText().toString());
                    Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                } else if (!int_doesnt_matter) {
                    int_doesnt_matter = true;
                    complexion.add(doesnt_matter.getText().toString());
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();

                }
                if (doesnt_matter.isChecked()) {
                    very_fair.setChecked(false);
                    int_very_fair = false;
                    complexion.remove(very_fair.getText().toString());
                    fair.setChecked(false);
                    int_fair = false;
                    complexion.remove(fair.getText().toString());
                    wheatish.setChecked(false);
                    int_wheatish = false;
                    complexion.remove(wheatish.getText().toString());
                    wheatish_brown.setChecked(false);
                    int_wheatish_brown = false;
                    complexion.remove(wheatish_brown.getText().toString());
                    dark.setChecked(false);
                    int_dark = false;
                    complexion.remove(dark.getText().toString());
                }
            }
        });


        profession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (int_profession) {
                    int_profession = false;
                    occupation.remove(profession.getText().toString());
                    Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                } else if (!int_profession) {
                    int_profession = true;
                    occupation.add(profession.getText().toString());
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();

                }
                if (profession.isChecked() || job.isChecked() || retired.isChecked() || business.isChecked() || not_employed.isChecked() || studying.isChecked()) {
                    dont_matter.setChecked(false);
                    int_dont_matter = false;
                    occupation.remove(dont_matter.getText().toString());
                }
                if (!profession.isChecked() && !job.isChecked() && !retired.isChecked() && !business.isChecked() && !not_employed.isChecked() && !studying.isChecked()) {
                    dont_matter.setChecked(true);
                    int_dont_matter = true;
                    occupation.add(dont_matter.getText().toString());
                }
            }
        });
        job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (int_job) {
                    int_job = false;
                    occupation.remove(job.getText().toString());
                    Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                } else if (!int_job) {
                    int_job = true;
                    occupation.add(job.getText().toString());
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();

                }
                if (profession.isChecked() || job.isChecked() || retired.isChecked() || business.isChecked() || not_employed.isChecked() || studying.isChecked()) {
                    dont_matter.setChecked(false);
                    int_dont_matter = false;
                    occupation.remove(dont_matter.getText().toString());
                }
                if (!profession.isChecked() && !job.isChecked() && !retired.isChecked() && !business.isChecked() && !not_employed.isChecked() && !studying.isChecked()) {
                    dont_matter.setChecked(true);
                    int_dont_matter = true;
                    occupation.add(dont_matter.getText().toString());
                }
            }
        });
        retired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (int_retired) {
                    int_retired = false;
                    occupation.remove(retired.getText().toString());
                    Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                } else if (!int_retired) {
                    int_retired = true;
                    occupation.add(retired.getText().toString());
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();

                }
                if (profession.isChecked() || job.isChecked() || retired.isChecked() || business.isChecked() || not_employed.isChecked() || studying.isChecked()) {
                    dont_matter.setChecked(false);
                    int_dont_matter = false;
                    occupation.remove(dont_matter.getText().toString());
                }
                if (!profession.isChecked() && !job.isChecked() && !retired.isChecked() && !business.isChecked() && !not_employed.isChecked() && !studying.isChecked()) {
                    dont_matter.setChecked(true);
                    int_dont_matter = true;
                    occupation.add(dont_matter.getText().toString());
                }
            }
        });
        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (int_business) {
                    int_business = false;
                    occupation.remove(business.getText().toString());
                    Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                } else if (!int_business) {
                    int_business = true;
                    occupation.add(business.getText().toString());
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();

                }
                if (profession.isChecked() || job.isChecked() || retired.isChecked() || business.isChecked() || not_employed.isChecked() || studying.isChecked()) {
                    dont_matter.setChecked(false);
                    int_dont_matter = false;
                    occupation.remove(dont_matter.getText().toString());
                }
                if (!profession.isChecked() && !job.isChecked() && !retired.isChecked() && !business.isChecked() && !not_employed.isChecked() && !studying.isChecked()) {
                    dont_matter.setChecked(true);
                    int_dont_matter = true;
                    occupation.add(dont_matter.getText().toString());
                }
            }
        });
        not_employed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (int_not_employed) {
                    int_not_employed = false;
                    occupation.remove(not_employed.getText().toString());
                    Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                } else if (!int_not_employed) {
                    int_not_employed = true;
                    occupation.add(not_employed.getText().toString());
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
                }
                if (profession.isChecked() || job.isChecked() || retired.isChecked() || business.isChecked() || not_employed.isChecked() || studying.isChecked()) {
                    dont_matter.setChecked(false);
                    int_dont_matter = false;
                    occupation.remove(dont_matter.getText().toString());
                }
                if (!profession.isChecked() && !job.isChecked() && !retired.isChecked() && !business.isChecked() && !not_employed.isChecked() && !studying.isChecked()) {
                    dont_matter.setChecked(true);
                    int_dont_matter = true;
                    occupation.add(dont_matter.getText().toString());
                }
            }
        });
        studying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (int_studying) {
                    int_studying = false;
                    occupation.remove(studying.getText().toString());
                    Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                } else if (!int_studying) {
                    int_studying = true;
                    occupation.add(studying.getText().toString());
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();

                }
                if (profession.isChecked() || job.isChecked() || retired.isChecked() || business.isChecked() || not_employed.isChecked() || studying.isChecked()) {
                    dont_matter.setChecked(false);
                    int_dont_matter = false;
                    occupation.remove(dont_matter.getText().toString());
                }
                if (!profession.isChecked() && !job.isChecked() && !retired.isChecked() && !business.isChecked() && !not_employed.isChecked() && !studying.isChecked()) {
                    dont_matter.setChecked(true);
                    int_dont_matter = true;
                    occupation.add(dont_matter.getText().toString());
                }
            }
        });
        dont_matter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (int_dont_matter) {
                    int_dont_matter = false;
                    occupation.remove(dont_matter.getText().toString());
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();

                } else if (!int_dont_matter) {
                    int_dont_matter = true;
                    occupation.add(dont_matter.getText().toString());
                    Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                }
                if (dont_matter.isChecked()) {
                    profession.setChecked(false);
                    int_profession = false;
                    occupation.remove(profession.getText().toString());
                    job.setChecked(false);
                    int_job = false;
                    occupation.remove(job.getText().toString());
                    retired.setChecked(false);
                    int_retired = false;
                    occupation.remove(retired.getText().toString());
                    business.setChecked(false);
                    int_business = false;
                    occupation.remove(business.getText().toString());
                    not_employed.setChecked(false);
                    int_not_employed = false;
                    occupation.remove(not_employed.getText().toString());
                    studying.setChecked(false);
                    int_studying = false;
                    occupation.remove(studying.getText().toString());

                }
            }
        });


        slim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (intSlim) {
                    intSlim = false;
                    bodyType.remove(slim.getText().toString());
                    Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                } else if (!intSlim) {
                    intSlim = true;
                    bodyType.add(slim.getText().toString());
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();

                }
                if (slim.isChecked() || athletic.isChecked() || average.isChecked() || heavy.isChecked()) {
                    doesntMatter.setChecked(false);
                    intDoesntMatter = false;
                    bodyType.remove(doesntMatter.getText().toString());
                }
                if (!slim.isChecked() && !athletic.isChecked() && !average.isChecked() && !heavy.isChecked()) {
                    doesntMatter.setChecked(true);
                    intDoesntMatter = true;
                    bodyType.add(doesntMatter.getText().toString());
                }
            }
        });
        athletic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (intAthletic) {
                    intAthletic = false;
                    bodyType.remove(athletic.getText().toString());
                    Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                } else if (!intAthletic) {
                    intAthletic = true;
                    bodyType.add(athletic.getText().toString());
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();

                }
                if (slim.isChecked() || athletic.isChecked() || average.isChecked() || heavy.isChecked()) {
                    doesntMatter.setChecked(false);
                    intDoesntMatter = false;
                    bodyType.remove(doesntMatter.getText().toString());
                }
                if (!slim.isChecked() && !athletic.isChecked() && !average.isChecked() && !heavy.isChecked()) {
                    doesntMatter.setChecked(true);
                    intDoesntMatter = true;
                    bodyType.add(doesntMatter.getText().toString());
                }
            }
        });
        average.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (intAverage) {
                    intAverage = false;
                    bodyType.remove(average.getText().toString());
                    Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                } else if (!intAverage) {
                    intAverage = true;
                    bodyType.add(average.getText().toString());
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();

                }
                if (slim.isChecked() || athletic.isChecked() || average.isChecked() || heavy.isChecked()) {
                    doesntMatter.setChecked(false);
                    intDoesntMatter = false;
                    bodyType.remove(doesntMatter.getText().toString());
                }
                if (!slim.isChecked() && !athletic.isChecked() && !average.isChecked() && !heavy.isChecked()) {
                    doesntMatter.setChecked(true);
                    intDoesntMatter = true;
                    bodyType.add(doesntMatter.getText().toString());
                }
            }
        });
        heavy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (intHeavy) {
                    intHeavy = false;
                    bodyType.remove(heavy.getText().toString());
                    Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                } else if (!intHeavy) {
                    intHeavy = true;
                    bodyType.add(heavy.getText().toString());
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();

                }
                if (slim.isChecked() || athletic.isChecked() || average.isChecked() || heavy.isChecked()) {
                    doesntMatter.setChecked(false);
                    intDoesntMatter = false;
                    bodyType.remove(doesntMatter.getText().toString());
                }
                if (!slim.isChecked() && !athletic.isChecked() && !average.isChecked() && !heavy.isChecked()) {
                    doesntMatter.setChecked(true);
                    intDoesntMatter = true;
                    bodyType.add(doesntMatter.getText().toString());
                }
            }
        });
        doesntMatter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (intDoesntMatter) {
                    intDoesntMatter = false;
                    bodyType.remove(doesntMatter.getText().toString());
                    Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                } else if (!intDoesntMatter) {
                    intDoesntMatter = true;
                    bodyType.add(doesntMatter.getText().toString());
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();

                }
                if (doesntMatter.isChecked()) {
                    slim.setChecked(false);
                    intSlim = false;
                    bodyType.remove(slim.getText().toString());
                    athletic.setChecked(false);
                    intAthletic = false;
                    bodyType.remove(athletic.getText().toString());
                    average.setChecked(false);
                    intAverage = false;
                    bodyType.remove(average.getText().toString());
                    heavy.setChecked(false);
                    intHeavy = false;
                    bodyType.remove(heavy.getText().toString());

                }
            }
        });


    }

    public int getCasebreak() {
        return casebreak;
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public int getAge(int DOByear, int DOBmonth, int DOBday) {

        int age;

        final Calendar calenderToday = Calendar.getInstance();
        int currentYear = calenderToday.get(Calendar.YEAR);
        int currentMonth = 1 + calenderToday.get(Calendar.MONTH);
        int todayDay = calenderToday.get(Calendar.DAY_OF_MONTH);

        age = currentYear - DOByear;

        if (DOBmonth > currentMonth) {
            --age;
        } else if (DOBmonth == currentMonth) {
            if (DOBday > todayDay) {
                --age;
            }
        }
        return age;
    }

    private class BackEnd extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(Search.this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.setMessage("Please Wait...");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            AndroidNetworking.post(Constants.AWS_SERVER + "/searchById")
                    .addBodyParameter("query", strings[0])
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {

                        @Override
                        public void onResponse(JSONArray response) {

                            Vector<String> customers = new Vector<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONArray user = null;
                                try {
                                    user = response.getJSONArray(i);
                                    if ((customers.indexOf(user.getString(3)) == -1)) {
                                        customers.add(user.getString(3));
                                        DateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
                                        Date now = new Date();
                                        Date date = null;
                                        try {
                                            date = formatter.parse(user.get(1).toString());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }


                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(date);
                                        String formatedDate = cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR);

                                        String[] partsOfDate = formatedDate.split("-");
                                        int day = Integer.parseInt(partsOfDate[0]);
                                        int month = Integer.parseInt(partsOfDate[1]);
                                        int year = Integer.parseInt(partsOfDate[2]);
                                        int a = getAge(year, month, day);
                                        String age = Integer.toString(a);
                                        SuggestionModel suggestionModel;
                                        if (user.get(8).equals("")) {
                                            suggestionModel = new SuggestionModel(Integer.parseInt(age), "http://www.marwadishaadi.com/uploads/cust_" + user.get(3).toString() + "/thumb/" + user.get(0).toString(), user.get(2).toString() + " " + user.get(12).toString(), user.get(3).toString(), user.get(4).toString(), user.get(5).toString(), user.get(6).toString(), user.get(7).toString(), "No Income mentioned.", user.get(9).toString(), user.get(10).toString(), user.get(11).toString(), "0", "Not");
                                        } else {
                                            suggestionModel = new SuggestionModel(Integer.parseInt(age), "http://www.marwadishaadi.com/uploads/cust_" + user.get(3).toString() + "/thumb/" + user.get(1).toString(), user.get(2).toString() + " " + user.get(12).toString(), user.get(3).toString(), user.get(4).toString(), user.get(5).toString(), user.get(6).toString(), user.get(7).toString(), user.get(8).toString(), user.get(9).toString(), user.get(10).toString(), user.get(11).toString(), "0", "Not");
                                        }
                                        suggestionModelList2.add(suggestionModel);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Search.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    cityList.clear();
                                    statesList.clear();
                                    addPrevious = addPreviousc = "";
                                    Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("which", "advSearch");
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }

                        @Override
                        public void onError(ANError error) {
                            Search.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                }
                            });

                            Toast.makeText(getApplicationContext(), "Network Error Occurder. Please check Internet", Toast.LENGTH_LONG).show();
                        }
                    });

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

}
