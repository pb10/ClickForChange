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
 * Created by poornima on 13/11/16.
 */
public class FetchUserData extends AsyncTask<String,Void,String>

{
    private static final String LOG_TAG = "FETCH: ";
    private Context context;



    public FetchUserData(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        String statusText = null;

        //TO BE REMOVED


        try {

            String username = params[0];


            String link = ServerConfig.SERVER + "fetchUserData.php";
            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");


            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);

            }
            statusText = sb.toString();
            Log.v(LOG_TAG, statusText);

            return statusText;
            //Toast.makeText(this.context, statusText, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("ERROR:", e.toString());
        }


        return statusText;

    }

    @Override
    protected void onPostExecute(String result) {

        Communication c = (Communication)context;

        c.onCompletionSecond(result);

    }

}
