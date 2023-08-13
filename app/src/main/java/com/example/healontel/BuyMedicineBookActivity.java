package com.example.healontel;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class BuyMedicineBookActivity extends AppCompatActivity {

    EditText edname,edaddress,edcontact,edpincode;
    Button btnBooking;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_USERNAME = "username";
    DatabaseReference addorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine_book);
        Objects.requireNonNull(getSupportActionBar()).hide();


        edname = findViewById(R.id.editTextBMBFullname);
        edaddress = findViewById(R.id.editTextBMBAddress);
        edcontact = findViewById(R.id.editTextBMBContact);
        edpincode = findViewById(R.id.editTextBMBPincode);
        btnBooking = findViewById(R.id.buttonBMBBooking);

        Intent intent = getIntent();

        String time = null;
        String category = "medicine";
        String product = intent.getStringExtra("products");
        String price = intent.getStringExtra("price").toString();
        String date = intent.getStringExtra("date");
        // String time = intent.getStringExtra("time");



        btnBooking.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            final String username = sharedPreferences.getString(KEY_USERNAME, "");

            addorder = FirebaseDatabase.getInstance().getReference("Orders");
            addorder.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Log.d("MyTag", "Debug message: "+product);
                    addProductOrder(username, product, price,category,date, edname.getText().toString(),edaddress.getText().toString(),edcontact.getText().toString(), Integer.parseInt(edpincode.getText().toString()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Carts");

            String usernameToRemove = username;

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
                    startActivity(new Intent(BuyMedicineBookActivity.this,HomeActivity.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle the error
                }
            });

        });




//        btnBooking.setOnClickListener(v -> {
//            SharedPreferences sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
//            String username = sharedpreferences.getString("username","").toString();
//
//
//            Toast.makeText(getApplicationContext(),"Your Booking is done successfully",Toast.LENGTH_LONG).show();
//            startActivity(new Intent(BuyMedicineBookActivity.this,HomeActivity.class));
//        });
    }
    private void addProductOrder(String username, String product, String price, String category, String date, String name, String address, String contact, int pincode) {
        String orderId = addorder.push().getKey();

        OrderItem orderItem = new OrderItem(username, product, price, category,date, null,name,address,contact,pincode);
        assert orderId != null;
        addorder.child(orderId).setValue(orderItem);

    }
}
