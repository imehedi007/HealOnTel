package com.example.healontel;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Objects;

public class LabTestBookActivity extends AppCompatActivity {

    EditText edname,edaddress,edcontact,edpincode;
    Button btnBooking;
    DatabaseReference addorder;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_book);
        Objects.requireNonNull(getSupportActionBar()).hide();

        edname = findViewById(R.id.editTextLTBFullname);
        edaddress = findViewById(R.id.editTextLTBAddress);
        edcontact = findViewById(R.id.editTextLTBContact);
        edpincode = findViewById(R.id.editTextLTBPincode);
        btnBooking = findViewById(R.id.buttonLTBBooking);
        Intent intent = getIntent();
        String price = intent.getStringExtra("price");
        String product = intent.getStringExtra("products");
        String category = intent.getStringExtra("category");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");

        btnBooking.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            final String username = sharedPreferences.getString(KEY_USERNAME, "");

            addorder = FirebaseDatabase.getInstance().getReference("Orders");
            addorder.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    addProductOrder(username, product, price,category,date,time,edname.getText().toString(),edaddress.getText().toString(),edcontact.getText().toString(), Integer.parseInt(edpincode.getText().toString()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            // Assuming you have a reference to your Firebase database
            DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Carts");

            String usernameToRemove = username;

// Query the database to find and delete the data
            Query query = cartRef.orderByChild("username").equalTo(usernameToRemove);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot cartItemSnapshot : snapshot.getChildren()) {
                        // Delete the cart item
                        cartItemSnapshot.getRef().removeValue();
                    }
                    // Data removal complete
                    Toast.makeText(getApplicationContext(),"Your Booking is done successfully",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LabTestBookActivity.this,HomeActivity.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle the error
                }
            });

        });

    }

    private void addProductOrder(String username, String product, String price, String category, String date, String time, String name, String address, String contact, int pincode) {
        String orderId = addorder.push().getKey();

        OrderItem orderItem = new OrderItem(username, product, price, category,date,time,name,address,contact,pincode);

        assert orderId != null;
        addorder.child(orderId).setValue(orderItem);

    }

}