package com.example.Quan_Le.popular_movies_android_app.Helper;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    public static Retrofit getRetrofitInstance() {

        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}