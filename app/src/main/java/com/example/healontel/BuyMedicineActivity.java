package com.example.healontel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

public class BuyMedicineActivity extends AppCompatActivity {
    HashMap<String,String> item;
    ArrayList list;
    SimpleAdapter sa;
    ListView lst;
    Button btnBack,btnGoToCart;
    private List<Medicine> medicineList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine);
        Objects.requireNonNull(getSupportActionBar()).hide();

        lst = findViewById(R.id.listViewBM);
        btnBack = findViewById(R.id.buttonBMBack);
        btnGoToCart = findViewById(R.id.buttonBMGoToCart);
        list = new ArrayList();
        btnGoToCart.setOnClickListener(v -> startActivity(new Intent(BuyMedicineActivity.this,CartBuyMedicineActivity.class)));
        btnBack.setOnClickListener(v -> startActivity(new Intent(BuyMedicineActivity.this,HomeActivity.class)));
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("medicines");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                medicineList.clear();
                for (DataSnapshot medicineSnapshot : snapshot.getChildren()) {
                    Medicine medicine = medicineSnapshot.getValue(Medicine.class);
                    if (medicine != null) {
                        medicineList.add(medicine);
                    }
                }

                if(medicineList.isEmpty()){
                    Toast.makeText(getApplicationContext(), "No data Found!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BuyMedicineActivity.this, HomeActivity.class));
                } else {
                    String [] names = new String[10];
                    String [] descriptions = new String[10];
                    String [] prices = new String[10];
                    for(int i=0;i<medicineList.size();i++){
                        Medicine medicine = medicineList.get(i);
                        String name = medicine.getMedicineName();
                        names[i] = name;
                        String description = medicine.getMedicineDescription();
                        descriptions[i] = description;
                        String price = medicine.getMedicinePrice();
                        prices[i] = price;
                        item = new HashMap<>();
                        item.put("line1", "Medicine Name: " + name);
                        item.put("line2", "description: " + description);
                        item.put("line3", "Price: "+ price);
                        item.put("line4", "");
                        item.put("line5", "");
                        list.add(item);
                    }
                    sa = new SimpleAdapter(BuyMedicineActivity.this, list,
                            R.layout.multi_lines,
                            new String[]{"line1", "line2", "line3", "line4", "line5"},
                            new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
                    ListView lst = findViewById(R.id.listViewBM);
                    lst.setAdapter(sa);
                    lst.setOnItemClickListener((adapterView, view, i, l) -> {
                        Intent it1 = new Intent(BuyMedicineActivity.this, BuyMedicineDetailsActivity.class);
                        it1.putExtra("text1",names[i]);
                        Log.d("Tag", "Name: "+names[i]);

                        it1.putExtra("text2",descriptions[i]);
                        it1.putExtra("text3",prices[i]);
                        startActivity(it1);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
