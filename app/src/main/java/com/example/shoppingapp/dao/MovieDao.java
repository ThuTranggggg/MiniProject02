package com.example.shoppingapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shoppingapp.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert
    long insert(Movie movie);

    @Update
    void update(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("SELECT * FROM Movies WHERE movieId = :movieId")
    Movie getMovieById(int movieId);

    @Query("SELECT * FROM Movies WHERE isActive = 1 ORDER BY title ASC")
    List<Movie> getAllActiveMovies();

    @Query("SELECT * FROM Movies ORDER BY title ASC")
    List<Movie> getAllMovies();

    @Query("SELECT * FROM Movies WHERE genre LIKE :genre ORDER BY title ASC")
    List<Movie> getMoviesByGenre(String genre);

    @Query("SELECT COUNT(*) FROM Movies")
    int getCount();
}
