package com.example.poornima.clickforchange;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by abhishek on 30-09-2016.
 */
public class notificationFragment extends Fragment
{
    //@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.notification , container , false);
        return v;
    }
}
