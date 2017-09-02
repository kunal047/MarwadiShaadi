package com.sid.marwadishaadi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SortBy extends AppCompatActivity {

    private RadioButton radioButton;
    private RadioGroup radioGroupSortBy;
    private String showPhotos, sortBy;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_by);

        preferences = getSharedPreferences("sort_by", MODE_PRIVATE);
        editor = preferences.edit();

        radioGroupSortBy = (RadioGroup) findViewById(R.id.radioGroupSort);

        CheckBox checkBoxOnlyWithPhotos = (CheckBox) findViewById(R.id.checkBoxOnlyWithPhotos);
        Button buttonUpdate = (Button) findViewById(R.id.sortby_update);

        int selectedId = radioGroupSortBy.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radioButton = (RadioButton) findViewById(selectedId);

        if (preferences.getString("sortBy", "").contains("Recently")) {
            sortBy = "Recently";
            radioGroupSortBy.check(R.id.radioRecentlyRegistered);
        } else if (preferences.getString("sortBy", "").contains("Last")) {
            sortBy = "Last";
            radioGroupSortBy.check(R.id.radioLastActive);
        }

        if (preferences.getString("showPhotos", "").contains("yes")) {
            showPhotos = "yes";
            checkBoxOnlyWithPhotos.setChecked(true);
        } else if (preferences.getString("showPhotos", "").contains("no")) {
            showPhotos = "no";
            checkBoxOnlyWithPhotos.setChecked(false);
        }

        //SETTING CURRENT STATUS

//        String sort = preferences.getString("sortBy", "Recently");
//        if (sort.contains("Recently")) {
//
//            radioGroupSortBy.check(R.id.radioRecentlyRegistered);
//        } else {
//
//            radioGroupSortBy.check(R.id.radioLastActive);
//        }
//        String showP = preferences.getString("showPhotos", "yes");
//        if (showP.contains("yes")) {
//            checkBoxOnlyWithPhotos.setChecked(true);
//        } else {
//            checkBoxOnlyWithPhotos.setChecked(false);
//        }


        radioGroupSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                radioButton = (RadioButton) findViewById(checkedId);
                if (radioButton.getText().toString().contains("Last")) {
                    sortBy = "Last";
                } else {
                    sortBy = "Recently";
                }


            }
        });

        checkBoxOnlyWithPhotos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showPhotos = "yes";
                } else {
                    showPhotos = "no";
                }
            }
        });


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                editor.putString("sortBy", sortBy);
                editor.putString("showPhotos", showPhotos);
                editor.apply();
                setResult(1);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {

        setResult(2);
        finish();

    }
}
