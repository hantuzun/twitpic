package co.tuzun.emrehan.hugetwit;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.io.IOException;

public class AuthActivity extends ActionBarActivity implements SurfaceHolder.Callback {

    private TwitterLoginButton loginButton;
    private Intent intent;
    private MediaPlayer mp = null;
    SurfaceView mSurfaceView=null;
    private SurfaceHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mSurfaceView = (SurfaceView)findViewById(R.id.surface);
        mp = new MediaPlayer();

        holder = mSurfaceView.getHolder();
        holder.setFixedSize(800, 480);
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                gotoActivity();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getApplicationContext(),
                        "Login failed, try again",
                        Toast.LENGTH_SHORT).show();
            }
        });

        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        if(session != null){
            gotoActivity();
        }
    }

    private void gotoActivity() {
        intent = new Intent(getApplicationContext(), TimelineActivity.class);
        startActivity(intent);
        finish();
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

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Uri video = Uri.parse("android.resource://co.tuzun.emrehan.twitpic/"
                + R.raw.newyork);

        try {
            mp.setDisplay(holder);
            mp.setDataSource(this, video);
            mp.prepare();
            mp.setLooping(true);
            Log.d("video", "" + video.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Get the dimensions of the video
        int videoWidth = mp.getVideoHeight();
        int videoHeight = mp.getVideoWidth();
        Log.d("video", "" + videoWidth + " " + videoHeight);


        //Start video
        mp.start();
        Log.d("video", "" + mp.isPlaying());
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
