package ServerSideAPIs;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import CommunicationInterface.Communication;

/**
 * Created by poornima on 7/11/16.
 */
public class UploadImage extends AsyncTask<String,Void,String> {

    private Context context;
    private String user_id;
    private static String TAG = "ERROR ON UPLOAD:";

    double latitude;
    double longitude;

    public String prob_typ;
    public String prob_desc;

    //String requestURL = ServerConfig.SERVER+"UploadToServer.php";

    String requestURL = "http://172.31.71.200/Click4Change/"+"UploadToServer.php";



    ProgressDialog dialog = null;



    String serverResponseMessage = null;


    private  String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;

    public UploadImage (Context context, String user_id, double latitude, double longitude, String prob_typ, String prob_desc) {
        //this.image = image;
        this.context = context;
        this.user_id = user_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.prob_typ = prob_typ;
        this.prob_desc = prob_desc;
        dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Uploading..");
        this.dialog.setCancelable(false);
        this.dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        final String fileName = params[0];

        boundary = "===" + System.currentTimeMillis() + "===";
        URL url = null;
        try {
            url = new URL(requestURL);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(true);    // indicates POST method
            httpConn.setDoInput(true);
            httpConn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);
            outputStream = httpConn.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream),
                    true);

            addFilePart("uploaded_file",fileName);
            addFormField("username",user_id);
            addFormField("latitude", String.valueOf(latitude));
            addFormField("longitude", String.valueOf(longitude));
            addFormField("prob_typ",prob_typ);
            addFormField("prob_desc",prob_desc);

            List<String> response = finish();
            Log.e(TAG, "SERVER REPLIED:");
            for (String line : response) {
                Log.e(TAG, "Upload Files Response:::" + line);
// get your server response here.
                serverResponseMessage = line;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        return serverResponseMessage;
    }


    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param fileName a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, String fileName)
            throws IOException {

        Log.e(TAG,fileName);
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        File uploadFile = new File(fileName);

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();
        writer.append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a header field to the request.
     *
     * @param name  - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }


    public List<String> finish() throws IOException {
        List<String> response = new ArrayList<String>();
        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();

        // checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.add(line);
            }
            reader.close();
            httpConn.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        dialog.dismiss();
        Log.e("SERVER RESPONSE:",result);
        //Toast.makeText(context,"Image Uploaded",Toast.LENGTH_SHORT).show();
        Communication l = (Communication) context;
        l.onCompletion(result);
    }


}
