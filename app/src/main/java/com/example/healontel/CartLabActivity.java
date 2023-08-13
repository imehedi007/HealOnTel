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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CartLabActivity extends AppCompatActivity {

    private DatabaseReference cartRef;
    private ListView cartListView;
    HashMap<String ,String> item;
    ArrayList list;
    SimpleAdapter sa;
    TextView tvTotal;
    ListView lst;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button dateButton, timeButton, btnCheckout,btnBack;
    private String [][] packages = {};
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_USERNAME = "username";
    //private List<CartItem> dbData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_lab);
        Objects.requireNonNull(getSupportActionBar()).hide();

        dateButton = findViewById(R.id.buttonCartDate);
        timeButton = findViewById(R.id.buttonCartTime);
        btnCheckout = findViewById(R.id.buttonCartCheckout);
        btnBack = findViewById(R.id.buttonCartBack);
        tvTotal = findViewById(R.id.textViewCartTotalCost);
        lst = findViewById(R.id.listViewCart);
        list = new ArrayList();
        CartItem items = new CartItem();
        final String[] Products = new String[10];
        final String[] Categories = new String[10];
        StringBuilder concatenatedProducts = new StringBuilder();

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        final String desiredUsername = sharedPreferences.getString(KEY_USERNAME, "");
        final String desiredText = "lab";
        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        cartRef = firebaseDatabase.getReference("Carts");

        retrieveAndOrganizeData(desiredUsername, desiredText, new DataCallback() {
            @Override
            public void onDataLoaded(List<CartItem> dbData) {
                // Now you have the filtered and organized data in the dbData List
                // You can use it as needed
                float totalAmount = 0;

                for (int i = 0; i < dbData.size(); i++) {
                    CartItem cartItem = dbData.get(i);
                    String product = cartItem.getProduct();
                    concatenatedProducts.append(product).append(", ");
                    String price = cartItem.getPrice();
                    String text = cartItem.getText();
                    Categories[i] = cartItem.getText();
                    //Products[i] = Arrays.toString(new String[dbData.size()]);
                    Products[i] = cartItem.getProduct();
                    item = new HashMap<>();
                    item.put("line1", product);
                    item.put("line2", "");
                    item.put("line3", "Category: "+ text);
                    item.put("line4", "");
                    item.put("line5", "Cost: " + price + "Tk");
                    list.add(item);
                        totalAmount += Float.parseFloat(price);
                }
                tvTotal.setText("Total Cost : " + totalAmount);

                sa = new SimpleAdapter(CartLabActivity.this, list,
                        R.layout.multi_lines,
                        new String[]{"line1", "line2", "line3", "line4", "line5"},
                        new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
                lst.setAdapter(sa);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle the error here
            }
        });

        btnBack.setOnClickListener(v -> startActivity(new Intent(CartLabActivity.this, LabTestActivity.class)));

        btnCheckout.setOnClickListener(v -> {
            String result = concatenatedProducts.toString().trim();
            Intent it = new Intent(CartLabActivity.this, LabTestBookActivity.class);
            it.putExtra("price", tvTotal.getText().toString());
            it.putExtra("products", result);
            it.putExtra("category", Categories);
            it.putExtra("date", dateButton.getText());
            it.putExtra("time", timeButton.getText());
            startActivity(it);
        });

        initDatePicker();
        dateButton.setOnClickListener(v -> datePickerDialog.show());
        initTimePicker();
        timeButton.setOnClickListener(v -> timePickerDialog.show());
    }

    private void retrieveAndOrganizeData(final String username, final String text, final DataCallback callback) {
        final List<CartItem> dbData = new ArrayList<>();

        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dbData.clear();

                for (DataSnapshot cartItemSnapshot : snapshot.getChildren()) {
                    CartItem cartItem = cartItemSnapshot.getValue(CartItem.class);
                    if (cartItem != null && cartItem.getUsername().equals(username) && cartItem.getText().equals(text)) {
                        dbData.add(cartItem);
                    }
                }

                // Data is now organized in the dbData ArrayList
                // Invoke the callback with the data
                callback.onDataLoaded(dbData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error here
                callback.onCancelled(error);
            }
        });
    }
    // Callback interface for data handling
    interface DataCallback {
        void onDataLoaded(List<CartItem> dbData);
        void onCancelled(DatabaseError error);
    }
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, i, i1, i2) -> {
            i1 = i1 + 1;
            dateButton.setText(i2+"/"+i1+"/"+i);
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis() + 86400000);
    }


    private void initTimePicker(){
        TimePickerDialog.OnTimeSetListener timeSetListener = (timePicker, i, i1) -> timeButton.setText(i+":"+i1);
        Calendar cal = Calendar.getInstance();
        int hrs = cal.get(Calendar.HOUR);
        int mins = cal.get(Calendar.MINUTE);
        int style = AlertDialog. THEME_HOLO_DARK;
        timePickerDialog = new TimePickerDialog( this, style, timeSetListener, hrs, mins, true);
    }
}