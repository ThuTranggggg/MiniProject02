package com.example.shoppingapp.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.example.shoppingapp.dao.MovieDao;
import com.example.shoppingapp.dao.ShowtimeDao;
import com.example.shoppingapp.dao.TheaterDao;
import com.example.shoppingapp.dao.TicketDao;
import com.example.shoppingapp.dao.UserDao;
import com.example.shoppingapp.database.AppDatabase;
import com.example.shoppingapp.model.Movie;
import com.example.shoppingapp.model.Showtime;
import com.example.shoppingapp.model.ShowtimeDisplayItem;
import com.example.shoppingapp.model.Theater;
import com.example.shoppingapp.model.Ticket;
import com.example.shoppingapp.model.TicketDetail;
import com.example.shoppingapp.model.User;
import com.example.shoppingapp.utils.Constants;
import com.example.shoppingapp.utils.SeatUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShoppingRepository {

    private static final String TAG = "ShoppingRepository";

    public interface DataCallback<T> {
        void onSuccess(T data);

        void onError(String message);
    }

    private static volatile ShoppingRepository INSTANCE;

    private final AppDatabase appDatabase;
    private final UserDao userDao;
    private final MovieDao movieDao;
    private final TheaterDao theaterDao;
    private final ShowtimeDao showtimeDao;
    private final TicketDao ticketDao;
    private final ExecutorService executorService;
    private final Handler mainHandler;

    private ShoppingRepository(Context context) {
        appDatabase = AppDatabase.getInstance(context);
        userDao = appDatabase.userDao();
        movieDao = appDatabase.movieDao();
        theaterDao = appDatabase.theaterDao();
        showtimeDao = appDatabase.showtimeDao();
        ticketDao = appDatabase.ticketDao();
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public static ShoppingRepository getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ShoppingRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ShoppingRepository(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    public void login(String username, String password, DataCallback<User> callback) {
        executeDataTask(callback, "login", () -> {
            String normalizedUsername = username == null ? "" : username.trim();
            String normalizedPassword = password == null ? "" : password.trim();
            User user = userDao.getUserByUsernameAndPassword(normalizedUsername, normalizedPassword);
            if (user == null) {
                postError(callback, "Tài khoản hoặc mật khẩu không đúng.");
                return;
            }
            Log.d(TAG, "login -> userId=" + user.getUserId() + ", username=" + user.getUsername());
            postSuccess(callback, user);
        });
    }

    public void getActiveMovies(DataCallback<List<Movie>> callback) {
        executeDataTask(callback, "getActiveMovies", () -> postSuccess(callback, movieDao.getAllActiveMovies()));
    }

    public void getTheaters(DataCallback<List<Theater>> callback) {
        executeDataTask(callback, "getTheaters", () -> postSuccess(callback, theaterDao.getAllTheaters()));
    }

    public void getShowtimes(Integer movieId, Integer theaterId, DataCallback<List<ShowtimeDisplayItem>> callback) {
        executeDataTask(callback, "getShowtimes", () -> {
            List<ShowtimeDisplayItem> items;
            if (movieId != null && movieId != Constants.INVALID_ID) {
                items = showtimeDao.getShowtimeDisplayItemsByMovie(movieId);
            } else if (theaterId != null && theaterId != Constants.INVALID_ID) {
                items = showtimeDao.getShowtimeDisplayItemsByTheater(theaterId);
            } else {
                items = showtimeDao.getShowtimeDisplayItems();
            }
            postSuccess(callback, items);
        });
    }

    public void getShowtimeById(int showtimeId, DataCallback<ShowtimeDisplayItem> callback) {
        executeDataTask(callback, "getShowtimeById", () -> {
            ShowtimeDisplayItem item = showtimeDao.getShowtimeDisplayItemById(showtimeId);
            if (item == null) {
                postError(callback, "Không tìm thấy lịch chiếu.");
                return;
            }
            postSuccess(callback, item);
        });
    }

    public void getBookedSeatNumbers(int showtimeId, DataCallback<List<String>> callback) {
        executeDataTask(callback, "getBookedSeatNumbers", () -> {
            Showtime showtime = showtimeDao.getShowtimeById(showtimeId);
            if (showtime == null) {
                postError(callback, "Không tìm thấy lịch chiếu.");
                return;
            }

            List<String> bookedSeats = ticketDao.getBookedSeatNumbersByShowtime(showtimeId);
            postSuccess(callback, bookedSeats);
        });
    }

    public void bookTicket(int userId, int showtimeId, String seatNumber, DataCallback<TicketDetail> callback) {
        executeDataTask(callback, "bookTicket", () -> {
            if (!isValidUser(userId)) {
                postError(callback, "Phiên đăng nhập không hợp lệ. Vui lòng đăng nhập lại.");
                return;
            }

            String normalizedSeat = SeatUtils.normalizeSeatNumber(seatNumber);
            if (TextUtils.isEmpty(normalizedSeat)) {
                postError(callback, "Vui lòng nhập số ghế.");
                return;
            }

            final int[] createdTicketId = {Constants.INVALID_ID};
            final String[] errorMessage = {null};

            try {
                appDatabase.runInTransaction(() -> {
                    Showtime showtime = showtimeDao.getShowtimeById(showtimeId);
                    if (showtime == null) {
                        errorMessage[0] = "Không tìm thấy lịch chiếu.";
                        return;
                    }

                    if (showtime.getAvailableSeats() <= 0) {
                        errorMessage[0] = "Lịch chiếu này đã hết chỗ.";
                        return;
                    }

                    if (!SeatUtils.isSeatNumberValid(normalizedSeat, showtime.getTotalSeats())) {
                        errorMessage[0] = "Ghế " + normalizedSeat + " không tồn tại trong phòng chiếu này.";
                        return;
                    }

                    if (ticketDao.countTicketForSeat(showtimeId, normalizedSeat) > 0) {
                        errorMessage[0] = "Ghế " + normalizedSeat + " đã được đặt.";
                        return;
                    }

                    int updatedRows = showtimeDao.decreaseAvailableSeats(showtimeId);
                    if (updatedRows <= 0) {
                        errorMessage[0] = "Không còn ghế trống để đặt.";
                        return;
                    }

                    Ticket ticket = new Ticket(userId, showtimeId, normalizedSeat, System.currentTimeMillis());
                    createdTicketId[0] = (int) ticketDao.insert(ticket);
                });
            } catch (Exception exception) {
                Log.e(TAG, "bookTicket failed", exception);
                postError(callback, "Không thể đặt vé. Vui lòng thử lại.");
                return;
            }

            if (!TextUtils.isEmpty(errorMessage[0])) {
                postError(callback, errorMessage[0]);
                return;
            }

            TicketDetail ticketDetail = ticketDao.getTicketDetailById(createdTicketId[0]);
            if (ticketDetail == null) {
                postError(callback, "Đặt vé thành công nhưng không thể tải thông tin vé.");
                return;
            }
            postSuccess(callback, ticketDetail);
        });
    }

    public void getTicketsByUser(int userId, DataCallback<List<TicketDetail>> callback) {
        executeDataTask(callback, "getTicketsByUser", () -> {
            if (!isValidUser(userId)) {
                postError(callback, "Phiên đăng nhập không hợp lệ. Vui lòng đăng nhập lại.");
                return;
            }
            postSuccess(callback, ticketDao.getTicketDetailsByUser(userId));
        });
    }

    private boolean isValidUser(int userId) {
        return userId != Constants.INVALID_ID && userDao.getUserById(userId) != null;
    }

    private <T> void executeDataTask(DataCallback<T> callback, String action, Runnable task) {
        executorService.execute(() -> {
            try {
                task.run();
            } catch (Exception exception) {
                Log.e(TAG, action + " failed", exception);
                postError(callback, "Không thể tải dữ liệu. Vui lòng thử lại.");
            }
        });
    }

    private <T> void postSuccess(DataCallback<T> callback, T data) {
        mainHandler.post(() -> callback.onSuccess(data));
    }

    private <T> void postError(DataCallback<T> callback, String message) {
        Log.d(TAG, "postError -> " + message);
        mainHandler.post(() -> callback.onError(message));
    }
}
