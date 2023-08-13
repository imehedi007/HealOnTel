package com.example.healontel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Objects.requireNonNull(getSupportActionBar()).hide();
        sessionManager = new SessionManager(getApplicationContext());

        // Check if the user is logged in or not
        if (!sessionManager.isLoggedIn()) {
            // Redirect to login screen if not logged in (Optional)
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // Your home page code

        //finDoctor
        CardView findDoctor = findViewById(R.id.cardFindDoctor);
        findDoctor.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this,FindDoctorActivity.class)));

        //labTest
        CardView labTest = findViewById(R.id.cardLabTest);
        labTest.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this,LabTestActivity.class)));

        //orderDetails
        CardView orderDetails = findViewById(R.id.cardOrderDetails);
        orderDetails.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this,OrderDetailsActivity.class)));

        //Buy Medicine
        CardView buyMedicine = findViewById(R.id.cardBuyMedicine);
        buyMedicine.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this,BuyMedicineActivity.class)));

        //Health
        CardView health = findViewById(R.id.cardHealthDoctor);
        health.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this,HealthArticlesActivity.class)));

        // Call the logout method when you want to log out
        CardView exit = findViewById(R.id.cardExit);
        exit.setOnClickListener(v -> {
            logout();
        });
    }
    private void logout() {
        // Clear session data (SharedPreferences)
        sessionManager.clearSession();

        // Redirect to login screen after logout
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
