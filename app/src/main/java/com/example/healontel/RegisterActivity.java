package com.example.healontel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference reference;
    Users users;
    EditText edUsername,edEmail,edPassword,edConfirm;
    Button btn;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).hide();

        edUsername = findViewById(R.id.editTextRegUsername);
        edPassword = findViewById(R.id.editTextRegPassword);
        edEmail = findViewById(R.id.editTextRegEmail);
        edConfirm = findViewById(R.id.editTextRegConfirmPassword);
        btn = findViewById(R.id.buttonRegister);
        tv = findViewById(R.id.textViewExistingUser);
        users = new Users();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        tv.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this,LoginActivity.class)));
        btn.setOnClickListener(v -> {
            String username = edUsername.getText().toString();
            String email = edEmail.getText().toString();
            String password = edPassword.getText().toString();
            String confirm = edConfirm.getText().toString();
            if(!username.isEmpty()&&!email.isEmpty()&&!password.isEmpty()&&!confirm.isEmpty()){

                reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.child(username).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            // Username already exists, handle the case (e.g., show an error message)
                            Toast.makeText(getApplicationContext(),"Username Already Exists!",Toast.LENGTH_SHORT).show();
                        } else {
                            // Username is available, proceed with registration or desired action
                            if (isValidEmail(email)) {
                                // Email is valid, proceed with your desired action
                                if(validatePassword(password, confirm)){
                                    users.setUsername(username);
                                    users.setEmail(email);
                                    users.setPassword(password);
                                    users.setConfirm(confirm);
                                    reference.child(username).setValue(users);
                                    Toast.makeText(getApplicationContext(),"Record Inserted",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                }else {
                                    Toast.makeText(getApplicationContext(),"Error Occurred!",Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                // Email is invalid, display an error message or take appropriate action
                                Toast.makeText(getApplicationContext(),"Invalid Email!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        // Handle potential errors
                        Toast.makeText(getApplicationContext(),"Please enter a valid username!",Toast.LENGTH_SHORT).show();
                    }
                });

            }else {
                Toast.makeText(getApplicationContext(),"Please enter the fields!",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public boolean validatePassword(String password, String con_pass) {

        int minLength = 8;
        if(!password.matches(con_pass)){
            Toast.makeText(getApplicationContext(),"Password does not match",Toast.LENGTH_SHORT).show();
            return false;
        }
        // the minimum length requirement
        if (password.length() < minLength) {
            Toast.makeText(getApplicationContext(),"Password's minimum length is 8",Toast.LENGTH_SHORT).show();
            return false;
        }

        // at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            Toast.makeText(getApplicationContext(),"At least one uppercase needed!",Toast.LENGTH_SHORT).show();
            return false;
        }

        // at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            Toast.makeText(getApplicationContext(),"Atleast one lowercase needed!",Toast.LENGTH_SHORT).show();
            return false;
        }

        // at least one digit
        if (!password.matches(".*\\d.*")) {
            Toast.makeText(getApplicationContext(),"Please insert atleast one digit",Toast.LENGTH_SHORT).show();
            return false;
        }

        // at least one special character
        if (!password.matches(".*[@#$%^&+=].*")) {
            Toast.makeText(getApplicationContext(),"Please insert atleast one special character",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}