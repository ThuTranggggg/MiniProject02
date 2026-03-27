package com.example.shoppingapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Tickets",
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "userId",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Showtime.class,
                        parentColumns = "showtimeId",
                        childColumns = "showtimeId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("userId"),
                @Index("showtimeId"),
                @Index(value = {"showtimeId", "seatNumber"}, unique = true)
        }
)
public class Ticket {

    @PrimaryKey(autoGenerate = true)
    private int ticketId;

    private int userId;

    private int showtimeId;

    @NonNull
    private String seatNumber; // e.g., A1, A2, B3, etc.

    private long bookingTimestamp;

    public Ticket(int userId, int showtimeId, @NonNull String seatNumber, long bookingTimestamp) {
        this.userId = userId;
        this.showtimeId = showtimeId;
        this.seatNumber = seatNumber;
        this.bookingTimestamp = bookingTimestamp;
    }

    // Getters and Setters
    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(int showtimeId) {
        this.showtimeId = showtimeId;
    }

    @NonNull
    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(@NonNull String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public long getBookingTimestamp() {
        return bookingTimestamp;
    }

    public void setBookingTimestamp(long bookingTimestamp) {
        this.bookingTimestamp = bookingTimestamp;
    }
}
