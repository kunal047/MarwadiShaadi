package com.sid.marwadishaadi.Upload_User_Photos;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.facebook.CallbackManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sid.marwadishaadi.Analytics_Util;
import com.sid.marwadishaadi.Constants;
import com.sid.marwadishaadi.Membership.MembershipActivity;
import com.sid.marwadishaadi.R;
import com.sid.marwadishaadi.User_Profile.UserProfileActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UploadPhotoActivity extends AppCompatActivity {

    public static final int REQUEST_PERMISSION_SETTING = 105;
    public static final int CAMERA_PERMISSION = 100;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static int number = 0;
    protected Button fblogin;
    private CallbackManager callbackManager;
    private File file_one, file_two, file_three, file_four, file_five;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Button submit;
    private CircleImageView img;
    private boolean isSelected = false;
    private CircleImageView photo1, photo2, photo3, photo4, photo5;
    private String customer_id;
    private View view;
    private Bitmap bitmap;
    private ByteArrayOutputStream bos;
    private FileOutputStream fos;
    private byte[] bitmapdata;
    private String nameOfPhoto, timeStamp, file_type;
    private List<String> userImages;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_upload_photo);

        view = getWindow().getDecorView().getRootView();
        SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);
        callbackManager = CallbackManager.Factory.create();


        String source = getIntent().getStringExtra("from");

        if (source != null && !source.equalsIgnoreCase("otp")) {
            new FetchPhoto().execute();
        }
//        fblogin = (Button) findViewById(R.id.fb_login_button);
//
//        if (Profile.getCurrentProfile() != null || AccessToken.getCurrentAccessToken() != null) {
//            fblogin.setText("Upload photos from Facebook");
//        }

