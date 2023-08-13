package com.example.healontel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DoctorDetailsActivity extends AppCompatActivity {
    TextView tv;
    Button btn;
    HashMap<String, String> item;
    ArrayList list;
    SimpleAdapter sa;
    private DatabaseReference doctorsRef;
    private List<adminDoctorAdd> doctorList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);
        Objects.requireNonNull(getSupportActionBar()).hide();
        tv = findViewById(R.id.textViewDDTitle);
        btn = findViewById(R.id.buttonDDBack);

        Intent it = getIntent();
        String title = it.getStringExtra("title");
        //Log.d("123error", title);
        tv.setText(title);
        btn.setOnClickListener(v -> startActivity(new Intent(DoctorDetailsActivity.this, FindDoctorActivity.class)));
        list = new ArrayList();
        String selectedSpecialist = title; // Replace with the selected specialist

        assert selectedSpecialist != null;
        doctorsRef = FirebaseDatabase.getInstance().getReference("Doctors");

        doctorsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doctorList.clear();

                for (DataSnapshot doctorSnapshot : snapshot.getChildren()) {
                    adminDoctorAdd doctor = doctorSnapshot.getValue(adminDoctorAdd.class);
                    //Log.d("123error", doctor.getSpecialist());

                    if (doctor != null && doctor.getSpecialist().equals(selectedSpecialist)) {
                        doctorList.add(doctor);
                        Log.d("123error", doctor.getSpecialist());
                    }
                }
                if(doctorList.isEmpty()){
                    Toast.makeText(getApplicationContext(),"No data Found!",Toast.LENGTH_SHORT).show();
                   // startActivity(new Intent(DoctorDetailsActivity.this,FindDoctorActivity.class));
                }else {
                    String [] names = new String[10];
                    String [] experiences = new String[10];
                    String [] phones = new String[10];
                    String [] fees = new String[10];
                    String [] addresses = new String[10];
                    int value = 0;
                    for(int i=0;i<doctorList.size();i++){
                        adminDoctorAdd doctor = doctorList.get(i);
                        String name = doctor.getName();
                        value = i;
                        names[value] = name;
                        String experience = doctor.getExperience();
                        experiences[value] = experience;
                        String fee = doctor.getFee();
                        fees[value] = fee;
                        String phone = doctor.getPhone();
                        phones[value] = phone;
                        String address = doctor.getAddress();
                        addresses[value] = address;
                        String specialist = doctor.getSpecialist();
                        item = new HashMap<>();
                        item.put("line1", "Doctor's Name: " + name + " (" + address + ")");
                        item.put("line2", "Specialist: "+specialist);
                        item.put("line3", "Exp: " + experience + " yrs");
                        item.put("line4", "Cell: " + phone);
                        item.put("line5", "Fees: " + fee + "Tk");
                        list.add(item);
                        Log.d("123Doctor", "Name: " + doctor.getName());
                    }
                    sa = new SimpleAdapter(DoctorDetailsActivity.this, list,
                            R.layout.multi_lines,
                            new String[]{"line1", "line2", "line3", "line4", "line5"},
                            new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
                    ListView lst = findViewById(R.id.listViewDD);
                    lst.setAdapter(sa);
                    //int finalValue = value;
                    lst.setOnItemClickListener((adapterView, view, i, l) -> {
                        Intent it1 = new Intent(DoctorDetailsActivity.this, BookAppointmentActivity.class);
                        it1.putExtra("text1",title);
                        it1.putExtra("text2",names[i]);
                        it1.putExtra("text3",addresses[i]);
                        it1.putExtra("text4",phones[i]);
                        it1.putExtra("text5",fees[i]);
                        startActivity(it1);
                    });
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
