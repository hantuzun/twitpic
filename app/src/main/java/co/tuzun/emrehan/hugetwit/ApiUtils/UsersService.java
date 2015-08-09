package co.tuzun.emrehan.hugetwit.ApiUtils;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.User;

import retrofit.http.GET;
import retrofit.http.Query;

public interface UsersService {

    @GET("/1.1/users/show.json")
    void show(@Query("user_id") Long userId,
              @Query("include_entities") Boolean includeEntities,
              Callback<User> cb);
}