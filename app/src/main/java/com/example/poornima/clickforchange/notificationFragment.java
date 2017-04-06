package com.example.poornima.clickforchange;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import CommunicationInterface.Communication;
import ServerSideAPIs.GetNotifications;
import Utils.NotificationAdapter;

/**
 * Created by abhishek on 30-09-2016.
 */


public class notificationFragment extends Fragment implements Communication
{

    String username;

    public String dataForNotification;

    SharedPreferences sharedCredentialPreferences;

    public ListView notificationList;

    public NotificationAdapter notificationAdapter;

    public JSONArray notificationArray;

    @Override
    public void onCreate(Bundle savedInstaceState) {

        sharedCredentialPreferences = this.getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);

        username = sharedCredentialPreferences.getString("user_key",null);

        super.onCreate(savedInstaceState);
        setHasOptionsMenu(true);
        new GetNotifications(this.getContext()).execute(username);
    }
    //@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.notification , container , false);

        notificationList = (ListView) v.findViewById(R.id.listview_notification);

        return v;
    }

    @Override
    public void onCompletion(String response) {
        dataForNotification = response;
        try {
            notificationArray = convertDataToJson(dataForNotification);
            if(notificationArray!=null)

            {
                notificationAdapter = new NotificationAdapter(getActivity(), notificationArray);

                notificationList.setAdapter(notificationAdapter);

                notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        JSONObject feed_item = null;
                        try {
                            feed_item = notificationArray.getJSONObject((Integer) notificationAdapter.getItem(position));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Toast.makeText(getActivity(),forecast,Toast.LENGTH_SHORT).show();
                        Intent detail_intent = new Intent(getActivity(), NewsDetail.class).putExtra(Intent.EXTRA_TEXT, feed_item.toString());
                        startActivity(detail_intent);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletionSecond(String response) {

    }

    private JSONArray convertDataToJson(String forecastJsonStr)
            throws JSONException {


        // These are the names of the JSON objects that need to be extracted.
        final String LIST = "notifications";
        final String COUNT = "count";


        JSONObject problemsJson = new JSONObject(forecastJsonStr);
        JSONArray notifArray;
        notifArray = problemsJson.getJSONArray(LIST);

        int total_problems = problemsJson.getInt(COUNT);


        String[] resultStrs = new String[total_problems];
        for (int i = 0; i < total_problems; i++) {


            // Get the JSON object representing the problem
            JSONObject problemObject = notifArray.getJSONObject(i);
            resultStrs[i] = problemObject.toString();
        }

        return notifArray;

    }

}
