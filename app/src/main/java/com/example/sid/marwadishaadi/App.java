package com.example.sid.marwadishaadi;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.Dashboard.DashboardActivity;
import com.example.sid.marwadishaadi.LoginHistory.OnClearFromRecentService;


import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by Sid on 14-Jun-17.
 */


public class App extends Application{

    public static List<Place> placeslist = new ArrayList<>();

    private String customer_id;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {


        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        customer_id = sharedpref.getString("customer_id", null);

        new FetchLocation().execute();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cl=Calendar.getInstance();
        String formattedDate = df.format( Calendar.getInstance().getTime());
        cl.add(Calendar.DATE,90);
        String s2= df.format(cl.getTime());
        Log.e(TAG, "onCreate:--- "+ formattedDate+ "update is ------"+s2);

        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        OnClearFromRecentService onClearFromRecentService=new OnClearFromRecentService();
            boolean check = sharedpref.getBoolean("isLoggedIn", false);
            Toast.makeText(getApplicationContext(),"Please check your Internet Connection",Toast.LENGTH_LONG);
            Log.d(":", "onDonePressed:--------------------------- bool is  " + check);
            if (check) {
                Intent i = new Intent(getApplicationContext(),DashboardActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
                startActivity(i);
            }


    }


    private class FetchLocation extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("location wala", "onPostExecute: " + placeslist.toString());
        }

        @Override
        protected Void doInBackground(Void... voids) {

            AndroidNetworking.post("http://208.91.199.50:5000/fetchCityStateCountry")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Place place;
                        try {
                            for(int i = 0;i<response.length();i++) {
                                JSONArray array = response.getJSONArray(i);
                                place = new Place(array.getString(0), array.getString(2), array.getString(4));
                                App.placeslist.add(place);
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

}
