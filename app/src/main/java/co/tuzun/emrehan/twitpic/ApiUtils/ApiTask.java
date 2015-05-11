package co.tuzun.emrehan.twitpic.ApiUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import co.tuzun.emrehan.twitpic.App;
import retrofit.http.POST;

public class ApiTask extends AsyncTask<String, String, Void> {
    private static final String TAG = "apitask";
    private static final int POST_TWEET = 1;
    private static final int GET_MY_TWEETS = 2;

    private ApiListener listener;
    private Context con;
    private int apiRequest;
    private String[] params;

    public ApiTask(Context context, ApiListener listener){
        this.listener = listener;
        this.con = context;
    }

    public void run(){
        this.execute(params);
    }

    public void setApiRequest(int a){
        this.apiRequest = a;
    }

    public void setParams(String... params){
        this.params = params;
    }
    @Override
    protected void onProgressUpdate(String... progress) {
        if(listener!=null){
            if(progress[0].equals("success")){

                listener.onSuccess(progress[1]);
            }
            else if(progress[0].equals("failed")){

                listener.onFail(progress[1]);
            }
        }

    }

    @Override
    protected Void doInBackground(String... params) {

        HttpClient httpClient = new DefaultHttpClient();

        HttpUriRequest httpUriRequest = prepareHttpRequest(apiRequest, params);

        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpUriRequest);;
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String aux = "";

            while ((aux = reader.readLine()) != null) {
                builder.append(aux);
            }

            String text = builder.toString();

            Log.d(TAG, "body : " + text);

            if(httpResponse.getStatusLine().getStatusCode() == 200){
                //All of these requests returns user info
                if(apiRequest == POST_TWEET){
                    JSONObject jsonReader = new JSONObject(text);
                    String image = jsonReader.getString("image");
                    String id = jsonReader.getString("id");

                    URL url = new URL (image);
                    InputStream input = url.openStream();
                    try {

                        File folder = new File(App.outputFolder);
                        if(!folder.exists()){
                            folder.mkdir();
                        }
                        File outputFile = new File(folder, id + ".jpg");
                        OutputStream output = new FileOutputStream(outputFile.getPath());
                        try {
                            byte[] buffer = new byte[8096];
                            int bytesRead = 0;
                            while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                                output.write(buffer, 0, bytesRead);
                            }
                        } finally {
                            output.close();
                        }
                    } finally {
                        input.close();
                    }

                }
                else if(apiRequest == GET_MY_TWEETS){

                }
                publishProgress("success", text);
            }
            else{
                publishProgress("failedmessage", "Response code is " + httpResponse.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
            publishProgress("failedmessage", "IOException");
        } catch (JSONException e) {
            e.printStackTrace();
            publishProgress("failedmessage", "JSONException");
        }
        return null;
    }

    private HttpUriRequest prepareHttpRequest(int apiRequest, String[] params) {
        HttpUriRequest httpUriRequest = null;
        Log.d("tasks", "last visited " + apiRequest);
        try {
            if (apiRequest == POST_TWEET) {
                httpUriRequest = new HttpPost("http://halilibo.com/twitpic/");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("message", params[0]));
                ((HttpPost) httpUriRequest).setEntity(new UrlEncodedFormEntity(nameValuePairs));

            } else if (apiRequest == GET_MY_TWEETS) {
                httpUriRequest = new HttpGet("http://halilibo.com/twitpic/tweets/" + URLDecoder.decode(params[0], "utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return httpUriRequest;
    }

    public static void postTweet(Context con, ApiListener listener, String message) {
        ApiTask task = new ApiTask(con, listener);
        task.setApiRequest(POST_TWEET);
        task.setParams(message);
        task.run();
    }

    public static void getMyTweets(final Context con, final ApiListener listener, String uid) {
        ApiTask task = new ApiTask(con, listener);
        task.setApiRequest(GET_MY_TWEETS);
        task.setParams(uid);
        task.run();
    }

}