package com.example.shoppingapp.database;

import android.database.Cursor;
import android.util.Log;

import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.shoppingapp.utils.SeatUtils;

import java.util.HashMap;
import java.util.Map;

public final class DatabaseSeeder {

    private static final String TAG = "DatabaseSeeder";
    private static final int DEFAULT_TOTAL_SEATS = 20;

    private DatabaseSeeder() {
    }

    public static void seedOnCreate(SupportSQLiteDatabase database) {
        database.beginTransaction();
        try {
            insertDefaultUsers(database);
            insertDefaultMovies(database);
            insertDefaultTheaters(database);
            insertDefaultShowtimes(database);
            database.setTransactionSuccessful();
            logCurrentCounts(database, "seedOnCreate");
        } finally {
            database.endTransaction();
        }
    }

    public static void ensureSeedData(SupportSQLiteDatabase database) {
        database.beginTransaction();
        try {
            if (countRows(database, "Users") < 2) {
                insertDefaultUsers(database);
                Log.d(TAG, "Inserted default users because table Users was missing seed data.");
            }
            if (countRows(database, "Movies") < 4) {
                insertDefaultMovies(database);
                Log.d(TAG, "Inserted default movies because table Movies was missing seed data.");
            }
            if (countRows(database, "Theaters") < 3) {
                insertDefaultTheaters(database);
                Log.d(TAG, "Inserted default theaters because table Theaters was missing seed data.");
            }
            if (countRows(database, "Showtimes") < 6) {
                insertDefaultShowtimes(database);
                Log.d(TAG, "Inserted default showtimes because table Showtimes was missing seed data.");
            }
            normalizeShowtimeSeatCounts(database);
            database.setTransactionSuccessful();
            logCurrentCounts(database, "ensureSeedData");
        } finally {
            database.endTransaction();
        }
    }

    private static void insertDefaultUsers(SupportSQLiteDatabase database) {
        database.execSQL("INSERT OR IGNORE INTO Users (userId, username, password, fullName, role) VALUES " +
                "(1, 'user1', '123456', 'Nguyễn Văn A', 'customer')," +
                "(2, 'admin', '123456', 'Quản trị viên', 'admin')");
    }

    private static void insertDefaultMovies(SupportSQLiteDatabase database) {
        database.execSQL("INSERT OR IGNORE INTO Movies (movieId, title, genre, duration, description, posterRes, isActive) VALUES " +
                "(1, 'Avengers: Endgame', 'Action', 181, 'Cuộc chiến cuối cùng của nhóm siêu anh hùng chống lại Thanos', 'product_phone', 1)," +
                "(2, 'Interstellar', 'Sci-Fi', 169, 'Một nhóm phi hành gia du hành qua một lỗ sâu để cứu nhân loại', 'product_laptop', 1)," +
                "(3, 'Inception', 'Sci-Fi', 148, 'Một thợ trộm chuyên cuỗm những bí mật từ giấc mơ của con người', 'product_tablet', 1)," +
                "(4, 'Dune', 'Sci-Fi', 166, 'Hành trình của một người lính trẻ để bảo vệ hành tinh Arrakis', 'product_audio', 1)");
    }

    private static void insertDefaultTheaters(SupportSQLiteDatabase database) {
        database.execSQL("INSERT OR IGNORE INTO Theaters (theaterId, name, address, description) VALUES " +
                "(1, 'CGV Vincom', '72 Lê Thánh Tông, Hà Nội', 'Rạp chiếu phim hiện đại với tiện nghi tốt nhất')," +
                "(2, 'Lotte Cinema', '54 Liễu Giai, Hà Nội', 'Rạp chiếu phim cao cấp với âm thanh surround')," +
                "(3, 'Galaxy Cinema', '87 Láng Hạ, Hà Nội', 'Rạp chiếu phim gia đình thân thiện')");
    }

