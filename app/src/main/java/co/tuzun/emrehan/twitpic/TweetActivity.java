package co.tuzun.emrehan.twitpic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import co.tuzun.emrehan.twitpic.ApiUtils.ApiListener;
import co.tuzun.emrehan.twitpic.ApiUtils.ApiTask;
import co.tuzun.emrehan.twitpic.ApiUtils.MyTwitterApiClient;


public class TweetActivity extends ActionBarActivity {

    private static final int TWEET_COMPOSER_REQUEST_CODE = 145;

    private Button tweetpicButton;
    private EditText editText;
    private ProgressDialog generatingDialog;

    private ImageView userImage;
    private TextView userName;
    private TextView userHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        tweetpicButton = (Button) findViewById(R.id.tweetpic_button);
        generatingDialog = new ProgressDialog(this);
        getSupportActionBar().hide();

        editText = (EditText) findViewById(R.id.tweetpic_text);

        Long uid = TwitterCore.getInstance().getSessionManager().getActiveSession().getUserId();
        new MyTwitterApiClient(TwitterCore.getInstance().getSessionManager().getActiveSession()).getUsersService().show(uid, null, true,
                new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {

                        userName = (TextView) findViewById(R.id.tweetpic_userName);
                        userName.setText(result.data.screenName);

                        userHandle = (TextView) findViewById(R.id.tweetpic_userHandle);
                        userHandle.setText("@" + result.data.name);
                        
                        Log.d("twittercommunity", "user's profile url is "
                                + result.data.profileImageUrlHttps);

                        new InstallImage().execute(result.data.profileImageUrlHttps);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Log.d("twittercommunity", "exception is " + exception);
                    }
                });



        tweetpicButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create generating dialog
                generatingDialog.setMessage("Generating TweetPic Image...");
                generatingDialog.setIndeterminate(true);
                generatingDialog.setCancelable(false);
                generatingDialog.show();

                // Hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);


                ApiTask.postTweet(getApplicationContext(), new ApiListener() {
                    @Override
                    public void onSuccess(String text) {
                        try {
                            JSONObject jsonReader = new JSONObject(text);
                            String id = jsonReader.getString("id");

                            File outputFile = new File(App.outputFolder, id + ".jpg");
                            Uri myImageUri = Uri.fromFile(outputFile);
                            Log.d("imagefile", myImageUri.toString());

                            Intent intent = new TweetComposer.Builder(TweetActivity.this)
                                    .text(editText.getText().toString().substring(0, Math.min(editText.getText().toString().length(), 118)))
                                    .image(myImageUri)
                                    .createIntent();


                            startActivityForResult(intent, TWEET_COMPOSER_REQUEST_CODE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            generatingDialog.cancel();
                        }
                    }

                    @Override
                    public void onFail(String text) {
                        generatingDialog.cancel();
                    }
                }, editText.getText().toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TWEET_COMPOSER_REQUEST_CODE){
            Log.d("result", resultCode + "");
            if(resultCode == Activity.RESULT_OK){
                finishActivity(Activity.RESULT_OK);

                Intent intent = new Intent(getApplicationContext(), TimelineActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class InstallImage extends AsyncTask<String, Void, Boolean> {
        private Bitmap bitmap;
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(params[0]).getContent());
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result == true) {
                userImage = (ImageView) findViewById(R.id.tweetpic_userPicture);
                userImage.setImageBitmap(bitmap);
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}

