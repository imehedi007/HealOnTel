package com.example.healontel;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_USERNAME = "username";

    private DatabaseReference databaseRef;
    private SessionManager sessionManager;
    TextView tv,tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        tv = findViewById(R.id.textViewNewUser);
        tv2 = findViewById(R.id.admin);
        sessionManager = new SessionManager(getApplicationContext());
        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("Users"); // Replace with your actual user node path

        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(v -> {
            // Retrieve username and password from EditText fields
            EditText editTextUsername = findViewById(R.id.editTextLoginUsername);
            EditText editTextPassword = findViewById(R.id.editTextLoginPassword);
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
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            // Proceed to the next activity or perform any desired action
                            finish();
                        } else {
                            // Incorrect password
                            Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // User does not exist
                        Toast.makeText(LoginActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur during data retrieval
                    Toast.makeText(LoginActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            });
        });

        tv.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,RegisterActivity.class)));
        tv2.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,adminLogin.class)));
    }
}





















//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.Objects;
//
//public class LoginActivity extends AppCompatActivity {
//
//    EditText edUsername,edPassword;
//    Button btn;
//    TextView tv;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        Objects.requireNonNull(getSupportActionBar()).hide();
//
//        edUsername = findViewById(R.id.editTextLoginUsername);
//        edPassword = findViewById(R.id.editTextLoginPassword);
//        btn = findViewById(R.id.buttonLogin);
//        tv = findViewById(R.id.textViewNewUser);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String username = edUsername.getText().toString();
//                String password = edPassword.getText().toString();
//                Database db = new Database(getApplicationContext(),"healthcare",null,1);
//
//                if(username.length()==0 || password.length()==0)
//                {
//                    Toast.makeText(getApplicationContext(),"Please fill up all details",Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    //startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                    if(db.login(username,password)==1)
//                    {
//                        Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_SHORT).show();
//
//                        SharedPreferences sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedpreferences.edit();
//                        editor.putString("username", username);
//                        editor.apply();
//                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                    }
//                    else
//                    {
//                        Toast.makeText(getApplicationContext(),"Invalid Username and Password",Toast.LENGTH_SHORT).show();
//                    }
//                }
//             }
//        });
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
//            }
//        });
//    }
//}