package co.tuzun.emrehan.hugetwit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class TimelineActivity extends AppCompatActivity {


    SwipeRefreshLayout refreshLayout;
    FloatingActionButton fab;
    ListView tweetListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(getString(R.string.your_timeline));

        tweetListView = (ListView) findViewById(R.id.tweet_listview);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TweetActivity.class);
                startActivity(intent);
            }
        });

        createList(true);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createList(false);
                refreshLayout.setRefreshing(false);
            }
        });

    }

    protected void createList(boolean flag) {
        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(App.getTwitterSession().getUserName())
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(this, userTimeline);
        if(flag)
            tweetListView.setAdapter(adapter);
        fab.invalidate();
    }
}