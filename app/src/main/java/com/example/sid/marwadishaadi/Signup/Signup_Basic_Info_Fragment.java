package com.example.sid.marwadishaadi.Signup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sid.marwadishaadi.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;


public class Signup_Basic_Info_Fragment extends Fragment implements Step{

    private String height, weight, built, maritalStatus, education, highestDegree, occupation, companyName, designation, annualIncome,   fatherName, fatherOccupation, fatherOccupationDetails, familyStatus;
    private static final String TAG = "Signup_Basic_Info_Fragm";

    public static Signup_Basic_Info_Fragment bi = new Signup_Basic_Info_Fragment();

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBuilt() {
        return built;
    }

    public void setBuilt(String built) {
        this.built = built;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getHighestDegree() {
        return highestDegree;
    }

    public void setHighestDegree(String highestDegree) {
        this.highestDegree = highestDegree;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(String annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getFatherOccupation() {
        return fatherOccupation;
    }

    public void setFatherOccupation(String fatherOccupation) {
        this.fatherOccupation = fatherOccupation;
    }

    public String getFatherOccupationDetails() {
        return fatherOccupationDetails;
    }

    public void setFatherOccupationDetails(String fatherOccupationDetails) {
        this.fatherOccupationDetails = fatherOccupationDetails;
    }

    public String getFamilyStatus() {
        return familyStatus;
    }

    public void setFamilyStatus(String familyStatus) {
        this.familyStatus = familyStatus;
    }

    public Signup_Basic_Info_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basic_info, container, false);

        Spinner spinnerHeight = (Spinner) view.findViewById(R.id.spinnerHeight);
        Spinner spinnerBuilt = (Spinner) view.findViewById(R.id.spinnerBuilt);
        Spinner spinnerMaritalStatus = (Spinner) view.findViewById(R.id.spinnerMaritalStatus);
        Spinner spinnerEducation = (Spinner) view.findViewById(R.id.spinnerEducation);
        Spinner spinnerOccupation = (Spinner) view.findViewById(R.id.spinnerOccupation);
        Spinner spinnerDesignation = (Spinner) view.findViewById(R.id.spinnerDesignation);
        Spinner spinnerAnnualIncome = (Spinner) view.findViewById(R.id.spinnerAnnualIncome);
        Spinner spinnerFatherOccupation = (Spinner) view.findViewById(R.id.spinnerFatherOccupation);
        Spinner spinnerFamilyStatus = (Spinner) view.findViewById(R.id.spinnerFamilyStatus);

        EditText editTextWeight = (EditText) view.findViewById(R.id.editTextWeight);
        EditText editTextHighestDegree = (EditText) view.findViewById(R.id.editTextHighestDegree);
        EditText editTextCompanyName = (EditText) view.findViewById(R.id.editTextCompanyName);
        EditText editTextFatherName = (EditText) view.findViewById(R.id.editTextFatherName);
        EditText editTextFatherOccupationDetails = (EditText) view.findViewById(R.id.editTextFatherOccupationDetails);







        spinnerHeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                height = parent.getItemAtPosition(position).toString();
                bi.setHeight(height);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerBuilt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                built = parent.getItemAtPosition(position).toString();
                bi.setBuilt(built);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerMaritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maritalStatus = parent.getItemAtPosition(position).toString();
                bi.setMaritalStatus(maritalStatus);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerEducation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                education = parent.getItemAtPosition(position).toString();
                bi.setEducation(education);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

         spinnerOccupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                occupation = parent.getItemAtPosition(position).toString();
                bi.setOccupation(occupation);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDesignation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                designation = parent.getItemAtPosition(position).toString();
                bi.setDesignation(designation);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerAnnualIncome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                annualIncome = parent.getItemAtPosition(position).toString();
                bi.setAnnualIncome(annualIncome);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFatherOccupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fatherOccupation = parent.getItemAtPosition(position).toString();
                bi.setFatherOccupation(fatherOccupation);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFamilyStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                familyStatus = parent.getItemAtPosition(position).toString();
                bi.setFamilyStatus(familyStatus);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editTextWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                weight = s.toString();
                bi.setWeight(weight);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextHighestDegree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                highestDegree = s.toString();
                bi.setHighestDegree(highestDegree);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextCompanyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                companyName = s.toString();
                bi.setCompanyName(companyName);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextFatherName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fatherName = s.toString();
                bi.setFatherName(fatherName);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextFatherOccupationDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fatherOccupationDetails = s.toString();
                bi.setFatherOccupationDetails(fatherOccupationDetails);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Log.d(TAG, "onCreateView: jsdfaadf height is  ----------- " + height);

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
