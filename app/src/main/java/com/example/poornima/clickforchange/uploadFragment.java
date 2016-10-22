package com.example.poornima.clickforchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by abhishek on 29-09-2016.
 */
public class uploadFragment extends Fragment
{
    private final int PICK_IMAGE_REQUEST = 1;
    public void onCreate(Bundle savedInstaceState)
    {
        super.onCreate(savedInstaceState);
        setHasOptionsMenu(true);

        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    //@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.upload , container , false);
        return v;
    }
}
