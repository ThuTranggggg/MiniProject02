package com.example.shoppingapp.model;

import androidx.annotation.NonNull;

public class ShowtimeDisplayItem {

    public int showtimeId;
    public int movieId;
    public int theaterId;
    @NonNull
    public String movieTitle;
    @NonNull
    public String genre;
    public int duration;
    @NonNull
    public String movieDescription;
    @NonNull
    public String posterRes;
    @NonNull
    public String theaterName;
    @NonNull
    public String theaterAddress;
    @NonNull
    public String theaterDescription;
    @NonNull
    public String showDate;
    @NonNull
    public String showTime;
    @NonNull
    public String roomName;
    public int totalSeats;
    public int availableSeats;

    public int getShowtimeId() {
        return showtimeId;
    }

    public int getMovieId() {
        return movieId;
    }

    public int getTheaterId() {
        return theaterId;
    }

    @NonNull
    public String getMovieTitle() {
        return movieTitle;
    }

    @NonNull
    public String getGenre() {
        return genre;
    }

    public int getDuration() {
        return duration;
    }

    @NonNull
    public String getMovieDescription() {
        return movieDescription;
    }

    @NonNull
    public String getPosterRes() {
        return posterRes;
    }

    @NonNull
    public String getTheaterName() {
        return theaterName;
    }

    @NonNull
    public String getTheaterAddress() {
        return theaterAddress;
    }

    @NonNull
    public String getTheaterDescription() {
        return theaterDescription;
    }

    @NonNull
    public String getShowDate() {
        return showDate;
    }

    @NonNull
    public String getShowTime() {
        return showTime;
    }

    @NonNull
    public String getRoomName() {
        return roomName;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }
}
