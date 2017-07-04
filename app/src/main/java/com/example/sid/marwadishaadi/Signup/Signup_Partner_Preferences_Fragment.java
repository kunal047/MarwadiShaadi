package com.example.sid.marwadishaadi.Signup;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.Search.BottomSheet;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Signup_Partner_Preferences_Fragment extends Fragment implements Step {

    private static final String TAG = "Signup_Partner_Preferen";
    public static EditText preferenceMaritalstatus;
    public static EditText preferenceAnnualincome;
    public static EditText preferencePhysicalstatus;

    public static Signup_Partner_Preferences_Fragment pf = new Signup_Partner_Preferences_Fragment();


    private static int casebreak;

    public List<String> preferenceEducation = new ArrayList<>();
    public List<String> preferenceOccupation = new ArrayList<>();
    public List<String> preferenceComplexion = new ArrayList<>();
    public List<String> preferenceBodyType = new ArrayList<>();

    ImageView idoctor, iengineer, imbamca, icacs, ipg, ig, iug, illb;
    boolean intdoctor = false, intengineer = false, intmbamca = false, intcacs = false, intpg = false, intg = false, intug = false, intllb = false;
    TextView tdoctor, tengineer, tmbamca, tcacs, tpg, tg, tug, tllb;
    LinearLayout ldoctor, lengineer, lmbamca, lcacs, lpg, lg, lug, lllb;
    int colorg, colorb;

    String prefMinAge, prefMaxAge, prefHeightFrom, prefHeightTo, prefPhysicalStatus, prefMaritalStatus, prefAnnualIncome, prefWorkLocation;

    public Signup_Partner_Preferences_Fragment() {
        // Required empty public constructor
    }

    public String getPrefWorkLocation() {
        return prefWorkLocation;
    }

    public void setPrefWorkLocation(String prefWorkLocation) {
        this.prefWorkLocation = prefWorkLocation;
    }

    public List<String> getPreferenceEducation() {
        return preferenceEducation;
    }

    public void setPreferenceEducation(List<String> preferenceEducation) {
        this.preferenceEducation = preferenceEducation;
    }

    public List<String> getPreferenceOccupation() {
        return preferenceOccupation;
    }

    public void setPreferenceOccupation(List<String> preferenceOccupation) {
        this.preferenceOccupation = preferenceOccupation;
    }

    public List<String> getPreferenceComplexion() {
        return preferenceComplexion;
    }

    public void setPreferenceComplexion(List<String> preferenceComplexion) {
        this.preferenceComplexion = preferenceComplexion;
    }

    public List<String> getPreferenceBodyType() {
        return preferenceBodyType;
    }

    public void setPreferenceBodyType(List<String> preferenceBodyType) {
        this.preferenceBodyType = preferenceBodyType;
    }

    public String getPrefMinAge() {
        return prefMinAge;
    }

    public void setPrefMinAge(String prefMinAge) {
        this.prefMinAge = prefMinAge;
    }

    public String getPrefMaxAge() {
        return prefMaxAge;
    }

    public void setPrefMaxAge(String prefMaxAge) {
        this.prefMaxAge = prefMaxAge;
    }

    public String getPrefHeightFrom() {
        return prefHeightFrom;
    }

    public void setPrefHeightFrom(String prefHeightFrom) {
        this.prefHeightFrom = prefHeightFrom;
    }

    public String getPrefHeightTo() {
        return prefHeightTo;
    }

    public void setPrefHeightTo(String prefHeightTo) {
        this.prefHeightTo = prefHeightTo;
    }

    public String getPrefPhysicalStatus() {
        return prefPhysicalStatus;
    }

    public void setPrefPhysicalStatus(String prefPhysicalStatus) {
        this.prefPhysicalStatus = prefPhysicalStatus;
    }

    public String getPrefMaritalStatus() {
        return prefMaritalStatus;
    }

    public void setPrefMaritalStatus(String prefMaritalStatus) {
        this.prefMaritalStatus = prefMaritalStatus;
    }

    public String getPrefAnnualIncome() {
        return prefAnnualIncome;
    }

    public void setPrefAnnualIncome(String prefAnnualIncome) {
        this.prefAnnualIncome = prefAnnualIncome;
    }

    public void setColorb(int colorb) {
        this.colorb = colorb;
    }

    public int getCasebreak() {
        return casebreak;
    }

    public static void setCasebreak(int casebreak) {
        Signup_Partner_Preferences_Fragment.casebreak = casebreak;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_preferences, container, false);


        idoctor = (ImageView) view.findViewById(R.id.doctor);
        iengineer = (ImageView) view.findViewById(R.id.engineer);
        icacs = (ImageView) view.findViewById(R.id.ca_cs);
        ipg = (ImageView) view.findViewById(R.id.pg);
        ig = (ImageView) view.findViewById(R.id.g);
        iug = (ImageView) view.findViewById(R.id.ug);
        imbamca = (ImageView) view.findViewById(R.id.mba_mca);
        illb = (ImageView) view.findViewById(R.id.llb);

        tdoctor = (TextView) view.findViewById(R.id.text_doctor);
        tengineer = (TextView) view.findViewById(R.id.text_engineer);
        tmbamca = (TextView) view.findViewById(R.id.text_mba_mca);
        tcacs = (TextView) view.findViewById(R.id.text_ca_cs);
        tllb = (TextView) view.findViewById(R.id.text_llb);
        tpg = (TextView) view.findViewById(R.id.text_pg);
        tg = (TextView) view.findViewById(R.id.text_g);
        tug = (TextView) view.findViewById(R.id.text_ug);

        ldoctor = (LinearLayout) view.findViewById(R.id.list_doctor);
        lengineer = (LinearLayout) view.findViewById(R.id.list_engineer);
        lmbamca = (LinearLayout) view.findViewById(R.id.list_mab_mca);
        lcacs = (LinearLayout) view.findViewById(R.id.list_ca_cs);
        lllb = (LinearLayout) view.findViewById(R.id.list_llb);
        lpg = (LinearLayout) view.findViewById(R.id.list_pg);
        lg = (LinearLayout) view.findViewById(R.id.list_g);
        lug = (LinearLayout) view.findViewById(R.id.list_ug);

        Spinner spinnerHeightFrom = (Spinner) view.findViewById(R.id.spinnerHeightFromFP);
        Spinner spinnerHeightTo = (Spinner) view.findViewById(R.id.spinnerHeightToFP);
        Spinner spinnerMarritalStatus = (Spinner) view.findViewById(R.id.prefSpinnerMarital);
        Spinner spinnerPhysicalStatus = (Spinner) view.findViewById(R.id.prefSpinnerPhysical);

        CheckBox checkBoxVeryFair = (CheckBox) view.findViewById(R.id.checkVeryFairFP);
        CheckBox checkBoxFair = (CheckBox) view.findViewById(R.id.checkFairFP);
        CheckBox checkBoxWheatish = (CheckBox) view.findViewById(R.id.checkWheatishFP);
        CheckBox checkBoxWheatishBrown = (CheckBox) view.findViewById(R.id.checkWheatishBrownFP);
        CheckBox checkBoxDark = (CheckBox) view.findViewById(R.id.checkDarkFP);

        CheckBox checkBoxSlim = (CheckBox) view.findViewById(R.id.checkSlimFP);
        CheckBox checkBoxAthletic = (CheckBox) view.findViewById(R.id.checkAthleticFP);
        CheckBox checkBoxAverage = (CheckBox) view.findViewById(R.id.checkAverageFP);
        CheckBox checkBoxHeavy = (CheckBox) view.findViewById(R.id.checkHeavyFP);
        CheckBox checkBoxProfession = (CheckBox) view.findViewById(R.id.checkProfessionFP);
        CheckBox checkBoxJob = (CheckBox) view.findViewById(R.id.checkJobFP);
        CheckBox checkBoxRetired = (CheckBox) view.findViewById(R.id.checkRetiredFP);
        CheckBox checkBoxBusiness = (CheckBox) view.findViewById(R.id.checkBusinessFP);
        CheckBox checkBoxNotEmployed = (CheckBox) view.findViewById(R.id.checkNotEmployedFP);
        CheckBox checkBoxStudying = (CheckBox) view.findViewById(R.id.checkStudyingNotEmployedFP);

        AutoCompleteTextView workLocation = (AutoCompleteTextView) view.findViewById(R.id.prefWorkLocation);


        workLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                prefWorkLocation = s.toString();
                pf.setPrefWorkLocation(prefWorkLocation);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        checkBoxVeryFair.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferenceComplexion.add(buttonView.getText().toString());
                    pf.setPreferenceComplexion(preferenceComplexion);
                }
            }
        });


        checkBoxFair.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferenceComplexion.add(buttonView.getText().toString());
                    pf.setPreferenceComplexion(preferenceComplexion);
                }
            }
        });

        checkBoxWheatish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferenceComplexion.add(buttonView.getText().toString());
                    pf.setPreferenceComplexion(preferenceComplexion);
                }
            }
        });


        checkBoxWheatishBrown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferenceComplexion.add(buttonView.getText().toString());
                    pf.setPreferenceComplexion(preferenceComplexion);
                }
            }
        });

        checkBoxDark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferenceComplexion.add(buttonView.getText().toString());
                    pf.setPreferenceComplexion(preferenceComplexion);
                }
            }
        });

        // checkboxes for bodytype

        checkBoxSlim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferenceBodyType.add(buttonView.getText().toString());
                    pf.setPreferenceBodyType(preferenceBodyType);
                }
            }
        });

        checkBoxAthletic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferenceBodyType.add(buttonView.getText().toString());
                    pf.setPreferenceBodyType(preferenceBodyType);

                }
            }
        });

        checkBoxAverage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferenceBodyType.add(buttonView.getText().toString());
                    pf.setPreferenceBodyType(preferenceBodyType);

                }
            }
        });

        checkBoxHeavy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferenceBodyType.add(buttonView.getText().toString());
                    pf.setPreferenceBodyType(preferenceBodyType);

                }
            }
        });

        // occupation

        checkBoxProfession.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferenceOccupation.add(buttonView.getText().toString());
                    pf.setPreferenceOccupation(preferenceOccupation);

                }
            }
        });

        checkBoxJob.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferenceOccupation.add(buttonView.getText().toString());
                    pf.setPreferenceOccupation(preferenceOccupation);


                }
            }
        });

        checkBoxRetired.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferenceOccupation.add(buttonView.getText().toString());
                    pf.setPreferenceOccupation(preferenceOccupation);


                }
            }
        });

        checkBoxBusiness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferenceOccupation.add(buttonView.getText().toString());
                    pf.setPreferenceOccupation(preferenceOccupation);

                }
            }
        });

        checkBoxNotEmployed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferenceOccupation.add(buttonView.getText().toString());
                    pf.setPreferenceOccupation(preferenceOccupation);

                }
            }
        });

        checkBoxStudying.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferenceOccupation.add(buttonView.getText().toString());
                    pf.setPreferenceOccupation(preferenceOccupation);

                }
            }
        });

        spinnerHeightFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pf.setPrefHeightFrom(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerHeightTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pf.setPrefHeightTo(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerMarritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pf.setPrefMaritalStatus(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPhysicalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pf.setPrefPhysicalStatus(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // get seekbar from view

        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) view.findViewById(R.id.rangeSeekbar);
// get min and max text view
        final TextView tvMin = (TextView) view.findViewById(R.id.textMinFP);
        final TextView tvMax = (TextView) view.findViewById(R.id.textMaxFP);
        rangeSeekbar.setMinValue(18);
        rangeSeekbar.setMaxValue(71);
// set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(minValue));
                tvMax.setText(String.valueOf(maxValue));

                prefMinAge = String.valueOf(minValue);
                prefMaxAge = String.valueOf(maxValue);

                pf.setPrefMaxAge(prefMaxAge);
                pf.setPrefMinAge(prefMinAge);

            }
        });

