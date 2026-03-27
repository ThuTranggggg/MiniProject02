package com.example.shoppingapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Movies")
public class Movie {

    @PrimaryKey(autoGenerate = true)
    private int movieId;

    @NonNull
    private String title;

    @NonNull
    private String genre;

    private int duration; // in minutes

    @NonNull
    private String description;

    @NonNull
    private String posterRes; // resource name for image

    private boolean isActive;

    public Movie(@NonNull String title, @NonNull String genre, int duration,
                 @NonNull String description, @NonNull String posterRes, boolean isActive) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.description = description;
        this.posterRes = posterRes;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getGenre() {
        return genre;
    }

    public void setGenre(@NonNull String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public String getPosterRes() {
        return posterRes;
    }

    public void setPosterRes(@NonNull String posterRes) {
        this.posterRes = posterRes;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
