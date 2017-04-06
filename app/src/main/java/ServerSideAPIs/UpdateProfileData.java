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

/**
 * Created by abhishek on 09-11-2016.
 */
public class UpdateProfileData extends AsyncTask<String,Void,String> {

    private static final String LOG_TAG = "CHANGE PROFILE DATA";
    private static final String IP = "172.31.71.187";
    private Context context;
    private String response;
    private int requestId;


    public UpdateProfileData(Context context , int requestId)
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
            String name = params[1];
            String email = params[2];
            String address = params[3];

            String link=ServerConfig.SERVER+"updateProfile.php";
            String data  = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
            data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8");



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
            response = sb.toString();

//            Log.v(LOG_TAG,response);
//            Log.i(LOG_TAG,response);
//            Log.e(LOG_TAG,response);
            //Toast.makeText(this.context, statusText, Toast.LENGTH_SHORT).show();


        }
        catch(Exception e){
            Log.e("ERROR:",e.toString());
        }

        return response;
    }

    @Override
    protected void onPostExecute(String result)
    {
        Communication l =  (Communication)context;
        Log.v("^^^^^" , result);
        l.onCompletion(result);
        l.onCompletionSecond(String.valueOf(requestId));
    }




}
