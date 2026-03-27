package com.example.shoppingapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shoppingapp.model.Theater;

import java.util.List;

@Dao
public interface TheaterDao {

    @Insert
    long insert(Theater theater);

    @Update
    void update(Theater theater);

    @Delete
    void delete(Theater theater);

    @Query("SELECT * FROM Theaters WHERE theaterId = :theaterId")
    Theater getTheaterById(int theaterId);

    @Query("SELECT * FROM Theaters ORDER BY name ASC")
    List<Theater> getAllTheaters();

    @Query("SELECT COUNT(*) FROM Theaters")
    int getCount();
}
