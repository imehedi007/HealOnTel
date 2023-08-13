package com.example.healontel;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class BuyMedicineDetailsActivity extends AppCompatActivity {

    TextView tvPackageName,tvTotalCost;
    EditText edDetails;
    Button btnBack,btnAddToCart;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine_details);
        Objects.requireNonNull(getSupportActionBar()).hide();

        tvPackageName = findViewById(R.id.textViewBMDPackageName);
        edDetails = findViewById(R.id.editTextTextBMDMultiLine);
        edDetails.setKeyListener(null);
        tvTotalCost = findViewById(R.id.textViewBMDTotalCost);
        btnBack = findViewById(R.id.buttonBMDBack);
        btnAddToCart = findViewById(R.id.buttonBMDAddToCart);

        Intent intent = getIntent();
        tvPackageName.setText(intent.getStringExtra(  "text1"));
        edDetails.setText(intent.getStringExtra("text2"));
        tvTotalCost.setText("Total Cost : "+intent.getStringExtra( "text3")+"Tk");

        btnBack.setOnClickListener(v -> startActivity(new Intent(BuyMedicineDetailsActivity.this,BuyMedicineActivity.class)));

        btnAddToCart.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            final String username = sharedPreferences.getString(KEY_USERNAME, "");

            String product = tvPackageName.getText().toString();
            Log.d("MyTag", "Debug message:   " + product);
            String price = intent.getStringExtra("text3").toString();

            // Upload the CartItem to the "Carts" table in Firebase Database
            DatabaseReference cartsReference = FirebaseDatabase.getInstance().getReference("Carts");

            // Query to check if the same medicine is already added to the cart by the same user
            cartsReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean item = false;
                    for (DataSnapshot medicineSnapshot : dataSnapshot.getChildren()) {

                        CartItem medicine = medicineSnapshot.getValue(CartItem.class);
                        if (medicine != null && medicine.getProduct().equals(product)&&medicine.getText().equals("medicine")) {
                            item = true;
                        }
                    }
                    if (item) {
                        // Item already exists in cart, show a toast
                        Toast.makeText(BuyMedicineDetailsActivity.this, "Already Added to cart", Toast.LENGTH_SHORT).show();
                    } else {
                        // Item doesn't exist, add it to the cart
                        String cartItemKey = cartsReference.push().getKey();
                        assert cartItemKey != null;

                        CartItem cartItem = new CartItem(username, product, price, "medicine");
                        cartsReference.child(cartItemKey).setValue(cartItem)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(BuyMedicineDetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                    // Optionally, navigate to cart or perform other actions
                                    startActivity(new Intent(BuyMedicineDetailsActivity.this, BuyMedicineActivity.class));
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(BuyMedicineDetailsActivity.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                                    // Handle failure, if necessary
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle the error
                }
            });
        });






//        btnAddToCart.setOnClickListener(v -> {
//            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//            final String username = sharedPreferences.getString(KEY_USERNAME, "");
//
//            String product = tvPackageName.getText().toString();
//            Log.d("MyTag", "Debug message:   "+product);
//            String price = intent.getStringExtra("text3").toString();
//            // Create a CartItem object
//            CartItem cartItem = new CartItem();
//
//            Log.d("MyTag", "cart message:   "+cartItem.getProduct());
//
//            // Upload the CartItem to the "Carts" table in Firebase Databa
//            DatabaseReference cartsReference = FirebaseDatabase.getInstance().getReference("Carts");
//            String cartItemKey = cartsReference.push().getKey();
//
//
//            assert cartItemKey != null;
//            if(cartItem.getUsername().equals(username)&&cartItem.getProduct().equals(product)&&cartItem.getText().equals("medicine")){
//                Toast.makeText(BuyMedicineDetailsActivity.this, "Already to Added to cart", Toast.LENGTH_SHORT).show();
//            }else {
//                cartItem = new CartItem(username, product, price, "medicine");
//                cartsReference.child(cartItemKey).setValue(cartItem)
//                        .addOnSuccessListener(aVoid -> {
//                            Toast.makeText(BuyMedicineDetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
//                            // Optionally, navigate to cart or perform other actions
//                            startActivity(new Intent(BuyMedicineDetailsActivity.this,BuyMedicineActivity.class));
//                        })
//                        .addOnFailureListener(e -> {
//                            Toast.makeText(BuyMedicineDetailsActivity.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
//                            // Handle failure, if necessary
//                        });
//            }
//
//        });
    }
}