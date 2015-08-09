package co.tuzun.emrehan.hugetwit.ApiUtils;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

public class MyTwitterApiClient extends TwitterApiClient {
    public MyTwitterApiClient(TwitterSession session) {
        super(session);
    }

    public UsersService getUsersService() {
        return getService(UsersService.class);
    }
}
