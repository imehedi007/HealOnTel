package com.example.healontel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class adminDoctorDetails extends AppCompatActivity {

    EditText edName,edExperience,edFee,edPhone,edAddress;
    Button btn;
    DatabaseReference addDoc;
    String specialist;
    adminDoctorAdd Doctors = new adminDoctorAdd();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admindoctordetails);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Spinner spinner = findViewById(R.id.spinner_id);
        edName = findViewById(R.id.names);
        edExperience = findViewById(R.id.experience);
        edFee = findViewById(R.id.fee);
        edPhone = findViewById(R.id.Phone);
        edAddress = findViewById(R.id.address);
        btn = findViewById(R.id.button);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.your_array_resource, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //spinner.setPrompt("Select an item");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                specialist = (String) parentView.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        btn.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");

            String name = edName.getText().toString();
            String experience = edExperience.getText().toString();
            String fee = edFee.getText().toString();
            String phone = edPhone.getText().toString();
            String address = edAddress.getText().toString();

            if (!name.isEmpty() && !experience.isEmpty() && !fee.isEmpty() && !phone.isEmpty() && !address.isEmpty()) {
                addDoc = FirebaseDatabase.getInstance().getReference("Doctors");
                String docID = addDoc.push().getKey();

                if (docID != null) {
                    Doctors.setUsername(username); // Set the username here
                    Doctors.setName(name);
                    Doctors.setExperience(experience);
                    Doctors.setFee(fee);
                    Doctors.setPhone(phone);
                    Doctors.setAddress(address);
                    Doctors.setSpecialist(specialist);

                    addDoc.child(docID).setValue(Doctors).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Record Inserted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(adminDoctorDetails.this, adminHomeActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "Error uploading data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });


    }
}