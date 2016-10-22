package com.example.poornima.clickforchange;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;

import ServerSideAPIs.UploadClickedImage;

/**
 * Created by abhishek on 29-09-2016.
 */
public class cameraFragment extends Fragment {

    public final String APP_TAG = "CAMERA_TAG";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName;
    RadioGroup radioGroup;
    public File image_file;

    // @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
       // radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        //if(radioGroup.getCheckedRadioButtonId()==-){
          //  Toast.makeText(this,"Select one option",Toast.LENGTH_SHORT).show();
       // }
        //else{

       View v = inflater.inflate(R.layout.camera , container , false);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        return v;
    }//}




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap photo;

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Uri takenPhotoUri = getPhotoFileUri(photoFileName);
//                Log.e("Camera", photoFileName);
//                // by this point we have the camera photo on disk
//                photo = BitmapFactory.decodeFile(takenPhotoUri.getPath());
//                // RESIZE BITMAP, see section below
//                // Load the taken image into a preview
//
//            } else { // Result was a failure
//                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
//                photo = null;
//            }
        } else {
            photo = null;
        }


//        ImageView imageView = (ImageView) findViewById(R.id.list_item_icon);


    }



}
