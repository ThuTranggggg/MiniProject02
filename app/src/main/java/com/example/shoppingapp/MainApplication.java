package com.example.shoppingapp;

import android.app.Application;

import com.example.shoppingapp.database.AppDatabase;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase.getInstance(this).getOpenHelper().getWritableDatabase();
    }
}
