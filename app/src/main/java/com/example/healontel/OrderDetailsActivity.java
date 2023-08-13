package com.example.healontel;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.crypto.AEADBadTagException;

public class OrderDetailsActivity extends AppCompatActivity {

    private String [][] order_details = {};

    HashMap<String,String> item;
    ArrayList list;
    SimpleAdapter sa;
    ListView lst;
    Button btn;
    private List<OrderItem> orderItemList = new ArrayList<>();
    private List<Appointment> appointmentList = new ArrayList<>();
    private DatabaseReference ordersRef;
    private DatabaseReference appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Objects.requireNonNull(getSupportActionBar()).hide();

        btn = findViewById(R.id.buttonODBack);
        lst = findViewById(R.id.listViewDD);
        list = new ArrayList<HashMap<String, String>>();


        btn.setOnClickListener(v -> startActivity(new Intent(OrderDetailsActivity.this,HomeActivity.class)));

        ordersRef = FirebaseDatabase.getInstance().getReference("Orders");

        ordersRef.addValueEventListener(new ValueEventListener() {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderItemList.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    OrderItem orderItem = orderSnapshot.getValue(OrderItem.class);
                    if (orderItem != null && username.equals(orderItem.getUsername())) {
                        orderItemList.add(orderItem);
                    }
                }


                if (orderItemList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No data Found!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(OrderDetailsActivity.this, HomeActivity.class));
                } else {
                    list = new ArrayList<HashMap<String, String>>();
                    for (int i = 0; i < orderItemList.size(); i++) {
                        OrderItem orders = orderItemList.get(i);
                        String product = orders.getProduct();
                        String address = orders.getAddress();
                        String price = orders.getPrice();
                        String date = orders.getDate();
                        String time = orders.getTime();
                        item = new HashMap<>();
                        item.put("line1", product);
                        item.put("line2", "Address: " + address);
                        item.put("line3", "Lab");
                        item.put("line4", "Del: " + date + " " + time);
                        item.put("line5", price);
                        list.add(item);
                    }

                    sa = new SimpleAdapter(OrderDetailsActivity.this, list,
                            R.layout.multi_lines,
                            new String[]{"line1", "line2", "line3", "line4", "line5"},
                            new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
                    ListView lst = findViewById(R.id.listViewDD);
                    lst.setAdapter(sa);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        appointment = FirebaseDatabase.getInstance().getReference("Appointments");
        appointment.addValueEventListener(new ValueEventListener() {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentList.clear();

                for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                    Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                    if (appointment != null && appointment.getUsername().equals(username)) {
                        appointmentList.add(appointment);
                    }
                }
                if(appointmentList.isEmpty()){
                    Toast.makeText(getApplicationContext(),"No data Found!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(OrderDetailsActivity.this,HomeActivity.class));
                }else {
                    for(int i=0;i<appointmentList.size();i++){
                        Appointment appointment = appointmentList.get(i);
                        String name = appointment.getFullname();
                        String text = appointment.getAppointmentType();
                        String fee = String.valueOf(appointment.getTotalFee());
                        String date = appointment.getSelectedDate();
                        String time = appointment.getSelectedTime();
                        String address = appointment.getAddress();
                        String specialist = appointment.getTitle();
                        item = new HashMap<>();
                        item.put("line1", "Doctor's Name: " + name + " (" + address + ")");
                        item.put("line2", "Specialist: "+specialist);
                        item.put("line3", text);
                        item.put("line4", "Del: " + date + " " + time);
                        item.put("line5", "Fees: " + fee + "Tk");
                        list.add(item);
                    }
                    sa = new SimpleAdapter(OrderDetailsActivity.this, list,
                            R.layout.multi_lines,
                            new String[]{"line1", "line2", "line3", "line4", "line5"},
                            new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
                    ListView lst = findViewById(R.id.listViewDD);
                    lst.setAdapter(sa);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error here
                Log.e("DoctorDetails", "Error: " + error.getMessage());
            }
        });

    }
}