package com.example.sid.marwadishaadi.FB_Gallery_Photo_Upload;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sid.marwadishaadi.Dashboard.DashboardActivity;
import com.example.sid.marwadishaadi.OnPicSelectedListener;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.Upload_User_Photos.UploadPhotoActivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class FbGalleryActivity extends AppCompatActivity implements OnPicSelectedListener{

    private RecyclerView recyclerView;
    private List<FbGalleryModel> fbGalleryModelList;
    private FbGalleryAdapter fbGalleryAdapter;
    private String userid;
    private List<FbGalleryModel> selectedphotos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_gallery);

        Intent data = getIntent();
        userid = data.getStringExtra("userid");

        Toolbar toolbar = (Toolbar) findViewById(R.id.fbgallery_toolbar);
        toolbar.setTitle("Select Photos");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.fb_gallery_recyclerview);
        fbGalleryAdapter = new FbGalleryAdapter(FbGalleryActivity.this,fbGalleryModelList);
        recyclerView.setHasFixedSize(true);
        FadeInLeftAnimator fadeInLeftAnimator = new FadeInLeftAnimator();
        recyclerView.setItemAnimator(fadeInLeftAnimator);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(fbGalleryAdapter);
        fbGalleryAdapter.setListener(this);

        getData();

        Button photo_upload = (Button) findViewById(R.id.fb_upload);
        photo_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting all selected photos
                for (FbGalleryModel fb:fbGalleryModelList){
                    if (fb.isSelected()){
                        selectedphotos.add(fb);
                    }
                }

                // TODO: 28-Jun-17 upload selected photos in selected_photos arraylist
                Intent i = new Intent(FbGalleryActivity.this, DashboardActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });

    }

    private void getData() {

        // all photos
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+userid+"/photos",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        JSONObject obj = response.getJSONObject();
                        JSONArray data = null;
                        try {
                            data = obj.getJSONArray("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        for (int i=0;i<data.length();i++){

                            try {
                                JSONObject pic = data.getJSONObject(i);
                                String photoid = pic.getString("id");

                                // each individual photo
                                GraphRequest request = GraphRequest.newGraphPathRequest(
                                        AccessToken.getCurrentAccessToken(),
                                        "/"+photoid,
                                        new GraphRequest.Callback() {
                                            @Override
                                            public void onCompleted(GraphResponse response) {

                                                JSONObject js = response.getJSONObject();
                                                String url = null;
                                                try {
                                                    url = js.getString("picture");
                                                    FbGalleryModel fb = new FbGalleryModel(url,false);
                                                    fbGalleryModelList.add(fb);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "picture");
                                request.setParameters(parameters);
                                request.executeAsync();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            fbGalleryAdapter.notifyDataSetChanged();
                        }
                    }
                }
        ).executeAsync();
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        overridePendingTransition(R.anim.exit,0);
        return true;
    }

    @Override
    public void updateToolbar(int count) {
        getSupportActionBar().setTitle(count + " Selected ");
    }
}
