package com.example.sid.marwadishaadi.Search;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
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
import com.example.sid.marwadishaadi.Chat.Dialog;
import com.example.sid.marwadishaadi.Dashboard_Suggestions.SuggestionAdapter;
import com.example.sid.marwadishaadi.Dashboard_Suggestions.SuggestionModel;
import com.example.sid.marwadishaadi.R;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.example.sid.marwadishaadi.Login.LoginActivity.customer_id;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Search extends AppCompatActivity {
    ImageView idoctor, iengineer, imbamca, icacs, ipg, ig, iug, illb;
    boolean intdoctor = false, intengineer = false, intmbamca = false, intcacs = false, intpg = false, intg = false, intug = false, intllb = false;
    TextView tdoctor, tengineer, tmbamca, tcacs, tpg, tg, tug, tllb;
    LinearLayout ldoctor, lengineer, lmbamca, lcacs, lpg, lg, lug, lllb;
    int colorg, colorb;
    Button mOpenIDSearchButton;
    private static int casebreak;
    TextView statetextView, citytextview;
    CardView advCV;
    public ProgressDialog dialog;
    TextView tvMin, tvMax;
    Button addButton, searchaddbutton;
    EditText autoCompleteState, autocompletecity;
    static String addTextState, addPrevious = "";
    static String addTextcity, addPreviousc = "";
    public static EditText maritalstatus;
    public static EditText familystatus;

    public static EditText annualincome;
    public static EditText physicalstatus;
    public static EditText spinnerCastSearch;
    public static ArrayList<SuggestionModel> suggestionModelSearch=new ArrayList<SuggestionModel>();
    public  static SuggestionAdapter suggestionAdapter;
    public static int countmaritalstatus = 0, countfamilystatus = 0, countannualincome = 0, countphysicalstatus = 0, countspinnerCastSearch = 0;
    public static List<SuggestionModel> suggestionModelList2;


    boolean int_very_fair, int_fair, int_wheatish, int_wheatish_brown, int_dark, int_doesnt_matter = true, int_profession, int_job, int_retired, int_business, int_not_employed, int_studying, int_dont_matter = true, intSlim, intAthletic, intHeavy, intAverage, intDoesntMatter = true;
    CheckBox very_fair, fair, wheatish, wheatish_brown, dark, doesnt_matter, profession, job, retired, business, not_employed, studying, dont_matter, slim, athletic, average, heavy, doesntMatter;
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
    public static List<String> CastList = new ArrayList<>();
    public static List<String> familystatusList = new ArrayList<>();
    public static List<String> maritalstatusList = new ArrayList<>();
    public static List<String> AIList = new ArrayList<>();
    public static List<String> physicalstatusList = new ArrayList<>();


    Spinner height_from, height_to, sort_by, manglik, children;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        toolbar.setTitle("Search");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String[] community= getResources().getStringArray(R.array.communities);
        SharedPreferences communityPackage= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        CastList.clear();
        for(int i=0;i<5;i++){
            if(community[i].trim().toCharArray()[0]==customer_id.trim().toCharArray()[0])
            {
                CastList.add(community[i]);
                Log.e(TAG, "onCreate: -- my community is "+ communityPackage.getString(community[i],null).toCharArray()[0]);
            }else if(communityPackage.getString(community[i],null).contains("Yes")){
                Log.e(TAG, "onCreate: -- my community is "+ communityPackage.getString(community[i],null).toCharArray());
                CastList.add(community[i]);
            }
            Log.e(TAG, "onCreate: -- my community is "+ communityPackage.getString(community[i],null));
        }

        spinnerCastSearch = (EditText) findViewById(R.id.search_user_caste);
        spinnerCastSearch.setText(CastList.toString());
        init();
        height_from = (Spinner) findViewById(R.id.height_from);
        height_to = (Spinner) findViewById(R.id.height_to);
        sort_by = (Spinner) findViewById(R.id.spinner_sort_by);
        manglik = (Spinner) findViewById(R.id.search_manglik_status);
        children = (Spinner) findViewById(R.id.search_children);

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
        occupation.add("Doesn't Matter");
        bodyType.add("Doesn't Matter");
        complexion.add("Doesn't Matter");
        education.add("Doesn't Matter");

        maritalstatus = (EditText) findViewById(R.id.search_Marital_status);
        maritalstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak = 3;
                BottomSheetDialogFragment btm = new BottomSheet(0);
                btm.show(getSupportFragmentManager(), btm.getTag());
            }
        });

        familystatus = (EditText) findViewById(R.id.search_Family_status);
        familystatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak = 4;
                BottomSheetDialogFragment btm = new BottomSheet(0);
                btm.show(getSupportFragmentManager(), btm.getTag());
            }
        });

        annualincome = (EditText) findViewById(R.id.search_Annual_income);
        annualincome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak = 5;
                BottomSheetDialogFragment btm = new BottomSheet(0);
                btm.show(getSupportFragmentManager(), btm.getTag());
            }
        });

        physicalstatus = (EditText) findViewById(R.id.search_physical_status);
        physicalstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak = 6;
                BottomSheetDialogFragment btm = new BottomSheet(0);
                btm.show(getSupportFragmentManager(), btm.getTag());
            }
        });


        addButton = (Button) findViewById(R.id.search_add_state);
        searchaddbutton = (Button) findViewById(R.id.search_add_city);
        statetextView = (TextView) findViewById(R.id.text_view_search_add_state);
        citytextview = (TextView) findViewById(R.id.text_view_search_add_city);
        autoCompleteState = (EditText) findViewById(R.id.search_state);
        autocompletecity = (EditText) findViewById(R.id.search_city);

        advCV = (CardView) findViewById(R.id.advanced_search);
        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbar);
