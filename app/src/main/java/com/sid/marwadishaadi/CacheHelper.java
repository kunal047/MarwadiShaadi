package com.sid.marwadishaadi;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Siddhesh on 8/4/2017.
 */

public class CacheHelper {

    static int cacheLifeHour = 7 * 24;


    public static void save(String key, String value, File cache) {

        try {

            if (cache.exists()) {
                cache.delete();
            }

            key = URLEncoder.encode(key, "UTF-8");

            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(cache));
            out.writeUTF(value);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveHash(Context context, String cacheResponseHash, String type) {

        try {
            SharedPreferences cacheinfo = context.getSharedPreferences("cacheinfo", MODE_PRIVATE);
            SharedPreferences.Editor editors = cacheinfo.edit();
            editors.putString(type, cacheResponseHash);
            editors.apply();
        } catch (Exception e) {

        }

    }

    public static String retrieveHash(Context context, String type) {
        String hash = "";
        try {

            SharedPreferences cacheinfo = context.getSharedPreferences("cacheinfo", MODE_PRIVATE);
            hash = cacheinfo.getString(type, "");


        } catch (Exception e) {

        }

        return hash;
    }

    public static void save(String key, String value, String identifier, File cache) {

        save(key + identifier, value, cache);
    }

    public static String retrieve(String key, String identifier, File cache) {

        return retrieve(key + identifier, cache);
    }


    public static String retrieve(String key, File cache) {

        try {

            key = URLEncoder.encode(key, "UTF-8");


            if (cache.exists()) {

                Date lastModDate = new Date(cache.lastModified());
                Date now = new Date();

                long diffInMillisec = now.getTime() - lastModDate.getTime();
                long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMillisec);

                diffInSec /= 60;
                diffInSec /= 60;
                long hours = diffInSec % 24;

                if (hours > cacheLifeHour) {
                    cache.delete();
                    return "";
                }

                ObjectInputStream in = new ObjectInputStream(new FileInputStream(cache));
                String value = in.readUTF();
                in.close();

                return value;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return "";
    }

    public static String generateHash(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(data.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length(
            ) < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


}