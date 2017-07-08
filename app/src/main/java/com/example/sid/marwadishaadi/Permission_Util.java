package com.example.sid.marwadishaadi;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.sid.marwadishaadi.Forgot_Password.ForgotPasswordActivity;

/**
 * Created by Sid on 04-Jul-17.
 */

public class Permission_Util implements ActivityCompat.OnRequestPermissionsResultCallback{

    public static final int CAMERA_PERMISSION = 100;
    public static final int INTERNET_PERMISSION=101;
    public static final int CALL_PHONE_PERMISSION=102;
    public static  final int READ_EXTERNAL_STORAGE=103;
    public static final int WRITE_EXTERNAL_STORAGE=104;
    public static final int REQUEST_PERMISSION_SETTING = 105;
    private static SharedPreferences permissionStatus;
    private static boolean sentToSettings = false;
    private String permission_type;
    private Activity activity;
    private String alert_title;
    private String alert_msg;

    public Permission_Util(Activity activity,String permission_type,String title){
        this.activity = activity;
        this.permission_type = permission_type;
        this.alert_title ="Need " + title + " Permission";
        this.alert_msg = "This app needs " + title + " permission";
    }
    
    public boolean checkPermission(){

        if (ActivityCompat.checkSelfPermission(activity, permission_type) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity,permission_type)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(alert_title);
                builder.setMessage(alert_msg);
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(activity, new String[]{permission_type},getPermissionConstant(permission_type));
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            } else if (permissionStatus.getBoolean(permission_type,false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(alert_title);
                builder.setMessage(alert_msg);
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(activity, "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(activity, new String[]{permission_type}, getPermissionConstant(permission_type));
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permission_type,true);
            editor.commit();

        } else {
            //You already have the permission, just go ahead.
            return true;
        }
        return false;
    }


    public static int getPermissionConstant(String permission_type){

        switch (permission_type){
            case android.Manifest.permission.CAMERA:
                return CAMERA_PERMISSION;
            case android.Manifest.permission.INTERNET:
                return INTERNET_PERMISSION;
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                return READ_EXTERNAL_STORAGE;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                return WRITE_EXTERNAL_STORAGE;
            case Manifest.permission.CALL_PHONE:
                return CALL_PHONE_PERMISSION;
        }
        return CAMERA_PERMISSION;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == getPermissionConstant(this.permission_type)) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                ForgotPasswordActivity forgotPasswordActivity = new ForgotPasswordActivity();
                forgotPasswordActivity.Call();

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission_type)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(alert_title);
                    builder.setMessage(alert_msg);
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(activity, new String[]{permission_type},getPermissionConstant(permission_type));
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(activity,"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}

