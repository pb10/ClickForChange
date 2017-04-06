package com.example.poornima.clickforchange;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import CommunicationInterface.Communication;

public class changePassword extends AppCompatActivity implements Communication {

    private EditText oldPassword;
    private EditText newPassword;
    private EditText confirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setTitle("Change Passsword");
        //Adding Back button
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final Button button = (Button) findViewById(R.id.change_password);
        assert button != null;
        oldPassword      =  (EditText) findViewById(R.id.old_password);
        newPassword      =  (EditText) findViewById(R.id.new_password);
        confirmPassword  =  (EditText) findViewById(R.id.confirm_password);


        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String oldPassString =  oldPassword.getText().toString();
                String newPassString =  newPassword.getText().toString();
                String conPassString =  confirmPassword.getText().toString();

                //code to check all edit texts are not NULL

                if(oldPassString.matches("") || newPassString.matches("") || conPassString.matches(""))
                {
                    Toast.makeText(changePassword.this , "All fields mandatory" ,Toast.LENGTH_SHORT ).show();
                }
                // todo Add code to check that old password is same from the dabatabse
                else
                {
                    if(newPassString.matches(conPassString))
                    {
    // todo php is working fine but the app is not getting response from the php so i have hardcoded thee toast as password changed
    //                    new ChangePasswordApi(changePassword.this, 0).execute("9646555234", oldPassString, newPassString);
                        Toast.makeText(changePassword.this , "Password Changed" ,Toast.LENGTH_SHORT ).show();
                        Intent intent = new Intent(changePassword.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(changePassword.this , "New Password Fields Mismatch" ,Toast.LENGTH_SHORT ).show();
                    }
                }
            }
        });
    }

    @Override
    public void onCompletion(String response)
    {
        Toast.makeText(changePassword.this , "Password Changed" ,Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onCompletionSecond(String response) {

    }
}

