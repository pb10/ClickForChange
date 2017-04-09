package com.example.poornima.clickforchange;

//import android.app.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import CommunicationInterface.Communication;
import ServerSideAPIs.ServerConfig;
import Utils.FeedAdapter;


public class homeFragment extends Fragment implements Communication,SwipeRefreshLayout.OnRefreshListener{

    public FeedAdapter feed_adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    public  Intent detail_intent;
    protected SharedPreferences posts_sharedPreferences;
    public static final String POSTS_KEY = "problems_server_response";

    View v;
    ListView listView;

    protected JSONArray problemArray;



    @Override
    public void onCreate(Bundle savedInstaceState) {

        posts_sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        if (!isNetworkAvailable(this.getContext()))
        {
            try {
                throw new Exception("No internet connectivity.");
            } catch (Exception e) {
                makeAndShowDialogBox(e.getMessage()).show();
            }
        }

        else
        {
            updateNewsFeed();
        }

        super.onCreate(savedInstaceState);
        setHasOptionsMenu(true);
    }


    public void pseudoOnCreate()
    {
        if (!isNetworkAvailable(this.getContext()))
        {
            try {
                throw new Exception("No internet connectivity.");
            } catch (Exception e) {
                makeAndShowDialogBox(e.getMessage()).show();
            }
        }

        else
        {
            updateNewsFeed();
        }
    }

    private static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null)
        {
            return false;
        } else
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
            {
                for (int i = 0; i < info.length; i++)
                {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private AlertDialog makeAndShowDialogBox(String msg)
    {
        AlertDialog myQuittingDialogBox =  new AlertDialog.Builder(this.getContext())

                .setCancelable(false)
                .setTitle("NOT CONNECTED TO ANY NETWORK")
                .setMessage(msg)

                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //whatever should be done when answering "YES" goes here
                        pseudoOnCreate();
                    }
                })//setPositiveButton
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //whatever should be done when answering "NO" goes here
                        getActivity().finish();
                    }
                })//setNegativeButton

                .create();

        return myQuittingDialogBox;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Log.e("home","Hi");
         v =  inflater.inflate(R.layout.home , container , false);
        listView = (ListView) v.findViewById(R.id.listview_feed);

        swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        /*swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        updateNewsFeed();
                                    }
                                }
        );*/

        return v;
    }

    private void updateNewsFeed() {
        GetImagesForWall wallBuilderTask = new GetImagesForWall(getActivity(),this);
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //String location = prefs.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default));
        //wallBuilderTask.execute(location);

        wallBuilderTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onRefresh() {
        updateNewsFeed();
    }

    @Override
    public void onCompletion(String response)  {
        try {
            /*SharedPreferences.Editor editor = posts_sharedPreferences.edit();
            editor.putString(POSTS_KEY,response);
            editor.commit();*/

            response = posts_sharedPreferences.getString(POSTS_KEY,null);

            problemArray = convertWallDataToJson(response);
            if(problemArray!=null)
            {

                feed_adapter = new FeedAdapter(getActivity(), problemArray,detail_intent,0);

                listView.setAdapter(feed_adapter);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        JSONObject feed_item = null;
                        try {
                            feed_item = problemArray.getJSONObject((Integer) feed_adapter.getItem(position));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Toast.makeText(getActivity(),forecast,Toast.LENGTH_SHORT).show();
                        detail_intent = new Intent(getActivity(), NewsDetail.class).putExtra(Intent.EXTRA_TEXT, feed_item.toString());
                        startActivity(detail_intent);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCompletionSecond(String response) {

    }



    private JSONArray convertWallDataToJson(String forecastJsonStr)
            throws JSONException {


        // These are the names of the JSON objects that need to be extracted.
        final String PROB_LIST = "problems";
        final String PROB_COUNT = "count";


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



    public class GetImagesForWall extends AsyncTask<String,Void,String> {


        private ProgressDialog dialog = new ProgressDialog(getContext());


        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("In Progress...");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String imagesJsonStr = null;
        Context context;
        Communication c;
        private static final String LOG_TAG = "THE WALL";

        GetImagesForWall(Context context,Communication c)
        {
            this.c =c;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast



                final String FORECAST_BASE_URL = ServerConfig.SERVER+"getImages.php";

                Uri build_url = Uri.parse(FORECAST_BASE_URL).buildUpon().build();


                URL url = new URL(build_url.toString());

                Log.v(LOG_TAG, url.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line);
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                imagesJsonStr = buffer.toString();

                Log.v(LOG_TAG, imagesJsonStr);

                     return imagesJsonStr;


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
                return  imagesJsonStr;
            }




        }


        @Override
        public void onPostExecute(String result)
        {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            c.onCompletion(result);
        }




    }

}
