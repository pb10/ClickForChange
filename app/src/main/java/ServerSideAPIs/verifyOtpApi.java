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
 * Created by abhishek on 05-12-2016.
 */
public class verifyOtpApi extends AsyncTask<String,Void,String> {

    private static final String LOG_TAG = "VERIFY OTP";
    private Context context;
    private String response;
    private int requestId;

    public verifyOtpApi(Context context , int requestId)
    {

        this.context = context;
        this.requestId = requestId;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... params) {

        try{

            String phone = params[0];
            String otp = params[1];

            //String link=ServerConfig.SERVER+"verifyOtp.php";
            String link = "http://172.31.71.200/Click4Change/"+"verifyOtp.php";
            String data  = URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");
            data += "&" + URLEncoder.encode("otp", "UTF-8") + "=" + URLEncoder.encode(otp, "UTF-8");



            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            Log.v("verify OTP",data);
            Log.e("verify OTP",data);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                sb.append(line);

            }
            response = sb.toString();

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
        Log.v("^^^^^OTP RESULT ^^ " , result);
        l.onCompletion(result);
    }




}

