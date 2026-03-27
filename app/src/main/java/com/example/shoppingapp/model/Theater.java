package com.example.shoppingapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Theaters")
public class Theater {

    @PrimaryKey(autoGenerate = true)
    private int theaterId;

    @NonNull
    private String name;

    @NonNull
    private String address;

    @NonNull
    private String description;

    public Theater(@NonNull String name, @NonNull String address, @NonNull String description) {
        this.name = name;
        this.address = address;
        this.description = description;
    }

    // Getters and Setters
    public int getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(int theaterId) {
        this.theaterId = theaterId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }
}
