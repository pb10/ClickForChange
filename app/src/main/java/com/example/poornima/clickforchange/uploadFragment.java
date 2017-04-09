package com.example.poornima.clickforchange;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by abhishek on 29-09-2016.
 */
public class uploadFragment extends Fragment
{
    //private static Uri[] mUrls = null;
    //private static String[] strUrls = null;
    //private String[] mNames = null;
    private View view;
    private GridView gridView = null;

    //private Cursor cc = null;
    //private int gallery_geo_tagged_count = 0;
    private ProgressDialog myProgressDialog = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload , container , false);
        gridView = (GridView) view.findViewById(R.id.galleryGridView);


        Log.e("GRID",gridView.toString());
        gridView.setAdapter(new ImageAdapter(this.getContext()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent cameraIntent = new Intent(getActivity(), CameraClick.class);
                cameraIntent.putExtra("clickedImage",SplashScreen.mUrls[position].getPath());
                float[] latLong = new float[2];

                try {
                    ExifInterface exif = new ExifInterface(SplashScreen.mUrls[position].getPath());
                    exif.getLatLong(latLong);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cameraIntent.putExtra("latitude",latLong[0]);
                cameraIntent.putExtra("longitude",latLong[1]);

                Log.e("CHECK VAL", String.valueOf(cameraIntent.getFloatExtra("latitude",0)));

                startActivity(cameraIntent);
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // It have to be matched with the directory in SDCard
        /*cc = this.getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);

        if (cc != null) {

            myProgressDialog = new ProgressDialog(this.getContext());
            myProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            myProgressDialog.setMessage("PLEASE WAIT");
            //myProgressDialog.setIcon(R.drawable.blind);
            myProgressDialog.show();

            new Thread() {
                public void run() {
                    try {
                        cc.moveToFirst();
                        mUrls = new Uri[cc.getCount()];
                        strUrls = new String[cc.getCount()];
                        mNames = new String[cc.getCount()];

                        float[] latLong = new float[2];
                        ExifInterface exif;
                        for (int i = 0; i < cc.getCount(); i++) {
                            cc.moveToPosition(i);
                             exif = new ExifInterface(Uri.parse(cc.getString(1)).getPath());
                            if(exif.getLatLong(latLong))

                            {
                                mUrls[gallery_geo_tagged_count] = Uri.parse(cc.getString(1));
                                strUrls[gallery_geo_tagged_count] = cc.getString(1);
                                mNames[gallery_geo_tagged_count] = cc.getString(3);
                                gallery_geo_tagged_count++;
                            }
                            flag = 1;
                            //Log.e("mNames[i]",mNames[i]+":"+cc.getColumnCount()+ " : " +cc.getString(3));
                        }

                    } catch (Exception e) {
                    }
                    myProgressDialog.dismiss();
                }
            }.start();

        }*/


    }

    /**
     * This class loads the image gallery in grid view.
     *
     */
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            //if(flag==0)
                //return cc.getCount();
            return SplashScreen.gallery_geo_tagged_count;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.gallery_grid_item, null);

            //try {

                ImageView imageView = (ImageView) v.findViewById(R.id.grid_item_image);
                //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                // imageView.setPadding(8, 8, 8, 8);

                /*float[] latLong = new float[2];


                ExifInterface exif = new ExifInterface(SplashScreen.mUrls[position].getPath());
                if(exif.getLatLong(latLong))
                //BitmapFactory.decodeFile(mUrls[position].getPath());
                {*/
                    Bitmap bmp = decodeURI(SplashScreen.mUrls[position].getPath());
                    imageView.setImageBitmap(bmp);
                    //TextView txtName = (TextView) v.findViewById(R.id.TextView01);
                    //txtName.setText(mNames[position]);
                //}
                //bmp.

            /*} catch (Exception e) {

            }*/
            return v;
        }
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        //FlurryAgent.onStartSession(this, "***");
    }
    // @Override
    // protected void onStop() {
    // TODO Auto-generated method stub
    // super.onStop();
    // FlurryAgent.onEndSession(this);

    // }

    /**
     * This method is to scale down the image
     */
    public Bitmap decodeURI(String filePath){

        /*Bitmap click_bmp = BitmapFactory.decodeFile(filePath);

        Bitmap scaled = Bitmap.createScaledBitmap(click_bmp, 100, 100, true);*/

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Only scale if we need to
        // (16384 buffer for img processing)
        Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math.abs(options.outWidth - 100);
        if(options.outHeight * options.outWidth * 2 >= 16384){
            // Load, scaling to smallest power of 2 that'll get it <= desired dimensions
            double sampleSize = scaleByHeight
                    ? options.outHeight / 100
                    : options.outWidth / 100;
            options.inSampleSize =
                    (int)Math.pow(2d, Math.floor(
                            Math.log(sampleSize)/Math.log(2d)));
        }

        // Do the actual decoding
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[512];
        Bitmap output = BitmapFactory.decodeFile(filePath, options);

        //return scaled;
        return  output;
    }

}
