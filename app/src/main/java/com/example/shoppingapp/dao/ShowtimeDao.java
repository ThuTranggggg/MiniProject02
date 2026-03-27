package com.example.shoppingapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shoppingapp.model.ShowtimeDisplayItem;
import com.example.shoppingapp.model.Showtime;

import java.util.List;

@Dao
public interface ShowtimeDao {

    @Insert
    long insert(Showtime showtime);

    @Update
    void update(Showtime showtime);

    @Delete
    void delete(Showtime showtime);

    @Query("SELECT * FROM Showtimes WHERE showtimeId = :showtimeId")
    Showtime getShowtimeById(int showtimeId);

    @Query("SELECT * FROM Showtimes ORDER BY showDate ASC, showTime ASC")
    List<Showtime> getAllShowtimes();

    @Query("SELECT * FROM Showtimes WHERE movieId = :movieId ORDER BY showDate ASC, showTime ASC")
    List<Showtime> getShowtimesByMovie(int movieId);

    @Query("SELECT * FROM Showtimes WHERE theaterId = :theaterId ORDER BY showDate ASC, showTime ASC")
    List<Showtime> getShowtimesByTheater(int theaterId);

    @Query("SELECT * FROM Showtimes WHERE movieId = :movieId AND theaterId = :theaterId ORDER BY showDate ASC, showTime ASC")
    List<Showtime> getShowtimesByMovieAndTheater(int movieId, int theaterId);

    @Query("SELECT COUNT(*) FROM Showtimes")
    int getCount();

    @Query("UPDATE Showtimes SET availableSeats = availableSeats - 1 WHERE showtimeId = :showtimeId AND availableSeats > 0")
    int decreaseAvailableSeats(int showtimeId);

    @Query("SELECT s.showtimeId AS showtimeId, " +
            "s.movieId AS movieId, " +
            "s.theaterId AS theaterId, " +
            "m.title AS movieTitle, " +
            "m.genre AS genre, " +
            "m.duration AS duration, " +
            "m.description AS movieDescription, " +
            "m.posterRes AS posterRes, " +
            "t.name AS theaterName, " +
            "t.address AS theaterAddress, " +
            "t.description AS theaterDescription, " +
            "s.showDate AS showDate, " +
            "s.showTime AS showTime, " +
            "s.roomName AS roomName, " +
            "s.totalSeats AS totalSeats, " +
            "s.availableSeats AS availableSeats " +
            "FROM Showtimes s " +
            "INNER JOIN Movies m ON m.movieId = s.movieId " +
            "INNER JOIN Theaters t ON t.theaterId = s.theaterId " +
            "ORDER BY s.showDate ASC, s.showTime ASC")
    List<ShowtimeDisplayItem> getShowtimeDisplayItems();

    @Query("SELECT s.showtimeId AS showtimeId, " +
            "s.movieId AS movieId, " +
            "s.theaterId AS theaterId, " +
            "m.title AS movieTitle, " +
            "m.genre AS genre, " +
            "m.duration AS duration, " +
            "m.description AS movieDescription, " +
            "m.posterRes AS posterRes, " +
            "t.name AS theaterName, " +
            "t.address AS theaterAddress, " +
            "t.description AS theaterDescription, " +
            "s.showDate AS showDate, " +
            "s.showTime AS showTime, " +
            "s.roomName AS roomName, " +
            "s.totalSeats AS totalSeats, " +
            "s.availableSeats AS availableSeats " +
            "FROM Showtimes s " +
            "INNER JOIN Movies m ON m.movieId = s.movieId " +
            "INNER JOIN Theaters t ON t.theaterId = s.theaterId " +
            "WHERE s.movieId = :movieId " +
            "ORDER BY s.showDate ASC, s.showTime ASC")
    List<ShowtimeDisplayItem> getShowtimeDisplayItemsByMovie(int movieId);

    @Query("SELECT s.showtimeId AS showtimeId, " +
            "s.movieId AS movieId, " +
            "s.theaterId AS theaterId, " +
            "m.title AS movieTitle, " +
            "m.genre AS genre, " +
            "m.duration AS duration, " +
            "m.description AS movieDescription, " +
            "m.posterRes AS posterRes, " +
            "t.name AS theaterName, " +
            "t.address AS theaterAddress, " +
            "t.description AS theaterDescription, " +
            "s.showDate AS showDate, " +
            "s.showTime AS showTime, " +
            "s.roomName AS roomName, " +
            "s.totalSeats AS totalSeats, " +
            "s.availableSeats AS availableSeats " +
            "FROM Showtimes s " +
            "INNER JOIN Movies m ON m.movieId = s.movieId " +
            "INNER JOIN Theaters t ON t.theaterId = s.theaterId " +
            "WHERE s.theaterId = :theaterId " +
            "ORDER BY s.showDate ASC, s.showTime ASC")
    List<ShowtimeDisplayItem> getShowtimeDisplayItemsByTheater(int theaterId);

    @Query("SELECT s.showtimeId AS showtimeId, " +
            "s.movieId AS movieId, " +
            "s.theaterId AS theaterId, " +
            "m.title AS movieTitle, " +
            "m.genre AS genre, " +
            "m.duration AS duration, " +
            "m.description AS movieDescription, " +
            "m.posterRes AS posterRes, " +
            "t.name AS theaterName, " +
            "t.address AS theaterAddress, " +
            "t.description AS theaterDescription, " +
            "s.showDate AS showDate, " +
            "s.showTime AS showTime, " +
            "s.roomName AS roomName, " +
            "s.totalSeats AS totalSeats, " +
            "s.availableSeats AS availableSeats " +
            "FROM Showtimes s " +
            "INNER JOIN Movies m ON m.movieId = s.movieId " +
            "INNER JOIN Theaters t ON t.theaterId = s.theaterId " +
            "WHERE s.showtimeId = :showtimeId " +
            "LIMIT 1")
    ShowtimeDisplayItem getShowtimeDisplayItemById(int showtimeId);
}
