package com.example.shoppingapp.model;

import androidx.annotation.NonNull;

public class TicketDetail {

    public int ticketId;
    public int userId;
    public int showtimeId;
    @NonNull
    public String movieTitle;
    @NonNull
    public String posterRes;
    @NonNull
    public String theaterName;
    @NonNull
    public String theaterAddress;
    @NonNull
    public String showDate;
    @NonNull
    public String showTime;
    @NonNull
    public String roomName;
    @NonNull
    public String seatNumber;
    public long bookingTimestamp;

    public int getTicketId() {
        return ticketId;
    }

    public int getUserId() {
        return userId;
    }

    public int getShowtimeId() {
        return showtimeId;
    }

    @NonNull
    public String getMovieTitle() {
        return movieTitle;
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

    @NonNull
    public String getSeatNumber() {
        return seatNumber;
    }

    public long getBookingTimestamp() {
        return bookingTimestamp;
    }
}