// set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
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


        preferenceAnnualincome = (EditText) view.findViewById(R.id.preference_annual_income);
        preferenceAnnualincome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: values are ********************************* " + s.toString().substring(0, s.length() - 1));
                prefAnnualIncome = s.toString().substring(1, s.length() - 1);
                pf.setPrefAnnualIncome(prefAnnualIncome);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        preferenceAnnualincome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak = 5;
                BottomSheetDialogFragment btm = new BottomSheet(7);
                btm.show(getFragmentManager(), btm.getTag());


            }
        });


        ldoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (intdoctor) {
                    intdoctor = false;
                    tdoctor.setTextColor(colorb);
                    preferenceEducation.remove(tdoctor.getText().toString());
                    Toast.makeText(getApplicationContext(), "Doctor Removed", Toast.LENGTH_SHORT).show();
                    idoctor.setImageResource(R.drawable.doctor_black);
                } else if (!intdoctor) {
                    intdoctor = true;
                    tdoctor.setTextColor(colorg);
                    preferenceEducation.add(tdoctor.getText().toString());
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
                    preferenceEducation.remove(tengineer.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);
                    Toast.makeText(getApplicationContext(), "Engineer Removed", Toast.LENGTH_SHORT).show();
                    iengineer.setImageResource(R.drawable.engineer_black);
                } else if (!intengineer) {
                    intengineer = true;
                    tengineer.setTextColor(colorg);
                    preferenceEducation.add(tengineer.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

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
                    preferenceEducation.remove(tmbamca.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

                    Toast.makeText(getApplicationContext(), "MBA/MCA Removed", Toast.LENGTH_SHORT).show();
                    imbamca.setImageResource(R.drawable.mba_black);
                } else if (!intmbamca) {
                    intmbamca = true;
                    tmbamca.setTextColor(colorg);
                    preferenceEducation.add(tmbamca.getText().toString() + "/MS/MA/MSC/M.Arch");
                    pf.setPreferenceEducation(preferenceEducation);

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
                    preferenceEducation.remove(tcacs.getText().toString() + "/ICWA");
                    pf.setPreferenceEducation(preferenceEducation);

                    Toast.makeText(getApplicationContext(), "CA/CS Removed", Toast.LENGTH_SHORT).show();
                    icacs.setImageResource(R.drawable.ca_black);
                } else if (!intcacs) {
                    intcacs = true;
                    tcacs.setTextColor(colorg);
                    preferenceEducation.add(tcacs.getText().toString() + "/ICWA");
                    pf.setPreferenceEducation(preferenceEducation);

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
                    preferenceEducation.remove(tpg.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

                    Toast.makeText(getApplicationContext(), "PostGraduate Removed", Toast.LENGTH_SHORT).show();
                    ipg.setImageResource(R.drawable.mba_black);
                } else if (!intpg) {
                    intpg = true;
                    tpg.setTextColor(colorg);
                    preferenceEducation.add(tpg.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

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
                    preferenceEducation.remove(tg.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

                    ig.setImageResource(R.drawable.grad_black);
                } else if (!intg) {
                    intg = true;
                    tg.setTextColor(colorg);
                    preferenceEducation.add(tg.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

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
                    preferenceEducation.remove(tug.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

                    Toast.makeText(getApplicationContext(), "UnderGraduate Removed", Toast.LENGTH_SHORT).show();
                    iug.setImageResource(R.drawable.undergrad_black);
                } else if (!intug) {
                    intug = true;
                    preferenceEducation.add(tug.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

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
                    preferenceEducation.remove(tllb.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

                    Toast.makeText(getApplicationContext(), "LLB Removed", Toast.LENGTH_SHORT).show();
                    illb.setImageResource(R.drawable.llb_black);
                } else if (!intllb) {
                    intllb = true;
                    Toast.makeText(getApplicationContext(), "LLB Added", Toast.LENGTH_SHORT).show();
                    preferenceEducation.add(tllb.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

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
                    preferenceEducation.remove(tdoctor.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

                    Toast.makeText(getApplicationContext(), "Doctor Removed", Toast.LENGTH_SHORT).show();
                    idoctor.setImageResource(R.drawable.doctor_black);
                } else if (!intdoctor) {
                    intdoctor = true;
                    tdoctor.setTextColor(colorg);
                    preferenceEducation.add(tdoctor.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

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
                    preferenceEducation.remove(tengineer.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

                    Toast.makeText(getApplicationContext(), "Engineer Removed", Toast.LENGTH_SHORT).show();
                    iengineer.setImageResource(R.drawable.engineer_black);
                } else if (!intengineer) {
                    intengineer = true;
                    preferenceEducation.add(tengineer.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

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
                    preferenceEducation.remove(tmbamca.getText().toString() + "/MS/MA/MSC/M.Arch");
                    pf.setPreferenceEducation(preferenceEducation);

                    Toast.makeText(getApplicationContext(), "MBA/MCA Removed", Toast.LENGTH_SHORT).show();
                    imbamca.setImageResource(R.drawable.mba_black);
                } else if (!intmbamca) {
                    intmbamca = true;
                    tmbamca.setTextColor(colorg);
                    Toast.makeText(getApplicationContext(), "MBA/MCA Added", Toast.LENGTH_SHORT).show();
                    preferenceEducation.add(tmbamca.getText().toString() + "/MS/MA/MSC/M.Arch");
                    pf.setPreferenceEducation(preferenceEducation);

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
                    preferenceEducation.remove(tcacs.getText().toString() + "/ICWA");
                    pf.setPreferenceEducation(preferenceEducation);

                    Toast.makeText(getApplicationContext(), "CA/CS Removed", Toast.LENGTH_SHORT).show();
                    icacs.setImageResource(R.drawable.ca_black);
                } else if (!intcacs) {
                    intcacs = true;
                    tcacs.setTextColor(colorg);
                    preferenceEducation.add(tcacs.getText().toString() + "/ICWA");

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
                    preferenceEducation.remove(tpg.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

                    Toast.makeText(getApplicationContext(), "PostGraduate Removed", Toast.LENGTH_SHORT).show();
                    ipg.setImageResource(R.drawable.mba_black);
                } else if (!intpg) {
                    intpg = true;
                    tpg.setTextColor(colorg);
                    preferenceEducation.add(tpg.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

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
                    preferenceEducation.remove(tg.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

                    Toast.makeText(getApplicationContext(), "Graduate Removed", Toast.LENGTH_SHORT).show();
                    ig.setImageResource(R.drawable.grad_black);
                } else if (!intg) {
                    intg = true;
                    tg.setTextColor(colorg);
                    preferenceEducation.add(tg.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

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
                    preferenceEducation.remove(tug.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

                    Toast.makeText(getApplicationContext(), "UnderGraduate Removed", Toast.LENGTH_SHORT).show();
                    iug.setImageResource(R.drawable.undergrad_black);
                } else if (!intug) {
                    intug = true;
                    preferenceEducation.add(tug.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

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
                    preferenceEducation.remove(tllb.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

                    Toast.makeText(getApplicationContext(), "LLB Removed", Toast.LENGTH_SHORT).show();
                    illb.setImageResource(R.drawable.llb_black);
                } else if (!intllb) {
                    intllb = true;
                    preferenceEducation.add(tllb.getText().toString());
                    pf.setPreferenceEducation(preferenceEducation);

                    Toast.makeText(getApplicationContext(), "LLB Added", Toast.LENGTH_SHORT).show();
                    tllb.setTextColor(colorg);
                    illb.setImageResource(R.drawable.llb);
                }
            }
        });
        return view;

    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
