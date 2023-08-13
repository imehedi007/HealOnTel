package com.example.healontel;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CartBuyMedicineActivity extends AppCompatActivity {
    HashMap<String, String> item;
    ArrayList list;
    SimpleAdapter sa;
    TextView tvTotal;
    ListView lst;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button dateButton, btnCheckout, btnBack;
    private String[][] packages = {};
    private List<Medicine> medicineList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_buy_medicine);
        Objects.requireNonNull(getSupportActionBar()).hide();

        dateButton = findViewById(R.id.buttonBMCartDate);
        btnCheckout = findViewById(R.id.buttonBMCartCheckout);
        btnBack = findViewById(R.id.buttonBMCartBack);
        tvTotal = findViewById(R.id.textViewBMCartTotalCost);
        lst = findViewById(R.id.listViewBMCart);
        StringBuilder concatenatedProducts = new StringBuilder();
        SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = sharedpreferences.getString("username", "").toString();


        // Assuming you have the username stored in the "username" variable

        DatabaseReference cartsReference = FirebaseDatabase.getInstance().getReference("Carts");
        cartsReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList();
                List<CartItem> cartItems = new ArrayList<>();
                for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                    CartItem cartItem = cartSnapshot.getValue(CartItem.class);
                    if (cartItem != null&&cartItem.getText().equals("medicine")) {
                        cartItems.add(cartItem);
                    }
                }
                if (cartItems.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No data Found!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CartBuyMedicineActivity.this, HomeActivity.class));
                } else {
                    for (int i = 0; i < cartItems.size(); i++) {
                        CartItem cartItem = cartItems.get(i);
                        String product = cartItem.getProduct();
                        concatenatedProducts.append(product).append(", ");
                        String text = cartItem.getText();
                        String price = cartItem.getPrice();
                        item = new HashMap<>();
                        item.put("line1", "Medicine Name: " + product);
                        item.put("line2", "");
                        item.put("line3", text);
                        item.put("line4", "");
                        item.put("line5", price+"taka");
                        list.add(item);
                    }
                }
                    sa = new SimpleAdapter(CartBuyMedicineActivity.this, list,
                            R.layout.multi_lines,
                            new String[]{"line1", "line2", "line3", "line4", "line5"},
                            new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
                    //ListView lst = findViewById(R.id.listViewBMCart);
                lst.setAdapter(sa);

                float totalCost = 0;
                for (CartItem item : cartItems) {
                    totalCost += Float.parseFloat(item.getPrice());
                }
                tvTotal.setText("Total Cost: " + totalCost + " Tk");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error here
                Log.e("CartBuyMedicine", "Error: " + error.getMessage());
            }
        });



        btnBack.setOnClickListener(v -> startActivity(new Intent(CartBuyMedicineActivity.this, BuyMedicineActivity.class)));
        btnCheckout.setOnClickListener(v -> {
            String result = concatenatedProducts.toString().trim();
            Intent it = new Intent(CartBuyMedicineActivity.this, BuyMedicineBookActivity.class);
            it.putExtra("products", result);
            it.putExtra("price", tvTotal.getText());
            it.putExtra("date", dateButton.getText());
            startActivity(it);
        });

        initDatePicker();
        dateButton.setOnClickListener(v -> datePickerDialog.show());

    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, i, i1, i2) -> {
            i1 = i1 + 1;
            dateButton.setText(i2 + "/" + i1 + "/" + i);
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis() + 86400000);
    }

}
