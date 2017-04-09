package Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
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

import ServerSideAPIs.RegisterReaction;

/**
 * Created by poornima on 13/10/16.
 */
public class FeedAdapter extends BaseAdapter{

    private JSONArray result;

    public JSONObject user_reactions;
    Activity activity;


    public ImageLoader imageLoader;

    public Bitmap bmp = null;

    final String PROB_ID = "prob_id";
    final String PROB_IMG = "prob_image";
    final String PROB_TYPE = "prob_type";
    final String PROB_DESC = "prob_desc";
    final String USER_ID = "user_id";
    final String LOC_ID = "loc_id";
    final String LATITUDE = "latitude";
    final String LONGITUDE = "longitude";
    final String DATE_TIME = "date_time";
    final String NUM_REACTIONS = "num_reactions";
    final String PROFILE_PIC = "profile_img";
    final String USER_NAME = "username";

    public static int callingFragment;


    protected SharedPreferences sharedCredentialPreferences;

    public static final String USER_REACTIONS = "user_reactions";

    final String TAG = "ADAPTER";

    String user_reaction_str;

    String user_name;

    private static LayoutInflater inflater=null;


    public FeedAdapter(Activity mainActivity, JSONArray prblmList,Intent intent, int callingActivity) {

        sharedCredentialPreferences = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        user_reaction_str = sharedCredentialPreferences.getString(USER_REACTIONS, null);

        user_name = sharedCredentialPreferences.getString(USER_NAME,null);

        //Log.e("user_reactions",user_reaction_str);

        // TODO Auto-generated constructor stub
        result=prblmList;
        //Log.e(TAG, result.toString());
        activity=mainActivity;

        //imageId=prgmImages;
        inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        imageLoader=new ImageLoader(activity.getApplicationContext());

        callingFragment = callingActivity;
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


        String prob_id = null;

        String path = null;

        String user_id = null;

        double latitude = 0;

        double longitude = 0;

        int reactions = 0;

        String profile_pic = null;

        String profile_name = null;

        String prob_desc_text = null;

        final int[] flag_react = {0};

        JSONObject problemObject = null;
        try {
            problemObject = result.getJSONObject(i);

            prob_id = (String) problemObject.get(PROB_ID);

            path = problemObject.getString(PROB_IMG);

            user_id = problemObject.getString(USER_ID);

            profile_name = problemObject.getString("user_name");

            latitude = problemObject.getDouble(LATITUDE);

            longitude = problemObject.getDouble(LONGITUDE);

            reactions = problemObject.getInt(NUM_REACTIONS);

            prob_desc_text = problemObject.getString(PROB_DESC);

            profile_pic = problemObject.getString(PROFILE_PIC);

            user_reactions = new JSONObject(user_reaction_str);

        } catch (JSONException e) {
            e.printStackTrace();
        }




        View rowView;
        rowView = inflater.inflate(R.layout.list_item_newsfeed, null);

        TextView username=(TextView) rowView.findViewById(R.id.list_item_textview);
        TextView description=(TextView) rowView.findViewById(R.id.list_item_desc);
       ImageView problemPic= (ImageView) rowView.findViewById(R.id.list_item_icon);
        ImageView profilePic = (ImageView)rowView.findViewById(R.id.profile_img);
        final TextView reactNum = (TextView)rowView.findViewById(R.id.reactions_text);

        username.setText(profile_name);
        description.setText(prob_desc_text);

       final ImageView reactButton = (ImageView)rowView.findViewById(R.id.react_button);

        int reactStatus = 0;

        if(userReactStatus(prob_id)==1)
        {
            reactStatus = 1;
            reactButton.setImageResource(R.drawable.react_done);
        }
        else
        {
            reactStatus = 0;
            reactButton.setImageResource(R.drawable.react_new);
        }


        final String finalProb_id = prob_id;
        final String finalUser_id = user_id;
        final int finalReactStatus = reactStatus;

        reactButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {



                        if(finalReactStatus == 1)
                        {
                            reactButton.setImageResource(R.drawable.react_new);
                            flag_react[0]=0;
                        }
                        else
                        {
                            reactButton.setImageResource(R.drawable.react_done);
                            flag_react[0]=1;
                        }

                        RegisterReaction registerReaction = new RegisterReaction();

                        registerReaction.execute(finalProb_id, finalUser_id,String.valueOf(finalReactStatus));
                    }
                });

        if(flag_react[0]==1)
        {
            reactions = reactions+1;
            Log.e(TAG, String.valueOf(reactions));
        }
        else if(flag_react[0]==0)
        {
            reactions = reactions-1;
            Log.e(TAG, String.valueOf(reactions));
        }



       if(reactions>0)
        {
            reactNum.setText(String.valueOf(reactions)+" AFFECTED");
        }
        else
        {
            reactNum.setText("Be the first to show concern");
        }


        imageLoader.DisplayImage(path, problemPic);

        imageLoader.DisplayImage(profile_pic,profilePic);


        return rowView;
    }

    public int userReactStatus(String prob_id)  {

        Log.e("FUNC PROB_ID",prob_id);

        Log.e("JSON OBJECT",user_reactions.toString());


        try {

            JSONArray prob_reacted = user_reactions.getJSONArray("react_prob");

            Log.e("ARRAY PROBLEMS", prob_reacted.toString());

            Log.e("COUNT", String.valueOf(user_reactions.getInt("count")));


            if (user_reactions.getInt("count") > 0)
            {

                for (int i = 1; i <= user_reactions.getInt("count"); i++) {
                    //Log.e("PROB_ID_COMPR", prob_reacted.getJSONObject(i - 1).getString("prob_id"));
                    if (prob_reacted.getJSONObject(i - 1).getString("prob_id").equals(prob_id)) {
                        return 1;
                    }
                }
            }
        }
        catch (Exception e)
        {

        }

        return 0;
    }

}
