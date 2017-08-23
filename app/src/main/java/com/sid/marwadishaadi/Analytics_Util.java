package com.sid.marwadishaadi;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Sid on 22-Jun-17.
 */

public class Analytics_Util {

    public static void logAnalytic(FirebaseAnalytics mFirebaseAnalytics,String name,String type){

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,type);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);

    }
}
