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

public class adminHomeActivity extends AppCompatActivity{
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminhome);
        Objects.requireNonNull(getSupportActionBar()).hide();
        sessionManager = new SessionManager(getApplicationContext());

        // Check if the user is logged in or not
        if (!sessionManager.isLoggedIn()) {
            // Redirect to login screen if not logged in (Optional)
            Intent intent = new Intent(adminHomeActivity.this, adminLogin.class);
            startActivity(intent);
            finish();
        }

        // Your home page code

        CardView findDoctor = findViewById(R.id.cardDoctorDetails);
        findDoctor.setOnClickListener(v -> startActivity(new Intent(adminHomeActivity.this,adminDoctorDetails.class)));

        //Buy Medicine
        CardView buyMedicine = findViewById(R.id.cardBuyMedicine);
        buyMedicine.setOnClickListener(v -> startActivity(new Intent(adminHomeActivity.this,adminMedicineActivity.class)));

//        CardView labTest = findViewById(R.id.cardLabTest);
//        labTest.setOnClickListener(v -> startActivity(new Intent(adminHomeActivity.this,LabTestActivity.class)));





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
        Intent intent = new Intent(adminHomeActivity.this, adminLogin.class);
        startActivity(intent);
        finish();
    }
}
