package com.example.shoppingapp.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.example.shoppingapp.utils.Constants;

public class SessionManager {

    private static final String TAG = "SessionManager";
    private final SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLoginSession(int userId, String username) {
        saveLogin(userId, username, true);
    }

    public void saveLogin(int userId, String username, boolean isLoggedIn) {
        sharedPreferences.edit()
                .putInt(Constants.KEY_USER_ID, userId)
                .putString(Constants.KEY_USERNAME, username == null ? "" : username.trim())
                .putBoolean(Constants.KEY_IS_LOGGED_IN, isLoggedIn)
                .apply();
        Log.d(TAG, "saveLogin -> userId=" + userId + ", username=" + getUsername() + ", isLoggedIn=" + isLoggedIn);
    }

    public boolean isLoggedIn() {
        boolean savedLoginState = sharedPreferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
        boolean hasValidUserId = getUserId() != Constants.INVALID_ID;
        boolean hasUsername = !TextUtils.isEmpty(getUsername());
        boolean isLoggedIn = savedLoginState && hasValidUserId && hasUsername;
        Log.d(TAG, "isLoggedIn -> " + isLoggedIn + " (saved=" + savedLoginState + ", userId=" + getUserId() + ", username=" + getUsername() + ")");
        return isLoggedIn;
    }

    public int getUserId() {
        return sharedPreferences.getInt(Constants.KEY_USER_ID, Constants.INVALID_ID);
    }

    public int getCurrentUserId() {
        return getUserId();
    }

    public String getUsername() {
        return sharedPreferences.getString(Constants.KEY_USERNAME, "");
    }

    public void clearSession() {
        sharedPreferences.edit().clear().apply();
        Log.d(TAG, "clearSession -> session removed");
    }
}
