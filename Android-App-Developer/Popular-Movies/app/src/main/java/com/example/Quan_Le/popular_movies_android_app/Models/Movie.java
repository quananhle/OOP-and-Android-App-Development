package com.example.Quan_Le.popular_movies_android_app.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie implements Parcelable {

    @SerializedName("poster_path")
    private String poster;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String release;
    @SerializedName("genre_name")
    private String  genre_name;
    @SerializedName("genre_ids")
    private List<Integer> genreIDs;
    @SerializedName("id")
    private Integer id;
    @SerializedName("original_title")
    private String oriTitle;
    @SerializedName("original_language")
    private String language;
    @SerializedName("title")
    private String title;
    @SerializedName("backdrop_path")
    private String backdrop;
    @SerializedName("popularity")
    private Double popularity;
    @SerializedName("vote_count")
    private Integer voteCount;
    @SerializedName("video")
    private Boolean video;
    @SerializedName("vote_average")
    private Double ratings;

    public Movie(String poster, boolean adult, String overview, String release, List<Integer> genreIDs, Integer id,
                 String oriTitle, String language, String title, String backdrop, Double popularity,
                 Integer voteCount, Boolean video, Double voteAverage) {
        this.poster = poster;
        this.adult = adult;
        this.overview = overview;
        this.release = release;
        this.genreIDs = genreIDs;
        this.id = id;
        this.oriTitle = oriTitle;
        this.language = language;
        this.title = title;
        this.backdrop = backdrop;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.ratings = voteAverage;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String posterPath) {
        this.poster = posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String releaseDate) {
        this.release = releaseDate;
    }

    public List<Integer> getGenreIDs() {
        return genreIDs;
    }

    public void setGenreIDs(List<Integer> genreIds) {
        this.genreIDs = genreIds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return oriTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.oriTitle = originalTitle;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String originalLanguage) {
        this.language = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdropPath) {
        this.backdrop = backdropPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Double getRatings() {
        return ratings;
    }

    public void setRatings(Double voteAverage) {
        this.ratings = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster);
        dest.writeString(overview);
        dest.writeString(release);
        dest.writeString(oriTitle);
        dest.writeString(language);
        dest.writeString(title);
        dest.writeDouble(ratings);
        dest.writeString(backdrop);
    }

    private Movie(Parcel in){
        this.poster = in.readString();
        this.overview = in.readString();
        this.release = in.readString();
        this.oriTitle = in.readString();
        this.language = in.readString();
        this.title = in.readString();
        this.ratings = in.readDouble();
        this.backdrop = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

