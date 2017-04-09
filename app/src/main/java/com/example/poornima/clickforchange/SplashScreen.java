package com.example.poornima.clickforchange;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Window;

public class SplashScreen extends Activity {

    public static Cursor cc;
    public static Uri[] mUrls = null;
    public static String[] strUrls = null;
    public static String[] mNames = null;
    public static int gallery_geo_tagged_count = 0;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);


        cc = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);

        if (cc != null) {
            try {
                cc.moveToFirst();

                float[] latLong = new float[2];
                ExifInterface exif;
                int count_geotagged = 0;

                for (int i = 0; i < cc.getCount(); i++) {
                    cc.moveToPosition(i);
                    exif = new ExifInterface(Uri.parse(cc.getString(1)).getPath());
                    if (exif.getLatLong(latLong)) {
                        count_geotagged = count_geotagged+1;
                    }
                    //Log.e("mNames[i]",mNames[i]+":"+cc.getColumnCount()+ " : " +cc.getString(3));
                }
                cc.moveToFirst();
                mUrls = new Uri[count_geotagged];
                strUrls = new String[count_geotagged];
                mNames = new String[count_geotagged];

                for (int i = 0; i < cc.getCount(); i++) {
                    cc.moveToPosition(i);
                    exif = new ExifInterface(Uri.parse(cc.getString(1)).getPath());
                    if (exif.getLatLong(latLong)) {
                        mUrls[gallery_geo_tagged_count] = Uri.parse(cc.getString(1));
                        strUrls[gallery_geo_tagged_count] = cc.getString(1);
                        mNames[gallery_geo_tagged_count] = cc.getString(3);
                        gallery_geo_tagged_count++;
                    }
                    //Log.e("mNames[i]",mNames[i]+":"+cc.getColumnCount()+ " : " +cc.getString(3));
                }

            } catch (Exception e) {
            }
        }

    }
}
