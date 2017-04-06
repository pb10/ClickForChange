package com.example.poornima.clickforchange;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;

import CommunicationInterface.Communication;
import ServerSideAPIs.UploadImage;

/**
 * Created by vishal on 10/15/2016.
 */
public class CameraClick extends Activity implements Communication {


    public Bitmap click_bmp;
    public RadioGroup radioGroup;
    public RadioButton radioButton;
    public String prob_type;
    public String prob_desc;
    public String username;

    double longitude=30;
    double latitude=30;

    String fileName;

    private File image_file;
    private String APP_TAG = "CAMERA CLICK";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    protected SharedPreferences sharedCredentialPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedCredentialPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        username = sharedCredentialPreferences.getString("user_key", null);

        fileName = getIntent().getStringExtra("clickedImage");

        latitude = getIntent().getDoubleExtra("latitude",30);

        longitude = getIntent().getDoubleExtra("longitude",30);

        Uri takenPhotoUri = getPhotoFileUri(fileName);
        Log.e("Camera", fileName);
        // by this point we have the camera photo on disk
        click_bmp = BitmapFactory.decodeFile(takenPhotoUri.getPath());

        Bitmap scaled = Bitmap.createScaledBitmap(click_bmp, 160, 160, true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_click);

        ImageView clickedImage = (ImageView) findViewById(R.id.clickedImageView);
        clickedImage.setImageBitmap(scaled);

    }



    public Uri getPhotoFileUri(String fileName) {

        image_file = new File(fileName);
        // Return the file target for the photo based on filename
        Log.e(APP_TAG,Uri.fromFile(image_file).toString());
        return Uri.fromFile(image_file);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_camera_click, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ClickPicture(View view) {

        radioGroup = (RadioGroup)findViewById(R.id.cameraRadioGroup);
        if (radioGroup.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(this, "Select one option", Toast.LENGTH_SHORT).show();
        }
        else
        {
            int selectedId = radioGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            radioButton = (RadioButton) findViewById(selectedId);

            prob_type = (String) radioButton.getText();

            EditText cameraEdittext = (EditText) findViewById(R.id.cameraEdittext);

            prob_desc = cameraEdittext.getText().toString();

            UploadImage uploader = new UploadImage (this, username, latitude, longitude,prob_type,prob_desc);
            uploader.execute(image_file.getAbsolutePath());

        }


       }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard the post?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        Intent intent = new Intent(CameraClick.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("No",null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onCompletion(String response) {
        String statusText = response;

        //Log.e(APP_TAG,statusText);

        if(statusText.equals("success"))
        {
            Log.e(APP_TAG,"true");
            Toast.makeText(this,"Uploaded",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CameraClick.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(this,"Try Again Later",Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onCompletionSecond(String response) {

    }
}
