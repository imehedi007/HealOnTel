package com.example.healontel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LabTestDetailsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_USERNAME = "username";
    TextView tvPackageName,tvTotalCost;
    EditText edDetails;
    Button btnAddToCart,btnBack;
    DatabaseReference cartRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_details);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //reference = FirebaseDatabase.getInstance().getReference("Carts");
        tvPackageName = findViewById(R.id.textViewLDPackageName);
        tvTotalCost = findViewById(R.id.textViewLDTotalCost);
        edDetails = findViewById(R.id.editTextLDTextMultiLine);
        btnAddToCart = findViewById(R.id.buttonLDAddToCart);
        btnBack = findViewById(R.id.buttonLDBack);


        edDetails.setKeyListener(null);

        Intent intent = getIntent();
        tvPackageName.setText(intent.getStringExtra("text1"));
        edDetails.setText(intent.getStringExtra("text2"));
        String text = "Total Cost: "+intent.getStringExtra("text3")+"Tk";
        tvTotalCost.setText(text);

        btnBack.setOnClickListener(v -> startActivity(new Intent(LabTestDetailsActivity.this,LabTestActivity.class)));

        btnAddToCart.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            final String username = sharedPreferences.getString(KEY_USERNAME, "");
            String product = tvPackageName.getText().toString();
            float price = Float.parseFloat(Objects.requireNonNull(intent.getStringExtra("text3")));

            cartRef = FirebaseDatabase.getInstance().getReference("Carts");
            // Define the username and product you want to check
            final String usernameToAdd = username;
            final String productToAdd = product;

            // Perform the query to check existence
            //Query query = cartRef.orderByChild("username").equalTo(usernameToCheck);
            cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean productExists = false;
                    for (DataSnapshot cartItem : snapshot.getChildren()) {
                        String username = cartItem.child("username").getValue(String.class);
                        String product = cartItem.child("product").getValue(String.class);

                        if (username != null && product != null && username.equals(usernameToAdd) && product.equals(productToAdd)) {
                            productExists = true;
                            break;
                        }
                    }

                    if (productExists) {
                        // Show error message: Product already exists in the cart
                        // You can display a Toast or a Snackbar here
                        Toast.makeText(getApplicationContext(),"Product Already Added",Toast.LENGTH_SHORT).show();
                    } else {
                        // Product does not exist, add it to the cart
                        addProductToCart(usernameToAdd, productToAdd, String.valueOf(price),"lab");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        });
    }

    private void addProductToCart(String username, String product, String price, String text) {
        // Generate a unique key for the new cart item
        String cartItemId = cartRef.push().getKey();

        // Create a new cart item with the provided values
        CartItem cartItem = new CartItem(username, product, price, text);

        // Add the new cart item to the database
        assert cartItemId != null;
        cartRef.child(cartItemId).setValue(cartItem);
        Toast.makeText(getApplicationContext(),"Record Inserted to Cart",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LabTestDetailsActivity.this,LabTestActivity.class));
        // Show success message: Product added to the cart
        // You can display a Toast or a Snackbar here
    }


}