//        if (getIntent().getParcelableExtra("user_images") != null) {
//            ArrayList<String> filelist = getIntent().getParcelableExtra("user_images");
//        }
//
//
//
//        fblogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Profile.getCurrentProfile() == null || AccessToken.getCurrentAccessToken() == null) {
//
//                    LoginManager.getInstance().logInWithReadPermissions(UploadPhotoActivity.this, Arrays.asList("user_photos", "email", "public_profile"));
//                    LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//                        @Override
//                        public void onSuccess(LoginResult loginResult) {
//
//                            // getting user profile
//                            GraphRequest request = GraphRequest.newMeRequest(
//                                    AccessToken.getCurrentAccessToken(),
//                                    new GraphRequest.GraphJSONObjectCallback() {
//                                        @Override
//                                        public void onCompleted(JSONObject object, GraphResponse response) {
//                                            try {
//
//                                                String userid = object.getString("id");
//                                                fblogin.setText("Or upload photos from Facebook");
//                                                Intent i = new Intent(UploadPhotoActivity.this, FbGalleryActivity.class);
//                                                i.putExtra("userid", userid);
//                                                startActivity(i);
//                                                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    });
//                            request.executeAsync();
//                        }
//
//                        @Override
//                        public void onCancel() {
//
//                        }
//
//                        @Override
//                        public void onError(FacebookException error) {
//
//                        }
//                    });
//                } else {
//
//                    Profile profile = Profile.getCurrentProfile();
//                    String userid = profile.getId();
//                    Intent i = new Intent(UploadPhotoActivity.this, FbGalleryActivity.class);
//                    i.putExtra("userid", userid);
//                    startActivity(i);
//                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//                }
//
//            }
//        });


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        submit = (Button) findViewById(R.id.submit_photo);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Saving your photos...", Toast.LENGTH_LONG).show();

                new RemovePhoto().execute();

                if (photo1.getTag() == "changed") {
                    Bitmap bitmap = ((BitmapDrawable) photo1.getDrawable()).getBitmap();
                    String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
                    if (customer_id == null || customer_id.isEmpty()) {
                        customer_id = "ddefault";
                    }
                    String nameOfPhoto = customer_id.substring(1) + "_" + timeStamp + ".jpg";
                    file_one = new File(getApplicationContext().getCacheDir(), nameOfPhoto);
                    file_type = "profile_image";
                    try {
                        file_one.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    try {
                        fos = new FileOutputStream(file_one);
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SystemClock.sleep(1500);
                    try {
                        isSelected = true;
                        new UploadPhoto().execute(file_one).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }


                }

                if (photo2.getTag() == "changed") {

                    bitmap = ((BitmapDrawable) photo2.getDrawable()).getBitmap();
                    timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
                    nameOfPhoto = customer_id.substring(1) + "_" + timeStamp + ".jpg";
                    file_two = new File(getApplicationContext().getCacheDir(), nameOfPhoto);
                    file_type = "image";
                    try {
                        file_two.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
                    bitmapdata = bos.toByteArray();

                    fos = null;
                    try {
                        fos = new FileOutputStream(file_two);
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SystemClock.sleep(1500);
                    try {
                        isSelected = true;
                        new UploadPhoto().execute(file_two).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }

                if (photo3.getTag() == "changed") {

                    bitmap = ((BitmapDrawable) photo3.getDrawable()).getBitmap();
                    timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
                    nameOfPhoto = customer_id.substring(1) + "_" + timeStamp + ".jpg";
                    file_three = new File(getApplicationContext().getCacheDir(), nameOfPhoto);
                    file_type = "image";

                    try {
                        file_three.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
                    bitmapdata = bos.toByteArray();

                    fos = null;
                    try {
                        fos = new FileOutputStream(file_three);
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SystemClock.sleep(1500);

                    try {
                        isSelected = true;
                        new UploadPhoto().execute(file_three).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }


                }

                if (photo4.getTag() == "changed") {
                    bitmap = ((BitmapDrawable) photo4.getDrawable()).getBitmap();
                    timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
                    nameOfPhoto = customer_id.substring(1) + "_" + timeStamp + ".jpg";
                    file_four = new File(getApplicationContext().getCacheDir(), nameOfPhoto);
                    file_type = "image";

                    try {
                        file_four.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
                    bitmapdata = bos.toByteArray();

                    fos = null;
                    try {
                        fos = new FileOutputStream(file_four);
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    SystemClock.sleep(1500);

                    try {
                        isSelected = true;
                        new UploadPhoto().execute(file_four).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }

                if (photo5.getTag() == "changed") {

                    bitmap = ((BitmapDrawable) photo4.getDrawable()).getBitmap();
                    timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
                    nameOfPhoto = customer_id.substring(1) + "_" + timeStamp + ".jpg";
                    file_five = new File(getApplicationContext().getCacheDir(), nameOfPhoto);
                    file_type = "image";

                    try {
                        file_five.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
                    bitmapdata = bos.toByteArray();

                    fos = null;
                    try {
                        fos = new FileOutputStream(file_five);
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SystemClock.sleep(1500);

                    try {
                        isSelected = true;
                        new UploadPhoto().execute(file_five).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }


                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "Upload Photo", "button");
                if (isSelected) {
                    Intent i = null;
                    Toast.makeText(UploadPhotoActivity.this, "All photos uploaded ", Toast.LENGTH_SHORT).show();
                    String from = getIntent().getStringExtra("from");
                    if (from != null && from.equals("userprofile")) {
                        i = new Intent(UploadPhotoActivity.this, UserProfileActivity.class);
                    } else if (photo1.getTag() != "changed" && (photo2.getTag() == "changed" || photo3.getTag() == "changed" || photo4.getTag() == "changed" || photo5.getTag() == "changed")) {


                        Toast.makeText(UploadPhotoActivity.this, "Set your profile picture", Toast.LENGTH_SHORT).show();
                    } else {
                        i = new Intent(UploadPhotoActivity.this, MembershipActivity.class);
                    }
                    startActivity(i);
                } else {
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
                selectImage(number);

            }
        });

        photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = 2;
                selectImage(number);
            }
        });


        photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = 3;
                selectImage(number);

            }
        });


        photo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = 4;
                selectImage(number);

            }
        });


        photo5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = 5;
                selectImage(number);
            }
        });


        if (this.getIntent().getExtras() != null && this.getIntent().getExtras().getStringArrayList("selected_photos_url") != null) {

            Bundle b = this.getIntent().getExtras();
            ArrayList<String> urls = b.getStringArrayList("selected_photos_url");
            for (int i = 0; i < urls.size(); i++) {
                Picasso.with(UploadPhotoActivity.this)
                        .load(urls.get(i))
                        .into(getImageviewInstance(i));

            }

        }

    }

    private CharSequence[] getItems() {

        String tag = getImageview().getTag().toString();
        if (tag.equals("default")) {
            CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
            return items;
        } else if (getImageview() == photo1) {
            if (!tag.equals("default")) {
                CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
//                CharSequence[] items = {"Take Photo", "Choose from Library", "Remove Photo", "Cancel"};
                return items;
            } else {
                CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                return items;
            }
        } else {
            final CharSequence[] items = {"Take Photo", "Choose from Library", "Set as Profile picture", "Remove Photo",
                    "Cancel"};
            return items;
        }
    }

    private void selectImage(int number) {

        final CharSequence[] items = getItems();
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadPhotoActivity.this);
        builder.setTitle("Choose Photos ");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();
                } else if (items[item].equals("Set as Profile picture")) {

                    Drawable temp = getImageview().getDrawable();
                    Drawable photo = photo1.getDrawable();

                    String temp_tag = getImageview().getTag().toString();
                    String photo_tag = photo1.getTag().toString();

                    photo1.setImageDrawable(temp);
                    if (temp_tag.equals("changed")) {
                        photo1.setTag("changed");
                    }

                    if (photo_tag.equals("default")) {
                        getImageview().setTag("default");
                    }
                    getImageview().setImageDrawable(photo);

                } else if (items[item].equals("Remove Photo")) {
                    int id = getImageview().getId();
                    getImageview().setImageResource(R.drawable.photo);
                    getImageview().setTag("default");
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });


        AlertDialog image = builder.create();
        image.setTitle("Choose Photos");
        image.show();
    }


    private void cameraIntent() {

        // add permission here
        int permissionCheck = ContextCompat.checkSelfPermission(UploadPhotoActivity.this, Manifest.permission.CAMERA);

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {

            if (!getCameraPermissionStatus()) {

                Dexter.withActivity(UploadPhotoActivity.this)
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                CameraIntent();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                                setCameraPermissionStatus();
                                showSettings();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                            }
                        }).check();
            } else {
                showSettings();
            }
        } else {
            CameraIntent();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CameraIntent();
            } else {
                Toast.makeText(UploadPhotoActivity.this, "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        } else {
            GalleryIntent();
        }
    }

    private void CameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    private void galleryIntent() {

        // add permission here
        int read_permissionCheck = ContextCompat.checkSelfPermission(UploadPhotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int write_permissionCheck = ContextCompat.checkSelfPermission(UploadPhotoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (read_permissionCheck == PackageManager.PERMISSION_DENIED || write_permissionCheck == PackageManager.PERMISSION_DENIED) {

            // first time asks for both permission
            if (!getReadStoragePermissionStatus() && !getWriteStoragePermissionStatus()) {

                Dexter.withActivity(UploadPhotoActivity.this)
                        .withPermissions(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        // if both are accepted
                        if (report.areAllPermissionsGranted()) {
                            GalleryIntent();

                            // if both are rejected
                        } else if (report.getDeniedPermissionResponses().size() == 2) {
                            showStorageSettings();

                            // one of them is accepted
                        } else {
                            List<PermissionGrantedResponse> grantedPermissions = report.getGrantedPermissionResponses();
                            for (PermissionGrantedResponse grantedPermission : grantedPermissions) {
                                if (grantedPermission.getPermissionName() == Manifest.permission.READ_EXTERNAL_STORAGE) {
                                    setReadStoragePermissionStatus();
                                    showStorageSettings();
                                } else {
                                    setWriteStoragePermissionStatus();
                                    showStorageSettings();
                                }
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    }
                }).check();

            }
            // other times
            // write allowed, read rejected
            else if (!getReadStoragePermissionStatus() && getWriteStoragePermissionStatus()) {

                Dexter.withActivity(UploadPhotoActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                GalleryIntent();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                showStorageSettings();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                            }
                        }).check();

            }
            // read allowed, write rejected
            else if (!getWriteStoragePermissionStatus() && getReadStoragePermissionStatus()) {

                Dexter.withActivity(UploadPhotoActivity.this)
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                GalleryIntent();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                showStorageSettings();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                            }
                        }).check();

            } else {
                showStorageSettings();
            }
        } else {
            GalleryIntent();
        }

    }

    private void GalleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
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
        isSelected = true;

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
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
        isSelected = true;
    }

    private CircleImageView getImageview() {
        switch (number) {
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

    private CircleImageView getImageviewInstance(int number) {
        switch (number) {
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

    private void showStorageSettings() {
        Snackbar snackbar = Snackbar
                .make(view.getRootView(), "Read & Write permission needed.Go to Settings to change", Snackbar.LENGTH_LONG)
                .setAction("Settings", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });

        snackbar.show();
    }

    private void showSettings() {
        Snackbar snackbar = Snackbar
                .make(view.getRootView(), "Go to settings and grant permission", Snackbar.LENGTH_LONG)
                .setAction("Settings", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });

        snackbar.show();
    }

    private Boolean getCameraPermissionStatus() {
        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedpref.getBoolean("isCameraPermissionDenied", false);
    }

    private void setCameraPermissionStatus() {

        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = sharedpref.edit();
        edit.putBoolean("isCameraPermissionDenied", true);
        edit.apply();
    }

    private Boolean getReadStoragePermissionStatus() {
        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedpref.getBoolean("isReadPermissionDenied", false);
    }

    private void setReadStoragePermissionStatus() {

        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = sharedpref.edit();
        edit.putBoolean("isReadPermissionDenied", true);
        edit.apply();
    }

    private Boolean getWriteStoragePermissionStatus() {
        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedpref.getBoolean("isWritePermissionDenied", false);
    }

    private void setWriteStoragePermissionStatus() {

        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = sharedpref.edit();
        edit.putBoolean("isWritePermissionDenied", true);
        edit.apply();
    }

    private class FetchPhoto extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            AndroidNetworking.post(Constants.AWS_SERVER + "/fetchPhotos")
                    .addBodyParameter("customerNo", customer_id)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {

                                String profile_image = response.getString(0);
                                userImages = new ArrayList<>();

                                userImages.add("http://www.marwadishaadi.com/uploads/cust_" + customer_id + "/thumb/" + profile_image);

                                if (response.length() > 1) {
                                    for (int i = 1; i < response.length(); i++) {
                                        JSONArray u_image = response.getJSONArray(i);
                                        userImages.add("http://www.marwadishaadi.com/uploads/cust_" + customer_id + "/thumb/" + u_image.getString(0));
                                    }
                                }
                                for (int i = 0; i < userImages.size(); i++) {

                                    Picasso.with(UploadPhotoActivity.this)
                                            .load(userImages.get(i))
                                            .into(getImageviewInstance(i));

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
            ;
            return null;
        }
    }

    private class RemovePhoto extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            AndroidNetworking.post(Constants.AWS_SERVER + "/removePhotos")
                    .addBodyParameter("customerNo", customer_id)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });

            return null;
        }
    }

    private class UploadPhoto extends AsyncTask<File, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(File... params) {

            File file_name = params[0];
            AndroidNetworking.upload(Constants.AWS_SERVER + "/uploadPhotos")
                    .addMultipartFile("image_one", file_name)
                    .addMultipartParameter("customerNo", customer_id)
                    .addMultipartParameter("file_type", file_type)
                    .setPriority(Priority.HIGH)
                    .build()
                    .setUploadProgressListener(new UploadProgressListener() {
                        @Override
                        public void onProgress(long bytesUploaded, long totalBytes) {
                            // do anything with progress
                        }
                    })
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
