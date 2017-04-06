package com.example.poornima.clickforchange;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Utils.GPSTracker;

/**
 * Created by abhishek on 29-09-2016.
 */
public class cameraFragment extends Fragment {

    public final String APP_TAG = "CAMERA_TAG";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName;
    RadioGroup radioGroup;
    public File image_file;

    public int flag = 0;
    public int settingsFlag = 0;

    double longitude=0;
    double latitude=0;


    public GPSTracker gps;


    ProgressDialog progressBar;
    int progressBarStatus = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        gps = new GPSTracker(getActivity());
        photoFileName = (new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()))+".jpg";

        if (gps.canGetLocation()) {
            latitude = gps.getLatitude(); // returns latitude
            longitude = gps.getLongitude(); // returns longitude


        } else {
            gps.showSettingsAlert();
        }

        View v = inflater.inflate(R.layout.camera , container , false);

        progressBar = new ProgressDialog(getContext());
        progressBar.setCancelable(false);
        progressBar.setMessage("Fetching location...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        progressBarStatus = 0;

        progressBarStatus = getLocation();

        if(latitude!=0&&longitude!=0)
        {
            progressBar.dismiss();

            TextView geoTextView = (TextView) v.findViewById(R.id.geoTextview);
            geoTextView.setText("Latitude: " + latitude + '\n' + "Longitude: " + longitude);

            flag = 1;

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                }
            }, 5000);
        }


        if(flag==1)

        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName));

            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {

                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        }

        return v;
    }//}


    private int getLocation()
    {
        Log.e(APP_TAG, String.valueOf(latitude));
        Log.e(APP_TAG, String.valueOf(longitude));

        if(latitude > 0 && longitude > 0)
            return 100;
        else
            return 0;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap photo;

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                Uri takenPhotoUri = getPhotoFileUri(photoFileName);
                Log.e("Camera", photoFileName);
                // by this point we have the camera photo on disk
                photo = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview

                geoTag(image_file.getAbsolutePath(), latitude, longitude);

                Intent cameraIntent = new Intent(getActivity(), CameraClick.class);
                cameraIntent.putExtra("clickedImage",LoginActivity.imagesFolder.getPath() + File.separator + photoFileName);
                cameraIntent.putExtra("latitude",latitude);
                cameraIntent.putExtra("longitude",longitude);
                startActivity(cameraIntent);
                getActivity().finish();

            } else { // Result was a failure
                Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                photo = null;
            }

            }

        else {
                Intent homeIntent = new Intent(getActivity(),HomeActivity.class);
                startActivity(homeIntent);
                getActivity().finish();
        }


//        ImageView imageView = (ImageView) findViewById(R.id.list_item_icon);

    }


    public Uri getPhotoFileUri(String fileName) {

        image_file = new File(LoginActivity.imagesFolder.getPath() + File.separator + fileName);
        // Return the file target for the photo based on filename
        Log.e(APP_TAG,Uri.fromFile(image_file).toString());
        return Uri.fromFile(image_file);

    }


    public void geoTag(String filename, double latitude, double longitude){

        ExifInterface exif;

        try {
            exif = new ExifInterface(filename);
            int num1Lat = (int)Math.floor(latitude);
            int num2Lat = (int)Math.floor((latitude - num1Lat) * 60);
            double num3Lat = (latitude - ((double)num1Lat+((double)num2Lat/60))) * 3600000;

            int num1Lon = (int)Math.floor(longitude);
            int num2Lon = (int)Math.floor((longitude - num1Lon) * 60);
            double num3Lon = (longitude - ((double)num1Lon+((double)num2Lon/60))) * 3600000;


            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, num1Lat+"/1,"+num2Lat+"/1,"+num3Lat+"/1000");
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, num1Lon+"/1,"+num2Lon+"/1,"+num3Lon+"/1000");

            /*Log.e("Geotag",exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
            Log.e("Geotag",exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
*/
            if (latitude > 0) {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
            }

            if (longitude > 0) {
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
            }

            exif.saveAttributes();
        } catch (IOException e) {
            Log.e("PictureActivity", e.getLocalizedMessage());
        }

    }



}
