package com.example.poornima.clickforchange;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import ServerSideAPIs.ServerConfig;
import Utils.ImageLoader;

public class NewsDetail extends ActionBarActivity implements OnMapReadyCallback {


    String feed_detail;

    double latitude;
    double longitude;

    final String LATITUDE = "latitude";
    final String LONGITUDE = "longitude";

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        Intent intent = this.getIntent();

        if(intent!=null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            feed_detail = intent.getStringExtra(Intent.EXTRA_TEXT);
        }

        try {
            JSONObject problemObject = new JSONObject(feed_detail);



            latitude = problemObject.getDouble(LATITUDE);

            longitude = problemObject.getDouble(LONGITUDE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

         SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
         mapFragment.getMapAsync(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
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
            //startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng prob_location = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(prob_location).title("THE PROBLEM CAUSEd HERE"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(prob_location));

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        final String PROB_ID = "prob_id";
        final String PROB_IMG = "prob_image";
        final String PROB_TYPE = "prob_type";
        final String USER_ID = "user_id";
        final String LOC_ID = "loc_id";
        final String LATITUDE = "latitude";
        final String LONGITUDE = "longitude";
        final String DATE_TIME = "date_time";
        final String NUM_REACTIONS = "num_reactions";


        String user_id = null;

        double latitude = 0;

        double longitude = 0;


        String path;

        ImageLoader imageLoader;


        private static final String LOG_TAG = PlaceholderFragment.class.getSimpleName();

        private static final String FORECAST_HASH_TAG = "#ClickForChange!";

        private String feed_detail;

        public PlaceholderFragment() {

            imageLoader=new ImageLoader(this.getContext());

            setHasOptionsMenu(true);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            Intent intent = getActivity().getIntent();

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            if(intent!=null && intent.hasExtra(Intent.EXTRA_TEXT))
            {
                feed_detail = intent.getStringExtra(Intent.EXTRA_TEXT);

                path = null;



                try {
                    JSONObject problemObject = new JSONObject(feed_detail);

                    path = problemObject.getString(PROB_IMG);

                    user_id = problemObject.getString(USER_ID);

                    latitude = problemObject.getDouble(LATITUDE);

                    longitude = problemObject.getDouble(LONGITUDE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String detail_feed_item = user_id+'\n'+latitude+'\n'+longitude;

                ((TextView)rootView.findViewById(R.id.detail_text)).setText(detail_feed_item);

                  ImageView problemPic = (ImageView) rootView.findViewById(R.id.detail_problemPic);

                  imageLoader.DisplayImage(path, problemPic);
            }




            return rootView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.detailfragment, menu);

            // Retrieve the share menu item
            MenuItem menuItem = menu.findItem(R.id.action_share);

            // Get the provider and hold onto it to set/change the share intent.
            ShareActionProvider mShareActionProvider =
                    (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

            // Attach an intent to this ShareActionProvider.  You can update this at any time,
            // like when the user selects a new piece of data they might like to share.
            if (mShareActionProvider != null ) {
                mShareActionProvider.setShareIntent(shareForecastIntent());
            } else {
                Log.d(LOG_TAG, "Share Action Provider is null?");
            }
        }


        private Intent shareForecastIntent()
        {
            Intent share_intent = new Intent(Intent.ACTION_SEND);
            share_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            share_intent.setType("image/jpeg");
            String url = ServerConfig.SERVER+path;
            Bitmap bmp = imageLoader.getBitmap(url);
            share_intent.putExtra(Intent.EXTRA_TEXT,feed_detail+FORECAST_HASH_TAG);
            if(bmp!=null)
            {
                share_intent.putExtra(Intent.EXTRA_STREAM, getImageUri(this.getContext(), bmp));
            }
            return share_intent;
        }

        public Uri getImageUri(Context inContext, Bitmap inImage) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            return Uri.parse(path);
        }
    }
}