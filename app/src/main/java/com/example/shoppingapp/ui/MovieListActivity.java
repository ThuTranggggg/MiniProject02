package com.example.shoppingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.adapter.MovieAdapter;
import com.example.shoppingapp.model.Movie;
import com.example.shoppingapp.repository.ShoppingRepository;
import com.example.shoppingapp.utils.Constants;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class MovieListActivity extends AppCompatActivity {

    private ShoppingRepository repository;
    private MovieAdapter movieAdapter;
    private TextView tvEmpty;
    private List<Movie> allMovies = new ArrayList<>();
    private TextInputEditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        repository = ShoppingRepository.getInstance(this);
        tvEmpty = findViewById(R.id.tvProductEmpty);
        etSearch = findViewById(R.id.etSearchProduct);
        ImageButton btnBack = findViewById(R.id.btnBackProductList);
        TextView tvTitle = findViewById(R.id.tvProductListTitle);
        RecyclerView recyclerView = findViewById(R.id.rvProducts);

        tvTitle.setText(R.string.movies_title);
        btnBack.setOnClickListener(v -> finish());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new MovieAdapter(movie -> {
            Intent intent = new Intent(this, ShowtimeListActivity.class);
            intent.putExtra(Constants.EXTRA_MOVIE_ID, movie.getMovieId());
            intent.putExtra(Constants.EXTRA_MOVIE_TITLE, movie.getTitle());
            startActivity(intent);
        });
        recyclerView.setAdapter(movieAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterMovies(s == null ? "" : s.toString().trim());
            }
        });

        loadMovies();
    }

    private void loadMovies() {
        repository.getActiveMovies(new ShoppingRepository.DataCallback<List<Movie>>() {
            @Override
            public void onSuccess(List<Movie> data) {
                allMovies = data;
                movieAdapter.setMovies(allMovies);
                tvEmpty.setVisibility(allMovies.isEmpty() ? View.VISIBLE : View.GONE);
                if (allMovies.isEmpty()) {
                    tvEmpty.setText(R.string.empty_movies);
                }
            }

            @Override
            public void onError(String message) {
                tvEmpty.setVisibility(View.VISIBLE);
                tvEmpty.setText(message);
                Toast.makeText(MovieListActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterMovies(String keyword) {
        String normalizedKeyword = keyword == null ? "" : keyword.toLowerCase();
        List<Movie> filtered = new ArrayList<>();
        for (Movie movie : allMovies) {
            if (movie.getTitle().toLowerCase().contains(normalizedKeyword)
                    || movie.getGenre().toLowerCase().contains(normalizedKeyword)) {
                filtered.add(movie);
            }
        }
        movieAdapter.setMovies(filtered);
        tvEmpty.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
        if (filtered.isEmpty()) {
            tvEmpty.setText(normalizedKeyword.isEmpty() ? R.string.empty_movies : R.string.empty_movies_search);
        }
    }
}
