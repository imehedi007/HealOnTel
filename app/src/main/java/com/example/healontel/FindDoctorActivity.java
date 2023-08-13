package com.example.healontel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Objects;


public class FindDoctorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);
        Objects.requireNonNull(getSupportActionBar()).hide();

        CardView exit = findViewById(R.id.cardFDBack);
        exit.setOnClickListener(v -> startActivity(new Intent(FindDoctorActivity.this,HomeActivity.class)));

        CardView familyphysician = findViewById(R.id.cardFDFamilyPhysician);
        familyphysician.setOnClickListener(v -> {
            Intent it = new Intent(FindDoctorActivity.this,DoctorDetailsActivity.class);
            it.putExtra("title","Family Doctors");
            startActivity(it);
        });

        CardView dietician = findViewById(R.id.cardFDDietician);
        dietician.setOnClickListener(v -> {
            Intent it = new Intent(FindDoctorActivity.this,DoctorDetailsActivity.class);
            it.putExtra("title","Dietician");
            startActivity(it);
        });

        CardView dentist = findViewById(R.id.cardFDDentist);
        dentist.setOnClickListener(v -> {
            Intent it = new Intent(FindDoctorActivity.this,DoctorDetailsActivity.class);
            it.putExtra("title","Dentist");
            startActivity(it);
        });

        CardView surgeon = findViewById(R.id.cardFDSurgeon);
        surgeon.setOnClickListener(v -> {
            Intent it = new Intent(FindDoctorActivity.this,DoctorDetailsActivity.class);
            it.putExtra("title","Surgeon");
            startActivity(it);
        });

        CardView cardiologists = findViewById(R.id.cardFDCardiologists);
        cardiologists.setOnClickListener(v -> {
            Intent it = new Intent(FindDoctorActivity.this,DoctorDetailsActivity.class);
            it.putExtra("title","Cardiologists");
            startActivity(it);
        });


    }
}
