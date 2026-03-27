package com.example.shoppingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingapp.R;
import com.example.shoppingapp.model.ShowtimeDisplayItem;
import com.example.shoppingapp.model.TicketDetail;
import com.example.shoppingapp.repository.ShoppingRepository;
import com.example.shoppingapp.session.SessionManager;
import com.example.shoppingapp.utils.Constants;
import com.example.shoppingapp.utils.DateTimeUtils;
import com.example.shoppingapp.utils.ImageUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class BookTicketActivity extends AppCompatActivity {

    private ShoppingRepository repository;
    private SessionManager sessionManager;
    private int showtimeId;
    private ImageView ivPoster;
    private TextView tvMovieTitle;
    private TextView tvMovieMeta;
    private TextView tvTheaterInfo;
    private TextView tvShowtimeInfo;
    private TextView tvSeatInfo;
    private TextInputLayout tilSeatNumber;
    private TextInputEditText etSeatNumber;
    private MaterialButton btnConfirmBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ticket);

        repository = ShoppingRepository.getInstance(this);
        sessionManager = new SessionManager(this);
        showtimeId = getIntent().getIntExtra(Constants.EXTRA_SHOWTIME_ID, Constants.INVALID_ID);

        if (showtimeId == Constants.INVALID_ID) {
            Toast.makeText(this, R.string.showtime_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ImageButton btnBack = findViewById(R.id.btnBackBookTicket);
        ivPoster = findViewById(R.id.ivBookPoster);
        tvMovieTitle = findViewById(R.id.tvBookMovieTitle);
        tvMovieMeta = findViewById(R.id.tvBookMovieMeta);
        tvTheaterInfo = findViewById(R.id.tvBookTheaterInfo);
        tvShowtimeInfo = findViewById(R.id.tvBookShowtimeInfo);
        tvSeatInfo = findViewById(R.id.tvBookSeatInfo);
        tilSeatNumber = findViewById(R.id.tilSeatNumber);
        etSeatNumber = findViewById(R.id.etSeatNumber);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);

        btnBack.setOnClickListener(v -> finish());
        btnConfirmBooking.setOnClickListener(v -> confirmBooking());

        loadShowtime();
    }

    private void loadShowtime() {
        repository.getShowtimeById(showtimeId, new ShoppingRepository.DataCallback<ShowtimeDisplayItem>() {
            @Override
            public void onSuccess(ShowtimeDisplayItem data) {
                bindShowtime(data);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(BookTicketActivity.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void bindShowtime(ShowtimeDisplayItem item) {
        ivPoster.setImageResource(ImageUtils.getDrawableResId(this, item.getPosterRes(), R.drawable.product_laptop));
        tvMovieTitle.setText(item.getMovieTitle());
        tvMovieMeta.setText(getString(R.string.movie_meta_format, item.getGenre(), item.getDuration()));
        tvTheaterInfo.setText(getString(R.string.book_theater_info, item.getTheaterName(), item.getTheaterAddress()));
        tvShowtimeInfo.setText(getString(
                R.string.book_showtime_info,
                DateTimeUtils.formatShowDateTime(item.getShowDate(), item.getShowTime()),
                item.getRoomName()
        ));
        tvSeatInfo.setText(getString(R.string.book_available_seats, item.getAvailableSeats(), item.getTotalSeats()));
    }

    private void confirmBooking() {
        tilSeatNumber.setError(null);

        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, R.string.login_required_booking, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        String seatNumber = etSeatNumber.getText() == null ? "" : etSeatNumber.getText().toString().trim();
        if (TextUtils.isEmpty(seatNumber)) {
            tilSeatNumber.setError(getString(R.string.seat_required));
            return;
        }

        btnConfirmBooking.setEnabled(false);
        repository.bookTicket(sessionManager.getCurrentUserId(), showtimeId, seatNumber, new ShoppingRepository.DataCallback<TicketDetail>() {
            @Override
            public void onSuccess(TicketDetail data) {
                btnConfirmBooking.setEnabled(true);
                Toast.makeText(BookTicketActivity.this, R.string.book_ticket_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BookTicketActivity.this, MyTicketsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String message) {
                btnConfirmBooking.setEnabled(true);
                tilSeatNumber.setError(message);
            }
        });
    }
}
