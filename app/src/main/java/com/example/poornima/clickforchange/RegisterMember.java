package com.example.poornima.clickforchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import CommunicationInterface.Communication;
import ServerSideAPIs.AddNewMember;


public class RegisterMember extends ActionBarActivity implements Communication {

    private String name;
    private String phone;
    private String password;
    private String confirm_password;
    private String gender;
    private String emailAddr;
    private String AdditionStatusText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_member);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_member, menu);
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

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_female:
                if (checked)
                    gender = "F";
                    break;
            case R.id.radio_male:
                if (checked)
                    gender="M";
                    break;
        }
    }

    public void CallScreen(View view) {

        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void RegisterMember(View view) {

        EditText nameField = (EditText)findViewById(R.id.user_name_textView);
        name = nameField.getText().toString();

        EditText password_Field = (EditText)findViewById(R.id.pass_textView);
        password = password_Field.getText().toString();

        EditText confirm_pass_Field = (EditText)findViewById(R.id.confirm_pass_textView);
        confirm_password = confirm_pass_Field.getText().toString();

        EditText phone_Field = (EditText)findViewById(R.id.user_phone_textView);
        phone = phone_Field.getText().toString();

        EditText email_Field = (EditText)findViewById(R.id.email_textView);
        emailAddr = email_Field.getText().toString();

        if(password.equals(confirm_password))
        {
             new AddNewMember(this).execute(name,password,phone,emailAddr,gender);
        }

        else
        {
            Toast.makeText(this,"Confirm password does not match",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onCompletion(String response) {

        AdditionStatusText = response;

        addMember();
        finish();
    }

    @Override
    public void onCompletionSecond(String response) {

    }

    public void addMember()
    {
        Log.e("^^^^" , AdditionStatusText);
        if(AdditionStatusText.equals("Success"))
        {

            Toast.makeText(this,"Registered, Please Verify it :)",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, verifyOtp.class);
            startActivity(intent);

        }

        else
        {
            if(AdditionStatusText.equals("Number registered but not verfied"))
            {

                Toast.makeText(this,"Number not verified , please verify it",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, verifyOtp.class);
                startActivity(intent);
            }
            else
            {
                if(AdditionStatusText.equals("Already Registered"))
                {
                    Toast.makeText(this,"Already registered, Login to continue",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(this,"Please try again later",Toast.LENGTH_SHORT).show();
                }

            }
        }

    }
}
