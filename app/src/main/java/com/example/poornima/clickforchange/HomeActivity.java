package com.example.poornima.clickforchange;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.io.File;

public class HomeActivity extends AppCompatActivity implements View.OnTouchListener {

    public static final String TAG = "Home Activity";

    public final String APP_TAG = "CAMERA_LOCATION_TAG";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    public String photoFileName;

    public File image_file;

    double longitude=30;
    double latitude=30;


    private GestureDetector gestureDetector;

    BottomBar mBottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestureDetector = new GestureDetector(this, new GestureListener());

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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
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



    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                    result = true;
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                }
                result = true;

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }
}