    private static void insertDefaultShowtimes(SupportSQLiteDatabase database) {
        database.execSQL("INSERT OR IGNORE INTO Showtimes (showtimeId, movieId, theaterId, showDate, showTime, roomName, totalSeats, availableSeats) VALUES " +
                "(1, 1, 1, '2026-03-28', '14:00', 'Hall 1', 20, 20)," +
                "(2, 1, 1, '2026-03-28', '17:00', 'Hall 2', 20, 20)," +
                "(3, 2, 2, '2026-03-28', '19:00', 'Hall 1', 20, 20)," +
                "(4, 3, 3, '2026-03-28', '20:00', 'Hall 1', 20, 20)," +
                "(5, 4, 1, '2026-03-29', '15:00', 'Hall 3', 20, 20)," +
                "(6, 2, 2, '2026-03-29', '18:00', 'Hall 2', 20, 20)," +
                "(7, 1, 3, '2026-03-29', '21:00', 'Hall 2', 20, 20)," +
                "(8, 3, 1, '2026-03-30', '16:00', 'Hall 1', 20, 20)");
    }

    private static void insertDefaultCategories(SupportSQLiteDatabase database) {
        // Deprecated - no longer used for Movie Ticket App
    }

    private static void insertDefaultProducts(SupportSQLiteDatabase database) {
        // Deprecated - no longer used for Movie Ticket App
    }

    private static int countRows(SupportSQLiteDatabase database, String tableName) {
        Cursor cursor = database.query("SELECT COUNT(*) FROM " + tableName);
        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return 0;
        } finally {
            cursor.close();
        }
    }

    private static void normalizeShowtimeSeatCounts(SupportSQLiteDatabase database) {
        Map<Integer, Integer> validBookedSeatCounts = new HashMap<>();
        Cursor ticketCursor = database.query("SELECT showtimeId, seatNumber FROM Tickets");
        try {
            int showtimeIdIndex = ticketCursor.getColumnIndexOrThrow("showtimeId");
            int seatNumberIndex = ticketCursor.getColumnIndexOrThrow("seatNumber");
            while (ticketCursor.moveToNext()) {
                int showtimeId = ticketCursor.getInt(showtimeIdIndex);
                String seatNumber = ticketCursor.getString(seatNumberIndex);
                if (!SeatUtils.isSeatNumberValid(seatNumber, DEFAULT_TOTAL_SEATS)) {
                    continue;
                }
                Integer currentCount = validBookedSeatCounts.get(showtimeId);
                validBookedSeatCounts.put(showtimeId, currentCount == null ? 1 : currentCount + 1);
            }
        } finally {
            ticketCursor.close();
        }

        Cursor showtimeCursor = database.query("SELECT showtimeId FROM Showtimes");
        try {
            int showtimeIdIndex = showtimeCursor.getColumnIndexOrThrow("showtimeId");
            while (showtimeCursor.moveToNext()) {
                int showtimeId = showtimeCursor.getInt(showtimeIdIndex);
                int bookedSeats = Math.min(validBookedSeatCounts.getOrDefault(showtimeId, 0), DEFAULT_TOTAL_SEATS);
                int availableSeats = DEFAULT_TOTAL_SEATS - bookedSeats;
                database.execSQL(
                        "UPDATE Showtimes SET totalSeats = ?, availableSeats = ? WHERE showtimeId = ?",
                        new Object[]{DEFAULT_TOTAL_SEATS, availableSeats, showtimeId}
                );
            }
        } finally {
            showtimeCursor.close();
        }
    }

    private static void logCurrentCounts(SupportSQLiteDatabase database, String source) {
        Log.d(
                TAG,
                source + " -> users=" + countRows(database, "Users")
                        + ", movies=" + countRows(database, "Movies")
                        + ", theaters=" + countRows(database, "Theaters")
                        + ", showtimes=" + countRows(database, "Showtimes")
                        + ", tickets=" + countRows(database, "Tickets")
        );
    }
}
