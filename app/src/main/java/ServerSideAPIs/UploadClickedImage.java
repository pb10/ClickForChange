package ServerSideAPIs;

/**
 * Created by poornima on 28/9/16.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class UploadClickedImage extends AsyncTask<Void,Void,String>{
    private Bitmap image;
    private Context context;
    private String user_id;
    private static String TAG = "ERROR ON UPLOAD:";
    String statusText="";
    double latitude;
    double longitude;

    public UploadClickedImage(Bitmap image,Context context,String user_id,double latitude,double longitude){
        this.image = image;
        this.context = context;
        this.user_id = user_id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected String doInBackground(Void... params) {

        Log.v(TAG,"Inside uploader");
        String encodeImage = bitmapToBase64(image);

        Log.v(TAG,encodeImage);


        try{

            String IP = String.valueOf(InetAddress.getLocalHost());
            String SERVER = "http://"+IP+"/Click4Change/";
            String link=SERVER+"upload.php";
            String data  = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(encodeImage, "UTF-8");
            data += "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(latitude), "UTF-8");
            data += "&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(longitude), "UTF-8");

           Log.e(TAG,data);

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
            Log.e(TAG, statusText);

            return statusText;
            //Toast.makeText(this.context, statusText, Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            Log.e("ERROR:",e.toString());
        }
        return statusText;
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    protected void onPostExecute(String s) {
        //show image uploaded
       if(statusText.equals("Successful"))
       {
           Toast.makeText(context,"Image Uploaded",Toast.LENGTH_SHORT).show();
       }
        else
       {
           Toast.makeText(context,"Try Again Later",Toast.LENGTH_SHORT).show();
       }
    }
}