// get min and max text view
        tvMin = (TextView) findViewById(R.id.textMin);
        tvMax = (TextView) findViewById(R.id.textMax);
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
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
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
                    statesList.add(addTextState);
                    statetextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    statetextView.setText((addPrevious + "\n" + addTextState));
                    addPrevious = statetextView.getText().toString();
                    autoCompleteState.setText("");
                    Toast.makeText(getApplicationContext(), "Added successfully ", Toast.LENGTH_SHORT).show();
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
                    cityList.add(addTextcity);
                    citytextview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    citytextview.setText(addPreviousc + "\n" + addTextcity);
                    addPreviousc = citytextview.getText().toString();
                    autocompletecity.setText("");
                    Toast.makeText(getApplicationContext(), "Added successfully ", Toast.LENGTH_SHORT).show();
                }
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

        mOpenIDSearchButton = (Button) findViewById(R.id.search_by_id_name_open);
        mOpenIDSearchButton.setFocusable(true);
        spinnerCastSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                casebreak = 1;
                BottomSheetDialogFragment btm = new BottomSheet(0);
                btm.show(getSupportFragmentManager(), btm.getTag());

            }
        });

        mOpenIDSearchButton = (Button) findViewById(R.id.search_by_id_name_open);
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
        FloatingActionButton search = (FloatingActionButton) findViewById(R.id.search_Submit);
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
                if(gender.contains("Male")){

                    gender = "Female";
                }else {

                    gender = "Male";
                }
                String query = "";
                query = "select  YEAR(tbl_user.birthdate),tbl_user_files.file_name,tbl_user.first_name,tbl_user.customer_no,tbl_user.edu_degree, tbl_user.occup_location,tbl_user.height,tbl_user.occup_company,tbl_user.anuual_income,tbl_user.marrital_status,tbl_city.City_name,tbl_user.occup_designation from tbl_user INNER JOIN tbl_user_files ON tbl_user.customer_no=tbl_user_files.customer_no INNER join tbl_state on tbl_state.state_id=tbl_user.state INNER JOIN tbl_login ON tbl_user.customer_no=tbl_login.customer_no inner join tbl_city on tbl_user.city=tbl_city.City_id where ( tbl_login.user_active ='Yes' and tbl_login.user_deleted='0' ) and  (tbl_user_files.file_type='profile_image') and ( tbl_user.gender='"+gender+"' ) ";
