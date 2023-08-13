package com.example.healontel;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class adminMedicineActivity extends AppCompatActivity {
    private EditText medicineNameEditText;
    private EditText medicineDescriptionEditText;
    private EditText medicinePriceEditText;
    private Button submitButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminmedicine);
        Objects.requireNonNull(getSupportActionBar()).hide();
        medicineNameEditText = findViewById(R.id.medicineName);
        medicineDescriptionEditText = findViewById(R.id.medicineDescription);
        medicinePriceEditText = findViewById(R.id.medicinePrice);
        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(view -> {
            String medicineName = medicineNameEditText.getText().toString();
            String medicineDescription = medicineDescriptionEditText.getText().toString();
            String medicinePrice = medicinePriceEditText.getText().toString();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("medicines");
            String key = databaseReference.push().getKey();

            Medicine medicine = new Medicine(medicineName, medicineDescription, medicinePrice);
            assert key != null;
            databaseReference.child(key).setValue(medicine);
            Toast.makeText(adminMedicineActivity.this, "Medicine added successfully", Toast.LENGTH_SHORT).show();
            finish();

        });


    }
}
