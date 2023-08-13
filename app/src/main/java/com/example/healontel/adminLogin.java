package com.example.healontel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class adminLogin extends AppCompatActivity {
    private DatabaseReference databaseRef;
    private static final String PREFS_NAME = "admin";
    private static final String KEY_USERNAME = "username";
    private SessionManager sessionManager;
    TextView tv,tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        tv = findViewById(R.id.register);
        tv2 = findViewById(R.id.user);
        sessionManager = new SessionManager(getApplicationContext());
        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("Admins"); // Replace with your actual user node path

        FrameLayout buttonLogin = findViewById(R.id.loginButton);
        buttonLogin.setOnClickListener(v -> {
            // Retrieve username and password from EditText fields
            EditText editTextUsername = findViewById(R.id.edUsername);
            TextView editTextPassword = findViewById(R.id.edtpass);
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            // Query Firebase database for the entered username
            databaseRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User exists in the database, retrieve stored password
                        String storedPassword = dataSnapshot.child("password").getValue(String.class);

                        // Check if the entered password matches the stored password
                        if (password.equals(storedPassword)) {
                            // Login successful
                            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(KEY_USERNAME, username);
                            editor.apply();
                            sessionManager.setLoggedIn(true);
                            Toast.makeText(adminLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(adminLogin.this,adminHomeActivity.class));
                            // Proceed to the next activity or perform any desired action
                            finish();
                        } else {
                            // Incorrect password
                            Toast.makeText(adminLogin.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // User does not exist
                        Toast.makeText(adminLogin.this, "User does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur during data retrieval
                    Toast.makeText(adminLogin.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            });
        });

        tv.setOnClickListener(v -> startActivity(new Intent(adminLogin.this,adminRegistration.class)));
        tv2.setOnClickListener(v -> startActivity(new Intent(adminLogin.this,LoginActivity.class)));
    }
}