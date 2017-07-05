package com.example.sid.marwadishaadi.Upload_User_Photos;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.FB_Gallery_Photo_Upload.FbGalleryActivity;
import com.example.sid.marwadishaadi.Membership.MembershipActivity;
import com.example.sid.marwadishaadi.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.example.sid.marwadishaadi.R.id.count;

public class UploadPhotoActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static int number = 0;
    private Button submit;
    private CircleImageView img;
    private boolean isSelected = false;
    private CircleImageView photo1,photo2,photo3,photo4,photo5;
    CallbackManager callbackManager;
    protected Button fblogin;
    LoginManager loginManager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_upload_photo);

        callbackManager = CallbackManager.Factory.create();
        fblogin = (Button) findViewById(R.id.fb_login_button);

        if (Profile.getCurrentProfile() !=null || AccessToken.getCurrentAccessToken() != null){
            fblogin.setText("Upload photos from Facebook");
        }

        fblogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Profile.getCurrentProfile() == null || AccessToken.getCurrentAccessToken() == null){

                loginManager.getInstance().logInWithReadPermissions(UploadPhotoActivity.this,Arrays.asList("email","user_photos"));
                loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        // getting user profile
                        GraphRequest request = GraphRequest.newMeRequest(
                                AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        try {

                                            String userid = object.getString("id");
                                            fblogin.setText("Or upload photos from Facebook");
                                            Intent i = new Intent(UploadPhotoActivity.this, FbGalleryActivity.class);
                                            i.putExtra("userid",userid);
                                            startActivity(i);
                                            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }});
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });
                }else{

                    Profile profile = Profile.getCurrentProfile();
                    String userid = profile.getId();
                    Intent i = new Intent(UploadPhotoActivity.this, FbGalleryActivity.class);
                    i.putExtra("userid",userid);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                }

            }
        });


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        submit= (Button)findViewById(R.id.submit_photo);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics,"Upload Photo","button");
                if(isSelected){
                         Intent i = new Intent(UploadPhotoActivity.this, MembershipActivity.class);
                        startActivity(i);
                }else{
                    Toast.makeText(UploadPhotoActivity.this, "Minimum 1 photo required ", Toast.LENGTH_SHORT).show();
                }
            }
        });


        photo1 = (CircleImageView) findViewById(R.id.photo1);
        photo2 = (CircleImageView) findViewById(R.id.photo2);
        photo3 = (CircleImageView) findViewById(R.id.photo3);
        photo4 = (CircleImageView) findViewById(R.id.photo4);
        photo5 = (CircleImageView) findViewById(R.id.photo5);

        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    number = 1;
                    selectImage();

            }
        });

        photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = 2;
                selectImage();
            }
        });


        photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = 3;
                selectImage();

            }
        });


        photo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = 4;
                selectImage();

            }
        });


        photo5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = 5;
                selectImage();
            }
        });



        if (this.getIntent().getExtras()!=null){
            Bundle b =this.getIntent().getExtras();
            ArrayList<String> urls = b.getStringArrayList("selected_photos_url");

            for (String url : urls) {
                Log.d("urls from upload", "onCreate: " + url);
            }

            for (int i=0;i<urls.size();i++){

                Picasso.with(UploadPhotoActivity.this)
                        .load(urls.get(i))
                        .into(getImageviewInstance(i));

            }

        }


    }

    private CharSequence[] getItems(){

        String tag = getImageview().getTag().toString();
        if(tag.equals("default")){
            CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
            return items;
        }else if (getImageview() == photo1){
            if (!tag.equals("default")) {
                CharSequence[] items = {"Take Photo", "Choose from Library", "Remove Photo", "Cancel"};
                return items;
            }else{
                CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                return items;
            }
        }else{
            final CharSequence[] items = { "Take Photo", "Choose from Library","Set as Profile picture","Remove Photo",
                    "Cancel" };
            return items;
        }
    }

    private void selectImage() {

        final CharSequence[] items = getItems();
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadPhotoActivity.this);
        builder.setTitle("Choose Photos !");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                        galleryIntent();
                } else if (items[item].equals("Set as Profile picture")){

                         Drawable temp = getImageview().getDrawable();
                         Drawable photo = photo1.getDrawable();

                         String temp_tag = getImageview().getTag().toString();
                         String photo_tag = photo1.getTag().toString();

                         photo1.setImageDrawable(temp);
                         if (temp_tag.equals("changed")){
                             photo1.setTag("changed");
                         }

                        if (photo_tag.equals("default")){
                            getImageview().setTag("default");
                        }
                         getImageview().setImageDrawable(photo);

                } else if (items[item].equals("Remove Photo")){
                        getImageview().setImageResource(R.drawable.photo);
                        getImageview().setTag("default");
                }else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                }
            }
        });


        AlertDialog image = builder.create();
        image.setTitle("Choose Photos !");
        image.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == SELECT_FILE)
                    onSelectFromGalleryResult(data);
                else if (requestCode == REQUEST_CAMERA)
                    onCaptureImageResult(data);
                else
                    callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        img = getImageview();
        img.setImageBitmap(thumbnail);
        img.setTag("changed");
        isSelected=true;

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        img = getImageview();
        img.setImageBitmap(bm);
        img.setTag("changed");
        isSelected=true;
    }

    private CircleImageView getImageview(){
        switch (number){
            case 1:
                return photo1;
            case 2:
                return photo2;
            case 3:
                return photo3;
            case 4:
                return photo4;
            case 5:
                return photo5;
            default:
                return photo1;
        }
    }

    private CircleImageView getImageviewInstance(int number){
        switch (number){
            case 0:
                photo1.setTag("changed");
                return photo1;
            case 1:
                photo2.setTag("changed");
                return photo2;
            case 2:
                photo3.setTag("changed");
                return photo3;
            case 3:
                photo4.setTag("changed");
                return photo4;
            case 4:
                photo5.setTag("changed");
                return photo5;
            default:
                photo1.setTag("changed");
                return photo1;
        }
    }
}
