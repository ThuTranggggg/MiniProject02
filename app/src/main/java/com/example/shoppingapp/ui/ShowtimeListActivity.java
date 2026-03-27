package com.example.shoppingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.adapter.ShowtimeAdapter;
import com.example.shoppingapp.model.ShowtimeDisplayItem;
import com.example.shoppingapp.repository.ShoppingRepository;
import com.example.shoppingapp.session.SessionManager;
import com.example.shoppingapp.utils.Constants;

import java.util.List;

public class ShowtimeListActivity extends AppCompatActivity {

    private ShoppingRepository repository;
    private SessionManager sessionManager;
    private ShowtimeAdapter showtimeAdapter;
    private TextView tvEmpty;
    private TextView tvTitle;
    private Integer movieId;
    private Integer theaterId;
    private int pendingShowtimeId = Constants.INVALID_ID;

    private final ActivityResultLauncher<Intent> loginLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && pendingShowtimeId != Constants.INVALID_ID) {
                    openBookTicketScreen(pendingShowtimeId);
                }
                pendingShowtimeId = Constants.INVALID_ID;
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        repository = ShoppingRepository.getInstance(this);
        sessionManager = new SessionManager(this);
        tvEmpty = findViewById(R.id.tvProductEmpty);
        tvTitle = findViewById(R.id.tvProductListTitle);
        ImageButton btnBack = findViewById(R.id.btnBackProductList);
        RecyclerView recyclerView = findViewById(R.id.rvProducts);
        View searchField = findViewById(R.id.etSearchProduct);

        searchField.setVisibility(View.GONE);
        tvTitle.setText(R.string.showtimes_title);
        btnBack.setOnClickListener(v -> finish());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        showtimeAdapter = new ShowtimeAdapter(this::openBookTicket);
        recyclerView.setAdapter(showtimeAdapter);

        if (getIntent().hasExtra(Constants.EXTRA_MOVIE_ID)) {
            movieId = getIntent().getIntExtra(Constants.EXTRA_MOVIE_ID, Constants.INVALID_ID);
            String movieTitle = getIntent().getStringExtra(Constants.EXTRA_MOVIE_TITLE);
            if (movieId != null && movieId != Constants.INVALID_ID && movieTitle != null) {
                tvTitle.setText(getString(R.string.showtimes_by_movie_title, movieTitle));
            }
        }
        if (getIntent().hasExtra(Constants.EXTRA_THEATER_ID)) {
            theaterId = getIntent().getIntExtra(Constants.EXTRA_THEATER_ID, Constants.INVALID_ID);
            String theaterName = getIntent().getStringExtra(Constants.EXTRA_THEATER_NAME);
            if (theaterId != null && theaterId != Constants.INVALID_ID && theaterName != null) {
                tvTitle.setText(getString(R.string.showtimes_by_theater_title, theaterName));
            }
        }

        loadShowtimes();
    }

    private void loadShowtimes() {
        repository.getShowtimes(movieId, theaterId, new ShoppingRepository.DataCallback<List<ShowtimeDisplayItem>>() {
            @Override
            public void onSuccess(List<ShowtimeDisplayItem> showtimes) {
                showtimeAdapter.setShowtimes(showtimes);
                tvEmpty.setVisibility(showtimes.isEmpty() ? View.VISIBLE : View.GONE);
                if (showtimes.isEmpty()) {
                    tvEmpty.setText(R.string.empty_showtimes);
                }
            }

            @Override
            public void onError(String message) {
                tvEmpty.setVisibility(View.VISIBLE);
                tvEmpty.setText(message);
            }
        });
    }

    private void openBookTicket(ShowtimeDisplayItem showtime) {
        pendingShowtimeId = showtime.getShowtimeId();
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, R.string.login_required_booking, Toast.LENGTH_SHORT).show();
            loginLauncher.launch(new Intent(this, LoginActivity.class));
            return;
        }
        openBookTicketScreen(showtime.getShowtimeId());
    }

    private void openBookTicketScreen(int showtimeId) {
        Intent intent = new Intent(this, BookTicketActivity.class);
        intent.putExtra(Constants.EXTRA_SHOWTIME_ID, showtimeId);
        startActivity(intent);
        pendingShowtimeId = Constants.INVALID_ID;
    }
}
