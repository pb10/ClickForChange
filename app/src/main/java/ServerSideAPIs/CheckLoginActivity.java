package ServerSideAPIs;

/**
 * Created by poornima on 9/9/16.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import CommunicationInterface.Communication;

public class CheckLoginActivity extends AsyncTask<String,Void,String> {

    private static final String LOG_TAG = "SIGN IN";
    private Context context;
    private TextView statusField;
    private String statusText;
    private ProgressDialog dialog;

    //flag 0 means get and 1 means post.(By default it is get.)
    public CheckLoginActivity(Context context, TextView statusField) {

        this.context = context;
        this.statusField = statusField;
        dialog = new ProgressDialog(context);

        /*this.roleField = roleField;*/
    }

    protected void onPreExecute() {

        this.dialog.setMessage("Logging In..");
        this.dialog.setCancelable(false);
        this.dialog.show();
    }



    @Override
    protected String doInBackground(String... params) {

        try{

            String IP = String.valueOf(InetAddress.getLocalHost());
            //String SERVER = "http://"+IP+"/Click4Change/";

            String username = params[0];

            String password = params[1];

            String link=ServerConfig.SERVER+"checkLogin.php";
            String data  = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                sb.append(line);

            }
            statusText = sb.toString();
            Log.v(LOG_TAG,statusText);
            //Toast.makeText(this.context, statusText, Toast.LENGTH_SHORT).show();


        }
        catch(Exception e){
            Log.e("ERROR:",e.toString());
        }

        return statusText;
    }

    @Override
    protected void onPostExecute(String result){
        this.dialog.dismiss();
        Communication l =  (Communication)context;
        l.onCompletion(result);
    }


}
