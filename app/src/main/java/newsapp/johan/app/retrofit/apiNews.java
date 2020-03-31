package newsapp.johan.app.retrofit;

import newsapp.johan.app.models.News_Base;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface apiNews {
    @GET("v2/top-headlines")
    Call<News_Base> listNews(@Query("country") String country, @Query("apiKey") String apiKey);
}
