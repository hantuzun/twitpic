package co.tuzun.emrehan.twitpic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import co.tuzun.emrehan.twitpic.ApiUtils.ApiListener;
import co.tuzun.emrehan.twitpic.ApiUtils.ApiTask;


public class TweetActivity extends ActionBarActivity {


    private Button tweetpicButton;
    private EditText editText;
    private ProgressDialog generatingDialog;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        editText = (EditText) findViewById(R.id.tweetpic_text);
        tweetpicButton = (Button) findViewById(R.id.tweetpic_button);
        generatingDialog = new ProgressDialog(this);

        tweetpicButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create generating dialog
                generatingDialog.setMessage("Generating TweetPic Image...");
                generatingDialog.setIndeterminate(true);
                generatingDialog.setCancelable(false);
                generatingDialog.show();

                // Hide keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(
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

                            intent = new TweetComposer.Builder(TweetActivity.this)
                                                            .image(myImageUri)
                                                            .createIntent();

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            startActivity( new Intent(getApplicationContext(), SuccessActivity.class) );
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
}

