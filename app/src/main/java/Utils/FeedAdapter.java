package Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.poornima.clickforchange.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by poornima on 13/10/16.
 */
public class FeedAdapter extends BaseAdapter{

    private JSONArray result;
    Activity activity;


    public ImageLoader imageLoader;

    public Bitmap bmp = null;

    final String PROB_ID = "prob_id";
    final String PROB_IMG = "prob_image";
    final String PROB_TYPE = "prob_type";
    final String USER_ID = "user_id";
    final String LOC_ID = "loc_id";
    final String LATITUDE = "latitude";
    final String LONGITUDE = "longitude";
    final String DATE_TIME = "date_time";
    final String NUM_REACTIONS = "num_reactions";

     String IP;

    final String TAG = "ADAPTER";

    private static LayoutInflater inflater=null;


    public FeedAdapter(Activity mainActivity, JSONArray prblmList,Intent intent) {
        // TODO Auto-generated constructor stub
        result=prblmList;
        //Log.e(TAG, result.toString());
        activity=mainActivity;
        //imageId=prgmImages;
        inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }



    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        String path = null;

        String user_id = null;

        double latitude = 0;

        double longitude = 0;

        JSONObject problemObject = null;
        try {
            problemObject = result.getJSONObject(i);
            path = problemObject.getString(PROB_IMG);

            user_id = problemObject.getString(USER_ID);

            latitude = problemObject.getDouble(LATITUDE);

            longitude = problemObject.getDouble(LONGITUDE);

        } catch (JSONException e) {
            e.printStackTrace();
        }




        View rowView;
        rowView = inflater.inflate(R.layout.list_item_newsfeed, null);

        TextView description=(TextView) rowView.findViewById(R.id.list_item_textview);
       ImageView problemPic= (ImageView) rowView.findViewById(R.id.list_item_icon);


        String test = user_id+" "+latitude+" "+ longitude;
        Log.e("HiPoornima",test);
        description.setText(user_id+'\n'+latitude+'\n'+longitude);


        imageLoader.DisplayImage(path, problemPic);

        return rowView;
    }
}
