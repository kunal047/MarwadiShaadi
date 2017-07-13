package com.example.sid.marwadishaadi.Signup;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.sid.marwadishaadi.App;
import com.example.sid.marwadishaadi.PlacesAdapter;
import com.example.sid.marwadishaadi.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.Calendar;

public class Signup_Additional_Info_Fragment extends Fragment implements Step {


    public static Signup_Additional_Info_Fragment ai = new Signup_Additional_Info_Fragment();
    private String aboutMe, hobbies, grandfatherName, mamaSurname, familyType, familyValues, nativePlace, subcaste, instituteName, workLocation, highestDegree, dietStatus, smokeStatus, drinkStatus, complexionStatus, physicalStatus, birthTime, birthPlace, gotra, manglikStatus, horoscopeStatus, relationNameStatus, relationFirstName, relationOccupation, relationLocation, relationMobile;
    private EditText editTextBirthTime;
    private int pHour;
    private int pMinute;
    protected PlacesAdapter placesAdapter;

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    pHour = hourOfDay;
                    pMinute = minute;
                    updateDisplay();
                }
            };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void updateDisplay() {
        editTextBirthTime.setText(
                new StringBuilder()
                        .append(pad(pHour)).append(":")
                        .append(pad(pMinute)));
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getGrandfatherName() {
        return grandfatherName;
    }

    public void setGrandfatherName(String grandfatherName) {
        this.grandfatherName = grandfatherName;
    }

    public String getMamaSurname() {
        return mamaSurname;
    }

    public void setMamaSurname(String mamaSurname) {
        this.mamaSurname = mamaSurname;
    }

    public String getFamilyType() {
        return familyType;
    }

    public void setFamilyType(String familyType) {
        this.familyType = familyType;
    }

    public String getFamilyValues() {
        return familyValues;
    }

    public void setFamilyValues(String familyValues) {
        this.familyValues = familyValues;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getSubcaste() {
        return subcaste;
    }

    public void setSubcaste(String subcaste) {
        this.subcaste = subcaste;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }

    public String getHighestDegree() {
        return highestDegree;
    }

    public void setHighestDegree(String highestDegree) {
        this.highestDegree = highestDegree;
    }

    public String getDietStatus() {
        return dietStatus;
    }

    public void setDietStatus(String dietStatus) {
        this.dietStatus = dietStatus;
    }

    public String getSmokeStatus() {
        return smokeStatus;
    }

    public void setSmokeStatus(String smokeStatus) {
        this.smokeStatus = smokeStatus;
    }

    public String getDrinkStatus() {
        return drinkStatus;
    }

    public void setDrinkStatus(String drinkStatus) {
        this.drinkStatus = drinkStatus;
    }

    public String getComplexionStatus() {
        return complexionStatus;
    }

    public void setComplexionStatus(String complexionStatus) {
        this.complexionStatus = complexionStatus;
    }

    public String getPhysicalStatus() {
        return physicalStatus;
    }

    public void setPhysicalStatus(String physicalStatus) {
        this.physicalStatus = physicalStatus;
    }

    public String getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(String birthTime) {
        this.birthTime = birthTime;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getGotra() {
        return gotra;
    }

    public void setGotra(String gotra) {
        this.gotra = gotra;
    }

    public String getManglikStatus() {
        return manglikStatus;
    }

    public void setManglikStatus(String manglikStatus) {
        this.manglikStatus = manglikStatus;
    }

    public String getHoroscopeStatus() {
        return horoscopeStatus;
    }

    public void setHoroscopeStatus(String horoscopeStatus) {
        this.horoscopeStatus = horoscopeStatus;
    }

    public String getRelationNameStatus() {
        return relationNameStatus;
    }

    public void setRelationNameStatus(String relationNameStatus) {
        this.relationNameStatus = relationNameStatus;
    }

    public String getRelationFirstName() {
        return relationFirstName;
    }

    public void setRelationFirstName(String relationFirstName) {
        this.relationFirstName = relationFirstName;
    }

    public String getRelationOccupation() {
        return relationOccupation;
    }

    public void setRelationOccupation(String relationOccupation) {
        this.relationOccupation = relationOccupation;
    }

    public String getRelationLocation() {
        return relationLocation;
    }

    public void setRelationLocation(String relationLocation) {
        this.relationLocation = relationLocation;
    }

    public String getRelationMobile() {
        return relationMobile;
    }

    public void setRelationMobile(String relationMobile) {
        this.relationMobile = relationMobile;
    }

    public EditText getEditTextBirthTime() {
        return editTextBirthTime;
    }

    public void setEditTextBirthTime(EditText editTextBirthTime) {
        this.editTextBirthTime = editTextBirthTime;
    }

    public int getpHour() {
        return pHour;
    }

    public void setpHour(int pHour) {
        this.pHour = pHour;
    }

    public int getpMinute() {
        return pMinute;
    }

    public void setpMinute(int pMinute) {
        this.pMinute = pMinute;
    }

    public TimePickerDialog.OnTimeSetListener getTimeSetListener() {
        return mTimeSetListener;
    }

    public void setTimeSetListener(TimePickerDialog.OnTimeSetListener timeSetListener) {
        mTimeSetListener = timeSetListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_additional__info, container, false);
        editTextBirthTime = (EditText) view.findViewById(R.id.editTextBirthTime);
        editTextBirthTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int min = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), mTimeSetListener, hour, min, DateFormat.is24HourFormat(getActivity()));
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();

            }
        });


        EditText editTextAboutMe = (EditText) view.findViewById(R.id.editTextAboutMe);
        EditText editTextHobbies = (EditText) view.findViewById(R.id.editTextHobbies);
        EditText editTextGrandfatherName = (EditText) view.findViewById(R.id.editTextGrandfatherName);
        EditText editTextMamaSurname = (EditText) view.findViewById(R.id.editTextMamaSurname);
        EditText editTextSubcaste = (EditText) view.findViewById(R.id.editTextSubcaste);
        EditText editTextInstituteName = (EditText) view.findViewById(R.id.editTextInstituteName);
        EditText editTextGotra = (EditText) view.findViewById(R.id.editTextGotra);
        EditText editTextRelationFirstName = (EditText) view.findViewById(R.id.editTextRelationFirstName);
        EditText editTextRelationOccupation = (EditText) view.findViewById(R.id.editTextRelationOccupation);
        EditText editTextRelationMobile = (EditText) view.findViewById(R.id.editTextRelationMobile);


        AutoCompleteTextView WorkLocation = (AutoCompleteTextView) view.findViewById(R.id.editTextWorkLocation);
        AutoCompleteTextView NativePlace = (AutoCompleteTextView) view.findViewById(R.id.editTextNativePlace);
        AutoCompleteTextView RelationLocation = (AutoCompleteTextView) view.findViewById(R.id.editTextRelationLocation);
        AutoCompleteTextView BirthPlace = (AutoCompleteTextView) view.findViewById(R.id.editTextBirthPlace);


        // autocomplete all location fields
        WorkLocation.setThreshold(1);
        placesAdapter = new PlacesAdapter(getContext(), R.layout.fragment_additional__info, R.id.editTextWorkLocation, App.placeslist);
        WorkLocation.setAdapter(placesAdapter);

        NativePlace.setThreshold(1);
        placesAdapter = new PlacesAdapter(getContext(), R.layout.fragment_additional__info, R.id.editTextNativePlace, App.placeslist);
        Log.d("sahjk", "onCreateView: placelist native-----------"+App.placeslist.size());
        NativePlace.setAdapter(placesAdapter);

        RelationLocation.setThreshold(1);
        placesAdapter = new PlacesAdapter(getContext(), R.layout.fragment_additional__info, R.id.editTextRelationLocation, App.placeslist);
        RelationLocation.setAdapter(placesAdapter);

        BirthPlace.setThreshold(1);
        placesAdapter = new PlacesAdapter(getContext(), R.layout.fragment_additional__info, R.id.editTextBirthPlace, App.placeslist);
        BirthPlace.setAdapter(placesAdapter);


        BirthPlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                birthPlace = s.toString();
                ai.setBirthPlace(birthPlace);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        RelationLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                relationLocation = s.toString();
                ai.setRelationLocation(relationLocation);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        NativePlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nativePlace = s.toString();
                ai.setNativePlace(nativePlace);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        WorkLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                workLocation = s.toString();
                ai.setWorkLocation(workLocation);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        editTextBirthTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                birthTime = s.toString();
                ai.setBirthTime(birthTime);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        editTextAboutMe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aboutMe = s.toString();
                ai.setAboutMe(aboutMe);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextHobbies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hobbies = s.toString();
                ai.setHobbies(hobbies);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextGrandfatherName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                grandfatherName = s.toString();
                ai.setGrandfatherName(grandfatherName);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextMamaSurname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mamaSurname = s.toString();
                ai.setMamaSurname(mamaSurname);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        editTextSubcaste.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                subcaste = s.toString();
                ai.setSubcaste(subcaste);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextInstituteName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                instituteName = s.toString();
                ai.setInstituteName(instituteName);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        editTextGotra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                gotra = s.toString();
                ai.setGotra(gotra);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextRelationFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                relationFirstName = s.toString();
                ai.setRelationFirstName(relationFirstName);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextRelationOccupation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                relationOccupation = s.toString();
                ai.setRelationOccupation(relationOccupation);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextRelationMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                relationMobile = s.toString();
                ai.setRelationMobile(relationMobile);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        Spinner spinnerFamilyType = (Spinner) view.findViewById(R.id.spinnerFamilyType);
        Spinner spinnerFamilyValues = (Spinner) view.findViewById(R.id.spinnerFamilyValues);
        Spinner spinnerDietStatus = (Spinner) view.findViewById(R.id.spinnerDietStatus);
        Spinner spinnerSmokeStatus = (Spinner) view.findViewById(R.id.spinnerSmokeStatus);
        Spinner spinnerDrinkStatus = (Spinner) view.findViewById(R.id.spinnerDrinkStatus);
        Spinner spinnerComplexionStatus = (Spinner) view.findViewById(R.id.spinnerComplexionStatus);
        Spinner spinnerPhysicalStatus = (Spinner) view.findViewById(R.id.spinnerPhysicalStatus);
        Spinner spinnerManglikStatus = (Spinner) view.findViewById(R.id.spinnerManglikStatus);
        Spinner spinnerHoroscopeStatus = (Spinner) view.findViewById(R.id.spinnerHoroscopeStatus);
        Spinner spinnerRelationNameStatus = (Spinner) view.findViewById(R.id.spinner_relation_name_status);

        spinnerFamilyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                familyType = parent.getItemAtPosition(position).toString();
                ai.setFamilyType(familyType);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFamilyValues.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                familyValues = parent.getItemAtPosition(position).toString();
                ai.setFamilyValues(familyValues);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDietStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dietStatus = parent.getItemAtPosition(position).toString();
                ai.setDietStatus(dietStatus);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSmokeStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                smokeStatus = parent.getItemAtPosition(position).toString();
                ai.setSmokeStatus(smokeStatus);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDrinkStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                drinkStatus = parent.getItemAtPosition(position).toString();
                ai.setDrinkStatus(drinkStatus);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerComplexionStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                complexionStatus = parent.getItemAtPosition(position).toString();
                ai.setComplexionStatus(complexionStatus);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerPhysicalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                physicalStatus = parent.getItemAtPosition(position).toString();
                ai.setPhysicalStatus(physicalStatus);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerManglikStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                manglikStatus = parent.getItemAtPosition(position).toString();
                ai.setManglikStatus(manglikStatus);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerHoroscopeStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                horoscopeStatus = parent.getItemAtPosition(position).toString();
                ai.setHoroscopeStatus(horoscopeStatus);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerRelationNameStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                relationNameStatus = parent.getItemAtPosition(position).toString();
                ai.setRelationNameStatus(relationNameStatus);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
