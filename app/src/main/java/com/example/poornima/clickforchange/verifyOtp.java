package com.example.poornima.clickforchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import CommunicationInterface.Communication;
import ServerSideAPIs.verifyOtpApi;

/**
 * Created by abhishek on 05-12-2016.
 */
public class verifyOtp extends AppCompatActivity implements Communication
{

    private static final String LOG_TAG = "VERIFY OTP";
    EditText phoneEditText;
    EditText otpEditText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        getSupportActionBar().setTitle("Verify OTP");



        phoneEditText = (EditText) findViewById(R.id.phone);
        otpEditText = (EditText) findViewById(R.id.otp);


        //Adding Back button
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


    }
    @Override
    public void onCompletion(String response)
    {
        if(response.matches("Verified"))
        {
            Log.v(LOG_TAG , response);
            Toast.makeText(verifyOtp.this, "Verified", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(verifyOtp.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        else
        {
            Toast.makeText(verifyOtp.this, "Error , Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCompletionSecond(String response) {

    }

    public void verifyOtp(View view)
    {
        final String phone = phoneEditText.getText().toString();
        final String otp = otpEditText.getText().toString();
        new verifyOtpApi(verifyOtp.this, 2).execute(phone, otp);

    }
}
