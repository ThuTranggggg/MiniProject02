package com.example.shoppingapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Showtimes",
        foreignKeys = {
                @ForeignKey(
                        entity = Movie.class,
                        parentColumns = "movieId",
                        childColumns = "movieId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Theater.class,
                        parentColumns = "theaterId",
                        childColumns = "theaterId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("movieId"),
                @Index("theaterId"),
                @Index(value = {"movieId", "theaterId"})
        }
)
public class Showtime {

    @PrimaryKey(autoGenerate = true)
    private int showtimeId;

    private int movieId;

    private int theaterId;

    @NonNull
    private String showDate; // format: YYYY-MM-DD

    @NonNull
    private String showTime; // format: HH:mm

    @NonNull
    private String roomName;

    private int totalSeats;

    private int availableSeats;

    public Showtime(int movieId, int theaterId, @NonNull String showDate, @NonNull String showTime,
                    @NonNull String roomName, int totalSeats, int availableSeats) {
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.showDate = showDate;
        this.showTime = showTime;
        this.roomName = roomName;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
    }

    // Getters and Setters
    public int getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(int showtimeId) {
        this.showtimeId = showtimeId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(int theaterId) {
        this.theaterId = theaterId;
    }

    @NonNull
    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(@NonNull String showDate) {
        this.showDate = showDate;
    }

    @NonNull
    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(@NonNull String showTime) {
        this.showTime = showTime;
    }

    @NonNull
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(@NonNull String roomName) {
        this.roomName = roomName;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}
