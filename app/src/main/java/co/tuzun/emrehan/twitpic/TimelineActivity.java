package co.tuzun.emrehan.twitpic;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class TimelineActivity extends ListActivity {


    SwipeRefreshLayout refreshLayout;
    FloatingActionButton sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createList();
                refreshLayout.setRefreshing(false);
            }
        });

        createList();

        sendButton = (FloatingActionButton) findViewById(R.id.tweet_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TweetActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void createList() {
        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(TwitterCore.getInstance().getSessionManager().getActiveSession().getUserName())
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(this, userTimeline);
        setListAdapter(adapter);
    }
}