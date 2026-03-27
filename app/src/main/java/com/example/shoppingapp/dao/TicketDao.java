package com.example.shoppingapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shoppingapp.model.TicketDetail;
import com.example.shoppingapp.model.Ticket;

import java.util.List;

@Dao
public interface TicketDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(Ticket ticket);

    @Update
    void update(Ticket ticket);

    @Delete
    void delete(Ticket ticket);

    @Query("SELECT * FROM Tickets WHERE ticketId = :ticketId")
    Ticket getTicketById(int ticketId);

    @Query("SELECT * FROM Tickets WHERE userId = :userId ORDER BY bookingTimestamp DESC")
    List<Ticket> getTicketsByUser(int userId);

    @Query("SELECT * FROM Tickets WHERE showtimeId = :showtimeId ORDER BY seatNumber ASC")
    List<Ticket> getTicketsByShowtime(int showtimeId);

    @Query("SELECT seatNumber FROM Tickets WHERE showtimeId = :showtimeId ORDER BY seatNumber ASC")
    List<String> getBookedSeatNumbersByShowtime(int showtimeId);

    @Query("SELECT * FROM Tickets WHERE showtimeId = :showtimeId AND seatNumber = :seatNumber")
    Ticket getTicketBySeatInShowtime(int showtimeId, String seatNumber);

    @Query("SELECT COUNT(*) FROM Tickets WHERE showtimeId = :showtimeId AND seatNumber = :seatNumber")
    int countTicketForSeat(int showtimeId, String seatNumber);

    @Query("SELECT COUNT(*) FROM Tickets WHERE userId = :userId")
    int getCountByUser(int userId);

    @Query("SELECT COUNT(*) FROM Tickets")
    int getCount();

    @Query("SELECT tk.ticketId AS ticketId, " +
            "tk.userId AS userId, " +
            "tk.showtimeId AS showtimeId, " +
            "m.title AS movieTitle, " +
            "m.posterRes AS posterRes, " +
            "th.name AS theaterName, " +
            "th.address AS theaterAddress, " +
            "st.showDate AS showDate, " +
            "st.showTime AS showTime, " +
            "st.roomName AS roomName, " +
            "tk.seatNumber AS seatNumber, " +
            "tk.bookingTimestamp AS bookingTimestamp " +
            "FROM Tickets tk " +
            "INNER JOIN Showtimes st ON st.showtimeId = tk.showtimeId " +
            "INNER JOIN Movies m ON m.movieId = st.movieId " +
            "INNER JOIN Theaters th ON th.theaterId = st.theaterId " +
            "WHERE tk.userId = :userId " +
            "ORDER BY tk.bookingTimestamp DESC")
    List<TicketDetail> getTicketDetailsByUser(int userId);

    @Query("SELECT tk.ticketId AS ticketId, " +
            "tk.userId AS userId, " +
            "tk.showtimeId AS showtimeId, " +
            "m.title AS movieTitle, " +
            "m.posterRes AS posterRes, " +
            "th.name AS theaterName, " +
            "th.address AS theaterAddress, " +
            "st.showDate AS showDate, " +
            "st.showTime AS showTime, " +
            "st.roomName AS roomName, " +
            "tk.seatNumber AS seatNumber, " +
            "tk.bookingTimestamp AS bookingTimestamp " +
            "FROM Tickets tk " +
            "INNER JOIN Showtimes st ON st.showtimeId = tk.showtimeId " +
            "INNER JOIN Movies m ON m.movieId = st.movieId " +
            "INNER JOIN Theaters th ON th.theaterId = st.theaterId " +
            "WHERE tk.ticketId = :ticketId " +
            "LIMIT 1")
    TicketDetail getTicketDetailById(int ticketId);
}
