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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public String user_id;

    double longitude;
    double latitude;

    String fileName;

    private File image_file;
    private String APP_TAG = "CAMERA CLICK";

    public static final String POSTS_KEY = "problems_server_response";

    final String PROB_ID = "prob_id";
    final String PROB_IMG = "prob_image";
    final String PROB_TYPE = "prob_type";
    final String PROB_DESC = "prob_desc";
    final String USER_ID = "user_id";
    final String LOC_ID = "loc_id";
    final String LATITUDE = "latitude";
    final String LONGITUDE = "longitude";
    final String DATE_TIME = "date_time";
    final String NUM_REACTIONS = "num_reactions";
    final String PROFILE_PIC = "profile_img";
    final String USER_NAME = "user_name";


    public static final String pref_USER_NAME = "username";
    public static final String pref_USER_PROFILE_PIC = "profile_img";
    public static final String pref_USER_NUM_POSTS = "user_posts";

    final String PROB_LIST = "problems";
    final String PROB_COUNT = "count";

    JSONArray problemArray;

    String username;
    String user_profile;
    int num_user_posts;
    String prob_id;
    String posts_string;

    SharedPreferences.Editor editor;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    protected SharedPreferences sharedCredentialPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedCredentialPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        user_id = sharedCredentialPreferences.getString("user_key", null);
        username = sharedCredentialPreferences.getString(pref_USER_NAME,null);
        user_profile = sharedCredentialPreferences.getString(pref_USER_PROFILE_PIC,null);
        num_user_posts = Integer.parseInt(sharedCredentialPreferences.getString(pref_USER_NUM_POSTS,null));

        num_user_posts = num_user_posts+1;

        editor = sharedCredentialPreferences.edit();
        editor.putString(pref_USER_NUM_POSTS, String.valueOf(num_user_posts));
        editor.commit();

        prob_id = user_id+"_prob_"+num_user_posts;


        fileName = getIntent().getStringExtra("clickedImage");

        latitude = getIntent().getFloatExtra("latitude",0);

        longitude = getIntent().getFloatExtra("longitude",0);

        Uri takenPhotoUri = getPhotoFileUri(fileName);
        Log.e("Camera", fileName);
        Log.e("Camera", String.valueOf(latitude));
        Log.e("Camera", String.valueOf(longitude));
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

            /*UploadImage uploader = new UploadImage (this, user_id, latitude, longitude,prob_type,prob_desc);
            uploader.execute(image_file.getAbsolutePath());*/


            posts_string = sharedCredentialPreferences.getString(POSTS_KEY,null);





            JSONObject new_prob_to_be_inserted = new JSONObject();
            try {
                new_prob_to_be_inserted.put(PROB_ID,prob_id);
                new_prob_to_be_inserted.put(PROB_IMG,image_file.getAbsoluteFile());
                new_prob_to_be_inserted.put(PROB_DESC,prob_desc);
                new_prob_to_be_inserted.put(LATITUDE,latitude);
                new_prob_to_be_inserted.put(LONGITUDE,longitude);
                new_prob_to_be_inserted.put(USER_ID,user_id);
                new_prob_to_be_inserted.put(NUM_REACTIONS,0);
                new_prob_to_be_inserted.put(PROFILE_PIC,user_profile);
                new_prob_to_be_inserted.put(USER_NAME,username);

                JSONObject problemsJson = new JSONObject(posts_string);
                problemArray = problemsJson.getJSONArray(PROB_LIST);

                int total_problems = problemsJson.getInt(PROB_COUNT);

                problemArray.put(total_problems,new_prob_to_be_inserted);

                total_problems = total_problems+1;

                JSONObject updated_problems_list = new JSONObject();

                updated_problems_list.put(PROB_LIST,problemArray);
                updated_problems_list.put(PROB_COUNT,total_problems);

                editor = sharedCredentialPreferences.edit();
                editor.putString(POSTS_KEY, updated_problems_list.toString());
                editor.commit();

                Log.e("PREF PROBLEMS",updated_problems_list.toString());

                Toast.makeText(this,"Uploaded",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CameraClick.this,HomeActivity.class);
                startActivity(intent);
                finish();

            } catch (JSONException e) {
                e.printStackTrace();
            }

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