//                ON tbl_user.customer_no=tbl_user_files.customer_no
                int year = Calendar.getInstance().get(Calendar.YEAR);
                query += "and ( YEAR(tbl_user.birthdate)>=" + Integer.toString(year - Integer.parseInt(tvMax.getText().toString())) + " and YEAR(tbl_user.birthdate)<=" + Integer.toString(year - Integer.parseInt(tvMin.getText().toString())) + ")";
                String s1, s2;
                s1 = height_from.getSelectedItem().toString();
                s2 = height_to.getSelectedItem().toString();
                Log.e(TAG, "onClick: ------" + s1 + "----" + s2);

                if (s1.equals("Doesn't matter") && s2.equals("Doesn't matter")) {
                    //no code is here *********** remove space from cm in first entry
                    Log.e(TAG, "onClick: --why it loged");
                } else {
                    if (s1.equals("Doesn't matter") & !s2.equals("Doesn't matter")) {
                        query += "and  tbl_user.height<=" + s2.substring(s2.length() - 5, s2.length() - 2);
                        Log.e(TAG, "onClick: ----------------------" + s2.substring(s2.length() - 5, s2.length() - 2));
                    } else if (!s1.equals("Doesn't matter") & s2.equals("Doesn't matter")) {
                        query += "and tbl_user.height>=" + s1.substring(s1.length() - 5, s1.length() - 2);
                    } else {
                        query += "and ( tbl_user.height<=" + s2.substring(s2.length() - 5, s2.length() - 2) + " and tbl_user.height>=" + s1.substring(s1.length() - 5, s1.length() - 2) + ")";
                    }
                }



                String str = "";
                Log.e(TAG, "onClick: countSpinnerSearch is ++++++++++++++++++++++++" );
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

                } else if (str.contains("Doesn't Matter")) {
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
                } else if (str.contains("Doesn't Matter")) {
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
                } else if (str.contains("Doesn't Matter")) {
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
                } else if (str.contains("Doesn't Matter")) {
                    query += " and (";
                    String[] arrayString = getResources().getStringArray(R.array.aincome_search_array);
                    for (int i = 0; i < arrayString.length; i++) {
                        String string = arrayString[i];
                        String s = string.replace("L", "00000");
                        arrayString[i] = s;
                        Log.e(TAG, "onClick: ----------- list is ----" + arrayString.toString());
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
                        Log.e(TAG, "onClick: ----------- list is ----" + AIList.toString());
                    }
                    for (int i = 0; i < countannualincome - 1; i++) {
                        query += "tbl_user.anuual_income = \"" + AIList.get(i) + "\" or ";
                    }
                    query += "tbl_user.anuual_income = \"" + AIList.get(countannualincome - 1) + "\") ";
                }
                str = physicalstatus.getText().toString();

                if (str.equals("[]") || str.equals("")) {
                    //no code is here
                } else if (str.contains("Doesn't Matter")) {
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
                if (education.indexOf("Doesn't Matter") != -1 && education.size() != 1) {
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

                if (complexion.indexOf("Doesn't Matter") == -1) {
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

                if (bodyType.indexOf("Doesn't Matter") == -1) {
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

                if (occupation.indexOf("Doesn't Matter") == -1) {
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
                if (mangli.equals("Doesn't matter")) {
                    //nothig is here for you
                } else {
                    query += " and ( tbl_user.manglik=\"" + mangli + "\" ) ";
                }
                String childs = children.getSelectedItem().toString();
                if (childs.equals("Doesn't Matter")) {
                    //nthg is here for you
                } else {
                    query += " and ( tbl_user.children=\"" + childs + "\")";
                }

                String itm = sort_by.getSelectedItem().toString();

                if (itm.equals("Doesn't Matter")) {
                    //nothing is here for you
                } else if (itm.equals("Recent Profiles")) {
                    query += " order by tbl_user.created_on asc ";
                } else {
                    query += " order by tbl_login.last_activity_on asc ";
                }

                new BackEnd().execute(query);


                Log.e(TAG, "onClick: ---------------------------query is\n  " + query);

              /*  Toast.makeText(getApplicationContext(),"Education are :------" + education.toString(),Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"Education are :------" + education.toString(),Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"Education are :------" + education.toString(),Toast.LENGTH_SHORT).show();
*/

            }
        });


        very_fair = (CheckBox) findViewById(R.id.search_check_very_fair);
        fair = (CheckBox) findViewById(R.id.search_check_fair);
        wheatish = (CheckBox) findViewById(R.id.search_check_wheatish);
        wheatish_brown = (CheckBox) findViewById(R.id.search_check_wheatish_brown);
        dark = (CheckBox) findViewById(R.id.search_check_dark);
        doesnt_matter = (CheckBox) findViewById(R.id.complexion_doesnt_matter);

        profession = (CheckBox) findViewById(R.id.check_profession);
        job = (CheckBox) findViewById(R.id.check_job);
        retired = (CheckBox) findViewById(R.id.check_retired);
        business = (CheckBox) findViewById(R.id.check_business);
        studying = (CheckBox) findViewById(R.id.check_studying_not_employed);
        not_employed = (CheckBox) findViewById(R.id.check_not_employed);
        dont_matter = (CheckBox) findViewById(R.id.occupation_doesnt_matter);

        slim = (CheckBox) findViewById(R.id.search_check_slim);
        athletic = (CheckBox) findViewById(R.id.search_check_athletic);
        heavy = (CheckBox) findViewById(R.id.search_check_heavy);
        average = (CheckBox) findViewById(R.id.search_check_average);
        doesntMatter = (CheckBox) findViewById(R.id.bodytype_doesnt_matter);

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
            }
        });
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
            }
        });


    }

    public int getCasebreak() {
        return this.casebreak;
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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
            Log.e(TAG, "doInBackground: query is ------"+strings[0]);
            AndroidNetworking.post("http://208.91.199.50:5000/searchById")
                    .addBodyParameter("query", strings[0])
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Log.e(TAG, "onResponse: -------------- "+response.toString());
                            Vector<String> customers=new Vector<String>();
                            for(int i=0;i<response.length();i++){
                                JSONArray user= null;
                                try {
                                    user = response.getJSONArray(i);
                                    if((customers.indexOf(user.getString(3))==-1)){
                                        customers.add(user.getString(3));
                                        Calendar calender = Calendar.getInstance();
                                        int year = calender.get(Calendar.YEAR);
                                        SuggestionModel suggestionModel;
                                        if(user.get(8).equals("")){
                                            suggestionModel = new SuggestionModel(year - (int) user.get(0), "http://www.marwadishaadi.com/uploads/cust_" + user.get(3).toString() + "/thumb/" + user.get(1).toString(), user.get(2).toString(), user.get(3).toString(), user.get(4).toString(), user.get(5).toString(), user.get(6).toString(), user.get(7).toString(), "No Income mentioned.", user.get(9).toString(), user.get(10).toString(), user.get(11).toString(), "0", "Not");
                                        }else{
                                            suggestionModel = new SuggestionModel(year - (int) user.get(0), "http://www.marwadishaadi.com/uploads/cust_" + user.get(3).toString() + "/thumb/" + user.get(1).toString(), user.get(2).toString(), user.get(3).toString(), user.get(4).toString(), user.get(5).toString(), user.get(6).toString(), user.get(7).toString(), user.get(8).toString(), user.get(9).toString(), user.get(10).toString(), user.get(11).toString(), "0", "Not");
                                        }
                                        suggestionModelList2.add(suggestionModel);}
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Search.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    Intent intent=new Intent(getApplicationContext(),SearchResultsActivity.class);
                                    intent.putExtra("which","advSearch");
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
                            Log.e(TAG, "onError: error in search is "+error );
                            Toast.makeText(getApplicationContext(),"Network Error Occurder. Please check Internet",Toast.LENGTH_LONG).show();
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
