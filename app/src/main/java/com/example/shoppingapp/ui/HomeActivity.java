package com.example.shoppingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingapp.R;
import com.example.shoppingapp.session.SessionManager;
import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private TextView tvGreeting;
    private TextView tvStatus;
    private MaterialButton btnAuth;
    private boolean openTicketsAfterLogin;

    private final ActivityResultLauncher<Intent> loginLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                updateSessionState();
                if (result.getResultCode() == RESULT_OK) {
                    if (openTicketsAfterLogin) {
                        openTicketsAfterLogin = false;
                        startActivity(new Intent(this, MyTicketsActivity.class));
                    }
                } else {
                    openTicketsAfterLogin = false;
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);
        tvGreeting = findViewById(R.id.tvGreeting);
        tvStatus = findViewById(R.id.tvSessionStatus);
        btnAuth = findViewById(R.id.btnHomeAuth);
        MaterialButton btnMovies = findViewById(R.id.btnHomeProducts);
        MaterialButton btnTheaters = findViewById(R.id.btnHomeCategories);
        MaterialButton btnShowtimes = findViewById(R.id.btnHomeCart);

        btnAuth.setOnClickListener(v -> handleAuthAction());
        btnMovies.setOnClickListener(v -> startActivity(new Intent(this, MovieListActivity.class)));
        btnTheaters.setOnClickListener(v -> startActivity(new Intent(this, TheaterListActivity.class)));
        btnShowtimes.setOnClickListener(v -> startActivity(new Intent(this, ShowtimeListActivity.class)));
        MaterialButton btnMyTickets = findViewById(R.id.btnMyTickets);
        btnMyTickets.setOnClickListener(v -> {
            if (sessionManager.isLoggedIn()) {
                startActivity(new Intent(this, MyTicketsActivity.class));
            } else {
                openTicketsAfterLogin = true;
                Toast.makeText(this, R.string.login_required_ticket_list, Toast.LENGTH_SHORT).show();
                loginLauncher.launch(new Intent(this, LoginActivity.class));
            }
        });

        updateSessionState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSessionState();
    }

    private void handleAuthAction() {
        if (sessionManager.isLoggedIn()) {
            sessionManager.clearSession();
            Toast.makeText(this, R.string.logout_success, Toast.LENGTH_SHORT).show();
            updateSessionState();
        } else {
            loginLauncher.launch(new Intent(this, LoginActivity.class));
        }
    }

    private void updateSessionState() {
        if (sessionManager.isLoggedIn()) {
            tvGreeting.setText(getString(R.string.home_logged_in, sessionManager.getUsername()));
            tvStatus.setText(R.string.home_status_logged_in);
            btnAuth.setText(R.string.action_logout);
        } else {
            tvGreeting.setText(R.string.home_welcome);
            tvStatus.setText(R.string.home_status_guest);
            btnAuth.setText(R.string.action_login);
        }
    }
}
