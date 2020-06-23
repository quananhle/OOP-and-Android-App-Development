package com.example.Quan_Le.popular_movies_android_app.Helper;

import com.example.Quan_Le.popular_movies_android_app.Models.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoviesInterface {
    @GET("movie/top_rated")
    Call<MovieResponse> getTopRated(@Query("api_key") String apiKey, @Query("language") String language,
                                          @Query("page") int page);
    @GET("movie/popular")
    Call<MovieResponse> getMostWatched(@Query("api_key") String apiKey, @Query("language") String language,
                                         @Query("page") int page);
}

