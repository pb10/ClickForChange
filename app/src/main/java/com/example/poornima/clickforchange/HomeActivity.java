package com.example.poornima.clickforchange;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;

public class HomeActivity extends AppCompatActivity {

    public static final String TAG = "Home Activity";


    BottomBar mBottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().setTitle("Click For Change");
        Log.e(TAG,"Inside Home");
        mBottomBar = BottomBar.attach(this,savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if(menuItemId==R.id.Bottombaritemone)
                {
                    homeFragment f = new homeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame ,f).commit();
                }
                else
                {
                    if(menuItemId==R.id.Bottombaritemtwo)
                    {
                        uploadFragment f = new uploadFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame ,f).commit();
                    }
                    else
                    {
                        if(menuItemId==R.id.Bottombaritemthree)
                        {

                            cameraFragment f = new cameraFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame ,f).commit();
                        }
                        else
                        {
                            if(menuItemId==R.id.Bottombaritemfour)
                            {
                                notificationFragment f = new notificationFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame ,f).commit();
                            }
                            else
                            {
                                if(menuItemId==R.id.Bottombaritemfive)
                                {
                                    profileFragment f = new profileFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.frame ,f).commit();
                                }

                            }
                        }
                    }
                }






            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
            }
        });
        mBottomBar.mapColorForTab(0,"#616161");
        mBottomBar.mapColorForTab(1,"#616161");
        mBottomBar.mapColorForTab(2,"#616161");
        mBottomBar.mapColorForTab(3,"#616161");
        mBottomBar.mapColorForTab(4,"#616161");

        BottomBarBadge unread;
        unread = mBottomBar.makeBadgeForTabAt(3,"#2196F3" , 13);
        unread.show();

    }



    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.home, container, false);
            return rootView;
        }
    }


    public void openEditProfile(View view)
    {
        Intent intent = new Intent(this, editProfile.class);
        startActivity(intent);
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        Log.e(TAG,"Back pressed");
        if (exit) {
            finish(); // finish activity
        } else {
            exit = true;
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3000);

        }

    }




}



