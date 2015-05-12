package co.tuzun.emrehan.twitpic;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class TimelineActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);

        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(TwitterCore.getInstance().getSessionManager().getActiveSession().getUserName())
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(this, userTimeline);

        FloatingActionButton sendButton = (FloatingActionButton) findViewById(R.id.tweet_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TweetActivity.class);
                startActivity(intent);
            }
        });
        setListAdapter(adapter);
    }
}