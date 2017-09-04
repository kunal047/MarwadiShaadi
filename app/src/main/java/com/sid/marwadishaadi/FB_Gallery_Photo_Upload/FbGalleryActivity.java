package com.sid.marwadishaadi.FB_Gallery_Photo_Upload;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sid.marwadishaadi.R;
import com.sid.marwadishaadi.Upload_User_Photos.UploadPhotoActivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class FbGalleryActivity extends AppCompatActivity implements OnPicSelectedListener{

    private RecyclerView recyclerView;
    private List<FbGalleryModel> fbGalleryModelList=new ArrayList<>();
    private FbGalleryAdapter fbGalleryAdapter;
    private String userid;
    private ArrayList<String> selected_photos_url = new ArrayList<>();

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
        recyclerView.setHasFixedSize(true);
        FadeInLeftAnimator fadeInLeftAnimator = new FadeInLeftAnimator();
        recyclerView.setItemAnimator(fadeInLeftAnimator);
        RecyclerView.LayoutManager mLayoutManager =new StaggeredGridLayoutManager(2,1);
        recyclerView.setLayoutManager(mLayoutManager);
        fbGalleryAdapter = new FbGalleryAdapter(FbGalleryActivity.this,fbGalleryModelList);
        recyclerView.setAdapter(fbGalleryAdapter);
        getData();
        fbGalleryAdapter.setListener(this);


        Button photo_upload = (Button) findViewById(R.id.fb_upload);
        photo_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting all selected photos
                for (FbGalleryModel fb:fbGalleryModelList){
                    if (fb.isSelected()){
                        selected_photos_url.add(fb.getUrl());
                    }
                }

                for (String url : selected_photos_url) {
                }

                if (selected_photos_url.size() <= 5) {
                     Bundle b = new Bundle();
                     b.putStringArrayList("selected_photos_url", selected_photos_url);
                     Intent i = new Intent(FbGalleryActivity.this, UploadPhotoActivity.class);
                     i.putExtras(b);
                     startActivity(i);
                     overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                 }else{
                     selected_photos_url.clear();
                     Toast.makeText(FbGalleryActivity.this, "Select only 5 photos", Toast.LENGTH_SHORT).show();
                 }
            }
        });

    }

    private void getData(){

        // access token
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Set<String> permissions  = accessToken.getPermissions();
        for (String permission : permissions) {
        }

        // getting all albums
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                "/"+userid+"/albums",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {


                        JSONObject obj = response.getJSONObject();
                        JSONArray data = null;
                        try {
                            data = obj.getJSONArray("data");
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        // getting album id of profile pictures
                        String album_id = null;
                        for (int i=0;i<data.length();i++){

                            try {
                                JSONObject album = data.getJSONObject(i);
                                if (album.getString("name").equals("Profile Pictures")){
                                    album_id = album.getString("id");
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }

                        // get all profile picture album photos
                        GraphRequest request = GraphRequest.newGraphPathRequest(
                                accessToken,
                                "/" + album_id+"/photos",
                                new GraphRequest.Callback() {
                                    @Override
                                    public void onCompleted(GraphResponse response) {
                                        JSONObject data = response.getJSONObject();
                                        try {

                                            final JSONArray photos = data.getJSONArray("data");

                                            for (int i=0;i<photos.length();i++){

                                                JSONObject photonode = photos.getJSONObject(i);
                                                String photoid = photonode.getString("id");


                                                // getting photo finally :)
                                                GraphRequest request = GraphRequest.newGraphPathRequest(
                                                        accessToken,
                                                        "/"+photoid,
                                                        new GraphRequest.Callback() {
                                                            @Override
                                                            public void onCompleted(GraphResponse response) {

                                                                JSONObject photo = response.getJSONObject();
                                                                try {
                                                                    JSONArray webp_images = photo.getJSONArray("webp_images");
                                                                    String url = webp_images.getJSONObject(0).getString("source");
                                                                    FbGalleryModel fb = new FbGalleryModel(url, false);
                                                                    fbGalleryModelList.add(fb);
                                                                    fbGalleryAdapter.notifyDataSetChanged();
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });

                                                Bundle parameters = new Bundle();
                                                parameters.putString("fields", "webp_images");
                                                request.setParameters(parameters);
                                                request.executeAsync();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                });

                        request.executeAsync();


                    }
                });

        request.executeAsync();

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
