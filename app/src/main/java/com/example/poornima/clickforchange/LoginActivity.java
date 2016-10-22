package com.example.poornima.clickforchange;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import CommunicationInterface.Communication;
import ServerSideAPIs.CheckLoginActivity;


public class LoginActivity extends Activity implements Communication {

    private static final String LOG_TAG = "LOGIN: ";
    private EditText usernameField,passwordField;


    public static String username;

    public static String password;

    private String statusText = null;

    public static File imagesFolder;

    public static File dataFolder;

    public static final String USER_KEY = "user_key";
    public static final String PASS_KEY = "pass_key";
    public static final String LOGIN_STATUS = "login_status";

    public final int PERMISSIONS_REQUEST_WRITE_STORAGE = 0;

    private int PERMISSIONS_REQUEST_READ_STORAGE;

    protected SharedPreferences sharedCredentialPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedCredentialPreferences = getSharedPreferences("Credentials", Context.MODE_PRIVATE);

            if(this.isLoggedIn())
            {
                Intent intent = new Intent(this, HomeActivity.class).putExtra(Intent.EXTRA_TEXT,statusText);
                startActivity(intent);
                finish();
            }
        //checking the credentials file
        imagesFolder = new File(Environment.getExternalStorageDirectory(), "/ClickForChange");



        dataFolder = new File(Environment.getExternalStorageDirectory(), "/ClickForChange/data");


        Log.e(LOG_TAG,"Login Activity!");


        int writePermissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int readPermissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);


        if (!imagesFolder.mkdirs() || (PERMISSIONS_REQUEST_WRITE_STORAGE==0)||(writePermissionCheck != PackageManager.PERMISSION_GRANTED)) {


            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_WRITE_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


            if (readPermissionCheck != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    /*ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSIONS_REQUEST_READ_STORAGE);*/

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
    }


    public boolean isLoggedIn(){
        return sharedCredentialPreferences.getBoolean(LOGIN_STATUS, false);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                if(!imagesFolder.exists())
                {
                    imagesFolder.mkdirs();
                }

                if(!dataFolder.exists())
                {
                    dataFolder.mkdirs();
                }

                    if (!imagesFolder.mkdirs()) {
                        Log.e(LOG_TAG, "Directory not created");
                        Log.e(LOG_TAG,imagesFolder.toString());
                    }
                    else
                    {
                        Log.e(LOG_TAG,imagesFolder.toString());
                    }

                    if (!dataFolder.mkdirs()) {
                        Log.e(LOG_TAG, "Directory not created");
                        Log.e(LOG_TAG,imagesFolder.toString());
                    }
                    else
                    {
                        Log.e(LOG_TAG,imagesFolder.toString());
                    }



                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    Log.e(LOG_TAG,"Permission denied");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
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

    public void CallScreen(View view) {
        usernameField = (EditText)findViewById(R.id.user_textView);
        passwordField = (EditText)findViewById(R.id.pass_textView);

        username = usernameField.getText().toString();
        password = passwordField.getText().toString();



        TextView status = (TextView) findViewById(R.id.status);
        new CheckLoginActivity(this,status).execute(username, password);
    }

    public void RegisterNewMember(View view) {

        Intent intent = new Intent(this,RegisterMember.class);
        startActivity(intent);

    }

    @Override
    public void onCompletion(String response) {
        statusText = response;
        checkUserPass();
    }


    public void checkUserPass()
    {
        Log.e(LOG_TAG, "returned: " + statusText);

        if(statusText.equals("Successful"))
        {
            SharedPreferences.Editor editor = sharedCredentialPreferences.edit();
            editor.putString(USER_KEY, username);
            editor.putString(PASS_KEY, password);
            editor.putBoolean(LOGIN_STATUS, true);
            editor.commit();

            UserData.SESSION_USER = usernameField.getText().toString();
            Intent intent = new Intent(this, HomeActivity.class).putExtra(Intent.EXTRA_TEXT,statusText);
            startActivity(intent);
            finish();
            //Toast.makeText(this,"Correct!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Invalid username/password", Toast.LENGTH_SHORT).show();
        }
    }
}
