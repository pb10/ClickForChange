package com.example.poornima.clickforchange;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Utils.FeedAdapter;
import Utils.ImageLoader;

/**
 * Created by abhishek on 29-09-2016.
 */


public class profileFragment extends Fragment {

    private Bundle savedState = null;
    private ListView user_feed_listview;
    private FeedAdapter user_posts_feed_adapter;
    public static JSONArray userProblemsArray;
    protected SharedPreferences sharedCredentialPreferences;
    public static final String POSTS_KEY = "problems_server_response";
    final String USER_ID = "user_id";
    public static final String USER_KEY = "user_key";
    String current_user;

    //@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());

        ImageLoader imageLoader = new ImageLoader(this.getContext());

        String user_profile_name = preferences.getString("username",null);
        String user_profile_img = preferences.getString("profile_img",null);
        String user_posts = preferences.getString("user_posts",null);

        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.profile, container, false);

        ImageView profile_img = (ImageView)v.findViewById(R.id.user_profile_pic);
        imageLoader.DisplayImage(user_profile_img,profile_img);

        TextView profile_user_name = (TextView)v.findViewById(R.id.profile_name);
        profile_user_name.setText(user_profile_name.toUpperCase());

        TextView noOfPosts = (TextView) v.findViewById(R.id.noOfPosts);
        noOfPosts.setText(user_posts+" POSTS");

        sharedCredentialPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        String posts_string = sharedCredentialPreferences.getString(POSTS_KEY,null);
         current_user = sharedCredentialPreferences.getString(USER_KEY,null);
        userProblemsArray = new JSONArray();
        String current_user_posts = "";
        int count = 0;

        try {
            JSONArray allProbsArray = convertWallDataToJson(posts_string);

            for(int i=0;i<allProbsArray.length();i++)
            {
                JSONObject problemObject = allProbsArray.getJSONObject(i);
                String user_id = problemObject.getString(USER_ID);

                if(user_id.equals(current_user))
                {
                    /*String prob = problemObject.toString();
                    current_user_posts = current_user_posts+prob+'\n';
                    count++;*/
                    userProblemsArray.put(allProbsArray.get(i));
                }
            }

            /*JSONObject userPosts = new JSONObject();
            userPosts.put("PROBLEMS",current_user_posts);

            Log.e("USER POSTS",current_user_posts);*/


            //userProblemsArray = userPosts.getJSONArray("PROBLEMS");

            Log.e("USER JSON",userProblemsArray.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        user_feed_listview = (ListView) v.findViewById(R.id.listview_user_posts_feed);
        if(userProblemsArray!=null) {

            user_posts_feed_adapter = new FeedAdapter(getActivity(), userProblemsArray, null,1);

            user_feed_listview.setAdapter(user_posts_feed_adapter);
        }

        setHasOptionsMenu(true);


        /* If the Fragment was destroyed inbetween (screen rotation), we need to recover the savedState first */
        /* However, if it was not, it stays in the instance from the last onDestroyView() and we don't want to overwrite it */
        if (savedInstanceState != null && savedState == null) {
            savedState = savedInstanceState.getBundle("");
        }
        if (savedState != null) {
            noOfPosts.setText(savedState.getCharSequence(""));
        }
        savedState = null;

        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here

        inflater.inflate(R.menu.profilemenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
//        return true;


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.editProfile:
//                Intent intent = new Intent(Intent.ACTION_SEND);
                Intent intent = new Intent(getContext(), editProfile.class);
                startActivity(intent);
                return true;
            case R.id.changePassword:
                Intent intent2 = new Intent(getContext(), changePassword.class);
                startActivity(intent2);
                return true;
            case R.id.logout:
                Intent intent5 = new Intent(getContext(), logout.class);
                startActivity(intent5);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private JSONArray convertWallDataToJson(String forecastJsonStr)
            throws JSONException {


        // These are the names of the JSON objects that need to be extracted.
        final String PROB_LIST = "problems";
        final String PROB_COUNT = "count";

        JSONArray problemArray;


        JSONObject problemsJson = new JSONObject(forecastJsonStr);
        problemArray = problemsJson.getJSONArray(PROB_LIST);

        int total_problems = problemsJson.getInt(PROB_COUNT);


        String[] resultStrs = new String[total_problems];
        for (int i = 0; i < total_problems; i++) {


            // Get the JSON object representing the problem
            JSONObject problemObject = problemArray.getJSONObject(i);
            resultStrs[i] = problemObject.toString();
        }


        return problemArray;

    }
}
