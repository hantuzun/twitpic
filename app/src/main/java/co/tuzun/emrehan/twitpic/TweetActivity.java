package co.tuzun.emrehan.twitpic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class TweetActivity extends ActionBarActivity {


    private Button tweetpicButton;
    private EditText editText;
    private ProgressDialog generatingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        editText = (EditText) findViewById(R.id.tweetpic_text);
        tweetpicButton = (Button) findViewById(R.id.tweetpic_button);
        generatingDialog = new ProgressDialog(this);

        tweetpicButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "TweetPic this: " + editText.getText(),
                        Toast.LENGTH_SHORT).show();

                // Create generating dialog
                generatingDialog.setMessage("Generating TweetPic Image...");
                generatingDialog.setIndeterminate(true);
                generatingDialog.setCancelable(false);
                generatingDialog.show();

                // Hide keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
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

