package com.example.shoppingapp.ui;

import android.content.res.ColorStateList;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.shoppingapp.R;
import com.example.shoppingapp.model.ShowtimeDisplayItem;
import com.example.shoppingapp.model.TicketDetail;
import com.example.shoppingapp.repository.ShoppingRepository;
import com.example.shoppingapp.session.SessionManager;
import com.example.shoppingapp.utils.Constants;
import com.example.shoppingapp.utils.DateTimeUtils;
import com.example.shoppingapp.utils.ImageUtils;
import com.example.shoppingapp.utils.SeatUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookTicketActivity extends AppCompatActivity {

    private ShoppingRepository repository;
    private SessionManager sessionManager;
    private int showtimeId;
    private ShowtimeDisplayItem currentShowtime;
    private ImageView ivPoster;
    private TextView tvMovieTitle;
    private TextView tvMovieMeta;
    private TextView tvTheaterInfo;
    private TextView tvShowtimeInfo;
    private TextView tvSeatInfo;
    private TextView tvSeatSelectionHint;
    private TextInputLayout tilSeatNumber;
    private TextInputEditText etSeatNumber;
    private ChipGroup chipGroupAvailableSeats;
    private MaterialButton btnConfirmBooking;
    private final List<String> availableSeatNumbers = new ArrayList<>();

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
        tvSeatSelectionHint = findViewById(R.id.tvSeatSelectionHint);
        tilSeatNumber = findViewById(R.id.tilSeatNumber);
        etSeatNumber = findViewById(R.id.etSeatNumber);
        chipGroupAvailableSeats = findViewById(R.id.chipGroupAvailableSeats);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);

        btnBack.setOnClickListener(v -> finish());
        btnConfirmBooking.setOnClickListener(v -> confirmBooking());
        bindSeatInput();

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
        currentShowtime = item;
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
        loadSeatMap();
    }

    private void confirmBooking() {
        tilSeatNumber.setError(null);

        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, R.string.login_required_booking, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        String seatNumber = etSeatNumber.getText() == null ? "" : etSeatNumber.getText().toString();
        String normalizedSeat = SeatUtils.normalizeSeatNumber(seatNumber);
        if (TextUtils.isEmpty(normalizedSeat)) {
            tilSeatNumber.setError(getString(R.string.seat_required));
            return;
        }

        if (!availableSeatNumbers.isEmpty() && !availableSeatNumbers.contains(normalizedSeat)) {
            tilSeatNumber.setError(getString(R.string.seat_invalid, normalizedSeat));
            return;
        }

        btnConfirmBooking.setEnabled(false);
        repository.bookTicket(sessionManager.getCurrentUserId(), showtimeId, normalizedSeat, new ShoppingRepository.DataCallback<TicketDetail>() {
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
                if (currentShowtime != null) {
                    loadSeatMap();
                }
            }
        });
    }

    private void bindSeatInput() {
        etSeatNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tilSeatNumber.setError(null);
                syncSelectedSeatChip(SeatUtils.normalizeSeatNumber(editable == null ? "" : editable.toString()));
            }
        });
    }

    private void loadSeatMap() {
        tvSeatSelectionHint.setText(R.string.book_loading_seats);
        btnConfirmBooking.setEnabled(false);
        repository.getBookedSeatNumbers(showtimeId, new ShoppingRepository.DataCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> data) {
                bindSeatMap(data);
            }

            @Override
            public void onError(String message) {
                availableSeatNumbers.clear();
                chipGroupAvailableSeats.removeAllViews();
                tvSeatSelectionHint.setText(message);
                btnConfirmBooking.setEnabled(false);
                Toast.makeText(BookTicketActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindSeatMap(List<String> bookedSeats) {
        availableSeatNumbers.clear();

        Set<String> bookedSeatSet = new HashSet<>();
        if (bookedSeats != null) {
            for (String seatNumber : bookedSeats) {
                String normalizedSeat = SeatUtils.normalizeSeatNumber(seatNumber);
                if (SeatUtils.isSeatNumberValid(normalizedSeat, currentShowtime == null ? 0 : currentShowtime.getTotalSeats())) {
                    bookedSeatSet.add(normalizedSeat);
                }
            }
        }

        chipGroupAvailableSeats.removeAllViews();
        if (currentShowtime != null) {
            List<String> allSeatNumbers = SeatUtils.generateSeatNumbers(currentShowtime.getTotalSeats());
            for (String seatNumber : allSeatNumbers) {
                boolean isBooked = bookedSeatSet.contains(seatNumber);
                if (!isBooked) {
                    availableSeatNumbers.add(seatNumber);
                }
                chipGroupAvailableSeats.addView(createSeatChip(seatNumber, isBooked));
            }
        }

        if (currentShowtime != null) {
            tvSeatInfo.setText(getString(
                    R.string.book_available_seats,
                    availableSeatNumbers.size(),
                    currentShowtime.getTotalSeats()
            ));
        }

        if (availableSeatNumbers.isEmpty()) {
            tvSeatSelectionHint.setText(R.string.book_no_available_seats);
            updateSeatInput("");
            tilSeatNumber.setEnabled(false);
            etSeatNumber.setEnabled(false);
            btnConfirmBooking.setEnabled(false);
            return;
        }

        tilSeatNumber.setEnabled(true);
        etSeatNumber.setEnabled(true);
        tvSeatSelectionHint.setText(R.string.book_select_seat_hint);
        btnConfirmBooking.setEnabled(true);

        String currentSeat = SeatUtils.normalizeSeatNumber(etSeatNumber.getText() == null ? "" : etSeatNumber.getText().toString());
        if (!TextUtils.isEmpty(currentSeat) && !availableSeatNumbers.contains(currentSeat)) {
            updateSeatInput("");
        } else {
            syncSelectedSeatChip(currentSeat);
        }
    }

    private void updateSeatInput(String seatNumber) {
        String normalizedSeat = SeatUtils.normalizeSeatNumber(seatNumber);
        String currentValue = SeatUtils.normalizeSeatNumber(etSeatNumber.getText() == null ? "" : etSeatNumber.getText().toString());
        if (TextUtils.equals(currentValue, normalizedSeat)) {
            return;
        }
        etSeatNumber.setText(normalizedSeat);
        if (etSeatNumber.getText() != null) {
            etSeatNumber.setSelection(etSeatNumber.getText().length());
        }
    }

    private void syncSelectedSeatChip(String normalizedSeat) {
        if (TextUtils.isEmpty(normalizedSeat)) {
            chipGroupAvailableSeats.clearCheck();
            return;
        }

        int childCount = chipGroupAvailableSeats.getChildCount();
        for (int index = 0; index < childCount; index++) {
            android.view.View child = chipGroupAvailableSeats.getChildAt(index);
            if (!(child instanceof Chip)) {
                continue;
            }

            Chip chip = (Chip) child;
            String chipSeat = SeatUtils.normalizeSeatNumber(chip.getText() == null ? "" : chip.getText().toString());
            if (TextUtils.equals(chipSeat, normalizedSeat)) {
                chipGroupAvailableSeats.check(chip.getId());
                return;
            }
        }

        chipGroupAvailableSeats.clearCheck();
    }

    private Chip createSeatChip(String seatNumber, boolean isBooked) {
        Chip chip = new Chip(this);
        chip.setId(android.view.View.generateViewId());
        chip.setText(seatNumber);
        chip.setCheckable(!isBooked);
        chip.setClickable(!isBooked);
        chip.setCheckedIconVisible(false);
        chip.setChipStrokeWidth(1f);

        if (isBooked) {
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.badge_danger_bg)));
            chip.setChipStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.danger)));
            chip.setTextColor(ContextCompat.getColor(this, R.color.danger));
            chip.setContentDescription(getString(R.string.booked_seat_content_description, seatNumber));
            return chip;
        }

        chip.setChipBackgroundColor(new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                new int[]{
                        ContextCompat.getColor(this, R.color.primary),
                        ContextCompat.getColor(this, R.color.primary_light)
                }
        ));
        chip.setChipStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary)));
        chip.setTextColor(new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                new int[]{
                        ContextCompat.getColor(this, R.color.white),
                        ContextCompat.getColor(this, R.color.primary_dark)
                }
        ));
        chip.setContentDescription(getString(R.string.available_seat_content_description, seatNumber));
        chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                updateSeatInput(seatNumber);
            }
        });
        return chip;
    }
}
