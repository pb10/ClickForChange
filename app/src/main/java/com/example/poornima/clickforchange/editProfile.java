package com.example.poornima.clickforchange;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import CommunicationInterface.Communication;
import ServerSideAPIs.UpdateProfileData;
import ServerSideAPIs.GetProfileData;

public class editProfile extends AppCompatActivity implements Communication {

    private static final String LOG_TAG = "EDIT PROFILE";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String ADDRESS = "address";
    String NAMESTRING;
    String EMAILSTRING;
    String ADDRESSSTRING;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;

    public String requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setTitle("Edit Profile");
        // calling class
        // TODO do this dynamic for username
        new GetProfileData(this, 1).execute("9646555234");

        final Button button = (Button) findViewById(R.id.EditProfileButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText nameEditText;
                EditText emailEditText;
                EditText addressEditText;
                nameEditText = (EditText) findViewById(R.id.new_name);
                final String newName = nameEditText.getText().toString();
                emailEditText = (EditText) findViewById(R.id.new_email);
                final String newEmail = emailEditText.getText().toString();
                addressEditText = (EditText) findViewById(R.id.new_address);
                final String newAddress = addressEditText.getText().toString();

                // compare the values and then accordingly call the php
                if (!(newEmail.matches(EMAILSTRING) && newName.matches(NAMESTRING) && newAddress.matches(ADDRESSSTRING)))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(editProfile.this);
                    builder.setMessage("Are you sure you want to update details?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    new UpdateProfileData(editProfile.this, 2).execute("9646555234", newName, newEmail, newAddress);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });


        //Adding Back button
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Hook up clicks on the thumbnail views.
        final View thumb1View = findViewById(R.id.thumb_button);
        assert thumb1View != null;
        thumb1View.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(editProfile.this, thumb1View);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.profile_picture, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().toString().matches("View Profile Picture")) {
                            zoomImageFromThumb(thumb1View, R.drawable.pic);
                        }
                        //TODO correct this code
                        if (item.getTitle().toString().matches("Change Profile Picture")) {
                            Toast.makeText(editProfile.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        }
                        if (item.getTitle().toString().matches("Remove Profile Picture")) {
                            Toast.makeText(editProfile.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    }
                });

                popup.show();//showing popup menu
                mShortAnimationDuration = getResources().getInteger(
                        android.R.integer.config_shortAnimTime);
            }

        });
    }
    // On clicking back button going to home
    //// TODO: 16-10-2016 change this to profile fragment 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void zoomImageFromThumb(final View thumbView, int imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);
        expandedImageView.setImageResource(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }


    @Override
    public void onCompletionSecond(String response) {

        requestId = response;

    }

    @Override
    public void onCompletion(String response) {

        if (requestId == "1")
        {
            String profileData = response;
            try {
                populateUserData(profileData);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        if (requestId == "02")
        {
            String status = response;
            if(status.matches("Successful"))
            {
                Log.v(LOG_TAG , status);
                Toast.makeText(editProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(editProfile.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(editProfile.this, "Error , Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void populateUserData(String profileData) throws JSONException {
        JSONObject object = new JSONObject(profileData);
        NAMESTRING = object.getString(NAME);
        EMAILSTRING = object.getString(EMAIL);
        ADDRESSSTRING = object.getString(ADDRESS);
        EditText nameEditText;
        EditText emailEditText;
        EditText addressEditText;
        nameEditText = (EditText) findViewById(R.id.new_name);
        nameEditText.setText(NAMESTRING);
        emailEditText = (EditText) findViewById(R.id.new_email);
        emailEditText.setText(EMAILSTRING);
        addressEditText = (EditText) findViewById(R.id.new_address);
        addressEditText.setText(ADDRESSSTRING);

    }



}
