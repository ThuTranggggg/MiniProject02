package com.example.shoppingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.adapter.TheaterAdapter;
import com.example.shoppingapp.model.Theater;
import com.example.shoppingapp.repository.ShoppingRepository;
import com.example.shoppingapp.utils.Constants;

import java.util.List;

public class TheaterListActivity extends AppCompatActivity {

    private ShoppingRepository repository;
    private TheaterAdapter theaterAdapter;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        repository = ShoppingRepository.getInstance(this);
        tvEmpty = findViewById(R.id.tvCategoryEmpty);
        ImageButton btnBack = findViewById(R.id.btnBackCategory);
        RecyclerView recyclerView = findViewById(R.id.rvCategories);
        TextView tvTitle = findViewById(R.id.tvCategoryTitle);

        tvTitle.setText(R.string.theaters_title);
        btnBack.setOnClickListener(v -> finish());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        theaterAdapter = new TheaterAdapter(theater -> {
            Intent intent = new Intent(this, ShowtimeListActivity.class);
            intent.putExtra(Constants.EXTRA_THEATER_ID, theater.getTheaterId());
            intent.putExtra(Constants.EXTRA_THEATER_NAME, theater.getName());
            startActivity(intent);
        });
        recyclerView.setAdapter(theaterAdapter);

        loadTheaters();
    }

    private void loadTheaters() {
        repository.getTheaters(new ShoppingRepository.DataCallback<List<Theater>>() {
            @Override
            public void onSuccess(List<Theater> theaters) {
                theaterAdapter.setTheaters(theaters);
                tvEmpty.setVisibility(theaters.isEmpty() ? View.VISIBLE : View.GONE);
                if (theaters.isEmpty()) {
                    tvEmpty.setText(R.string.empty_theaters);
                }
            }

            @Override
            public void onError(String message) {
                tvEmpty.setVisibility(View.VISIBLE);
                tvEmpty.setText(message);
                Toast.makeText(TheaterListActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
