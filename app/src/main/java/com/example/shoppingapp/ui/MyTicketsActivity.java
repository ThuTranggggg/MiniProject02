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
import com.example.shoppingapp.adapter.TicketAdapter;
import com.example.shoppingapp.model.TicketDetail;
import com.example.shoppingapp.repository.ShoppingRepository;
import com.example.shoppingapp.session.SessionManager;

import java.util.List;

public class MyTicketsActivity extends AppCompatActivity {

    private ShoppingRepository repository;
    private SessionManager sessionManager;
    private TicketAdapter ticketAdapter;
    private View layoutLoginRequired;
    private View layoutEmpty;
    private TextView tvHeaderSubtitle;
    private RecyclerView recyclerView;

    private final ActivityResultLauncher<Intent> loginLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadTickets();
                } else if (!sessionManager.isLoggedIn()) {
                    finish();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);

        repository = ShoppingRepository.getInstance(this);
        sessionManager = new SessionManager(this);

        ImageButton btnBack = findViewById(R.id.btnBackMyTickets);
        layoutLoginRequired = findViewById(R.id.layoutTicketLoginRequired);
        layoutEmpty = findViewById(R.id.layoutTicketEmpty);
        tvHeaderSubtitle = findViewById(R.id.tvTicketHeaderSubtitle);
        recyclerView = findViewById(R.id.rvMyTickets);

        findViewById(R.id.btnTicketLogin).setOnClickListener(v -> loginLauncher.launch(new Intent(this, LoginActivity.class)));
        findViewById(R.id.btnBrowseShowtimes).setOnClickListener(v -> startActivity(new Intent(this, ShowtimeListActivity.class)));
        btnBack.setOnClickListener(v -> finish());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ticketAdapter = new TicketAdapter();
        recyclerView.setAdapter(ticketAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTickets();
    }

    private void loadTickets() {
        if (!sessionManager.isLoggedIn()) {
            layoutLoginRequired.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            tvHeaderSubtitle.setText(R.string.ticket_list_login_hint);
            return;
        }

        layoutLoginRequired.setVisibility(View.GONE);
        tvHeaderSubtitle.setText(getString(R.string.ticket_list_header_logged_in, sessionManager.getUsername()));

        repository.getTicketsByUser(sessionManager.getCurrentUserId(), new ShoppingRepository.DataCallback<List<TicketDetail>>() {
            @Override
            public void onSuccess(List<TicketDetail> data) {
                ticketAdapter.setTickets(data);
                boolean isEmpty = data.isEmpty();
                layoutEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
                recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onError(String message) {
                layoutEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(MyTicketsActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
