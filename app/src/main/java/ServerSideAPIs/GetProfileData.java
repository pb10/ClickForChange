package ServerSideAPIs;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import CommunicationInterface.Communication;


public class GetProfileData extends AsyncTask<String,Void,String>   {

    private static final String LOG_TAG = "PROFILE DATA";
    private static final String IP = "172.31.71.187";
    private Context context;
    private int requestId;
    private String profileData;

    public GetProfileData(Context context , int requestId)
    {
        this.context = context;
        this.requestId = requestId;

    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... params) {

        try{

            String username = params[0];

//            String link="http://172.31.68.62/Click4Change/"+"getProfileData.php";
            String link=ServerConfig.SERVER+"getProfileData.php";
//            String link="http://192.168.43.197/Click4Change/"+"getProfileData.php";
            String data  = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            System.out.println("********** i am here " + data);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            Log.e("user",data);


            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                sb.append(line);

            }
            profileData = sb.toString();
        }
        catch(Exception e){
            Log.e("ERROR:",e.toString());
        }

        return profileData;
    }

    @Override
    protected void onPostExecute(String result)
    {
        Communication l =  (Communication)context;
        //Log.v(LOG_TAG,result);
        l.onCompletion(result);
    }




}
