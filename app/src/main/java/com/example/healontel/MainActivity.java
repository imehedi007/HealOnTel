package com.example.healontel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);

        loadingDialog = new LoadingDialog(MainActivity.this);

        // Simulate a delay before dismissing the loading screen and opening the login activity
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();

                // Start the login activity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

                // Finish the current activity (optional)
                finish();
            }
        }, 3000); // Delay in milliseconds

        // Show the loading screen
        //loadingDialog.show();
    }
}

