package com.example.healontel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class adminRegistration extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference reference;
    admin Admins;
    EditText edUsername;
    TextView edPassword;
    FrameLayout btn;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_register);
        Objects.requireNonNull(getSupportActionBar()).hide();

        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edtpass);
        btn = findViewById(R.id.registerButton);
        tv = findViewById(R.id.textViewExistingUser);
        Admins = new admin();
        reference = FirebaseDatabase.getInstance().getReference("Admins");

        tv.setOnClickListener(v -> startActivity(new Intent(adminRegistration.this,adminLogin.class)));

        btn.setOnClickListener(v -> {
            String username = edUsername.getText().toString();
            String password = edPassword.getText().toString();

            if(!username.isEmpty()&&!password.isEmpty()) {
                reference.child(username).get().addOnCompleteListener(task -> {
                    Admins.setUsername(username);
                    Admins.setPassword(password);
                    reference.child(username).setValue(Admins);
                    Toast.makeText(getApplicationContext(),"Record Inserted",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(adminRegistration.this,adminLogin.class));
                });
            }
        });

    }
}