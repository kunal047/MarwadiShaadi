package com.example.sid.marwadishaadi.Upload_User_Photos;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.FB_Gallery_Photo_Upload.FbGalleryActivity;
import com.example.sid.marwadishaadi.Membership.MembershipActivity;
import com.example.sid.marwadishaadi.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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
    protected LoginButton fblogin;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
        callbackManager = CallbackManager.Factory.create();

        fblogin = (LoginButton) findViewById(R.id.fb_login_button);

        if (Profile.getCurrentProfile() !=null || AccessToken.getCurrentAccessToken() != null){
            fblogin.setText("Or Upload photos from Facebook");
        }

        fblogin.setReadPermissions(Arrays.asList("email","user_photos"));
        fblogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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


    }


    private void selectImage() {


        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadPhotoActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                }
            }
        });


        AlertDialog image = builder.create();
        image.setTitle("Add photo !");
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


}
