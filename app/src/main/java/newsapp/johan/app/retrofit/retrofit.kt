package newsapp.johan.app.retrofit

import com.squareup.moshi.Moshi
import newsapp.johan.app.models.News_Base
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


public class retrofit {
    val retrofit: Retrofit

    constructor() {
        retrofit = createRetrofit()
    }

    fun createRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
// set your desired log level
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
// add your other interceptors …

// add logging as last interceptor
        // add your other interceptors …
// add logging as last interceptor
        httpClient.addInterceptor(logging) // <-- this is the important line!


        val moshi = Moshi.Builder().build()
        val retrofit = Retrofit.Builder().baseUrl("http://newsapi.org")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(httpClient.build())
            .build()
        return retrofit
    }


    fun callNews(country: String, apiKey: String): Call<News_Base> {
        val service: apiNews = retrofit.create<apiNews>(apiNews::class.java)
        val news: Call<News_Base> = service.listNews(country, apiKey)
        return news
    }

}
