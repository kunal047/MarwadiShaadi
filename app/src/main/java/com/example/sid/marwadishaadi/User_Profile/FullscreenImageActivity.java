package com.example.sid.marwadishaadi.User_Profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.R;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class FullscreenImageActivity extends AppCompatActivity {

    private static final boolean AUTO_HIDE = true;
    private String customer_id;
    private CarouselView carouselView;
    private List<String> userImages;
    private boolean isPaidMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen_image);

        SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);


        ImageView imageViewExit = (ImageView) findViewById(R.id.imageViewExit);

        imageViewExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent data = getIntent();
        customer_id = data.getStringExtra("customerNo");

        carouselView = (CarouselView) findViewById(R.id.carouselView);

        new FetchProfilePhotos().execute();


        // Set up the user interaction to manually show or hide the system UI.


        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
    }

    private class FetchProfilePhotos extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            AndroidNetworking.post("http://208.91.199.50:5000/fetchPhotos")
                    .addBodyParameter("customerNo", customer_id)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {
                                String profile_image = response.getString(0).replace("[", "").replace("]","").replace("\"", "");
                                userImages = new ArrayList<>();
                                userImages.add("http://www.marwadishaadi.com/uploads/cust_" + customer_id + "/thumb/" + profile_image);

                                if (response.length() > 1) {
                                    for (int i = 1; i <  response.length() ; i++) {
                                        JSONArray u_image = response.getJSONArray(i);
                                        userImages.add("http://www.marwadishaadi.com/uploads/cust_" + customer_id + "/thumb/" +u_image.getString(0));
                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            carouselView.setImageListener(new ImageListener() {
                                @Override
                                public void setImageForPosition(int position, ImageView imageView) {
                                    Picasso.with(FullscreenImageActivity.this)
                                                    .load(userImages.get(position))
                                                    .placeholder(R.drawable.default_drawer)
                                                    .error(R.drawable.default_drawer)
                                                    .fit()
                                                    .into(imageView);
                                }
                            });
                            carouselView.setPageCount(userImages.size());

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });

            return null;
        }
    }

}
