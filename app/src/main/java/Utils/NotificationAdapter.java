package Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
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
 * Created by poornima on 24/10/16.
 */
public class NotificationAdapter extends BaseAdapter {

    public ImageLoader imageLoader;

    public Bitmap bmp = null;

    final String PROB_ID = "prob_id";
    final String PROB_IMG = "prob_img";
    final String FROM_USER = "from_user";

    final String TAG = "ADAPTER";

    private static LayoutInflater inflater=null;

    private JSONArray result;
    Activity activity;

    public NotificationAdapter(Activity mainActivity, JSONArray notificationList)
    {
        activity = mainActivity;
        result = notificationList;

        inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

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
    public View getView(int i, View view, ViewGroup viewGroup) {

        String pic_path = null;

        String prob_id = null;

        String from_user = null;

        JSONObject notificationObject = null;
        try {
            notificationObject = result.getJSONObject(i);

            pic_path = notificationObject.getString(PROB_IMG);

            prob_id = notificationObject.getString(PROB_ID);

            from_user = notificationObject.getString(FROM_USER);

        } catch (JSONException e) {
            e.printStackTrace();
        }




        View rowView;
        rowView = inflater.inflate(R.layout.notification_item, null);

        TextView notification=(TextView) rowView.findViewById(R.id.textview_notification);

        ImageView problemPic= (ImageView) rowView.findViewById(R.id.imageview_notification);


        notification.setText(from_user+" reacted to your photo "+prob_id);


        imageLoader.DisplayImage(pic_path, problemPic);

        return rowView;
    }
}
