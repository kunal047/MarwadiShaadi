package com.sid.marwadishaadi.Signup;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

/**
 * Created by Sid on 15-Jun-17.
 */
public class Signup_Additional_Info_StepperAdapter extends AbstractFragmentStepAdapter {

    public Signup_Additional_Info_StepperAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        switch (position) {
            case 0:
                Signup_Basic_Info_Fragment signupBasicInfoFragment = new Signup_Basic_Info_Fragment();
                return signupBasicInfoFragment;
            case 1:
                Signup_Additional_Info_Fragment signupAdditional_infoFragment = new Signup_Additional_Info_Fragment();
                return signupAdditional_infoFragment;
            case 2:
                Signup_Partner_Preferences_Fragment signupPartnerPreferencesFragment = new Signup_Partner_Preferences_Fragment();
                return signupPartnerPreferencesFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        switch (position) {
            case 0:
                return new StepViewModel.Builder(context)
                        .setTitle("Basic") //can be a CharSequence instead
                        .create();
            case 1:
                return new StepViewModel.Builder(context)
                        .setTitle("Additional") //can be a CharSequence instead
                        .create();
            case 2:
                return new StepViewModel.Builder(context)
                        .setTitle("Partner") //can be a CharSequence instead
                        .create();
        }
        return null;
    }
}