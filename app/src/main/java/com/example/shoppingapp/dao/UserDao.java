package com.example.shoppingapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.shoppingapp.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    long insert(User user);

    @Query("SELECT * FROM Users WHERE username = :username AND password = :password LIMIT 1")
    User getUserByUsernameAndPassword(String username, String password);

    @Query("SELECT * FROM Users WHERE username = :username LIMIT 1")
    User getUserByUsername(String username);

    @Query("SELECT * FROM Users WHERE userId = :userId LIMIT 1")
    User getUserById(int userId);

    @Query("SELECT * FROM Users ORDER BY userId ASC")
    List<User> getAllUsers();
}
