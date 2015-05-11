package co.tuzun.emrehan.twitpic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import co.tuzun.emrehan.twitpic.ApiUtils.ApiListener;
import co.tuzun.emrehan.twitpic.ApiUtils.ApiTask;

public class Auth extends ActionBarActivity {

    private TwitterLoginButton loginButton;
    private Button composeButton;
    private EditText composeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);


        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        composeButton = (Button) findViewById(R.id.composeButton);
        composeEditText = (EditText) findViewById(R.id.composeEditText);

        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                loginButton.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Welcome " + result.data.getUserName(), Toast.LENGTH_SHORT).show();
                Log.d("token", result.data.getAuthToken().token + " " + result.data.getAuthToken().secret);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getApplicationContext(),
                        "Login failed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        composeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiTask.postTweet(getApplicationContext(), new ApiListener() {
                    @Override
                    public void onSuccess(String text) {
                        TweetComposer.Builder builder = new TweetComposer.Builder(getApplicationContext())
                                .text("Sending my first tweet.")
                                .image(Uri.parse(App.outputFolder));
                        builder.show();
                    }

                    @Override
                    public void onFail(String text) {

                    }
                }, composeEditText.getText().toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_auth, menu);
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
