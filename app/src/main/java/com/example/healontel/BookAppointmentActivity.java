package com.example.healontel;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

public class BookAppointmentActivity extends AppCompatActivity {

    EditText ed1,ed2,ed3,tv2;
    TextView tv;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_USERNAME = "username";
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button dateButton,timeButton,btnBack,btnBook;
    private DatabaseReference appointmentsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        Objects.requireNonNull(getSupportActionBar()).hide();
        tv = findViewById(R.id.textViewAppTitle);
        ed1 = findViewById(R.id.editTextAppFullName);
        ed2 = findViewById(R.id.editTextAppAddress);
        ed3 = findViewById(R.id.editTextAppContactNumber);
        tv2 = findViewById(R.id.textViewAppFees);
        dateButton = findViewById(R.id.buttonAppDate);
        timeButton = findViewById(R.id.buttonAppTime);
        btnBook = findViewById(R.id.buttonBookAppointment);
        btnBack = findViewById(R.id.buttonAppBack);
        appointmentsRef = FirebaseDatabase.getInstance().getReference("Appointments");

        ed1.setKeyListener(null);
        ed2.setKeyListener(null);
        ed3.setKeyListener(null);
        tv2.setKeyListener(null);
        // Retrieve data from intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("text1");
        String fullname = intent.getStringExtra("text2");
        String address = intent.getStringExtra("text3");
        String contact = intent.getStringExtra("text4");
        String fees = intent.getStringExtra("text5");

        tv.setText(title);
        ed1.setText(fullname);
        ed2.setText(address);
        ed3.setText(contact);
        tv2.setText("Consultation Fees: "+fees+" Tk");
        //datepicker
        initDatePicker();
        dateButton.setOnClickListener(v -> datePickerDialog.show());

        //timepicker
        initTimePicker();
        timeButton.setOnClickListener(v -> timePickerDialog.show());

        btnBack.setOnClickListener(v -> startActivity(new Intent(BookAppointmentActivity.this,FindDoctorActivity.class)));

        btnBook.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            final String username = sharedPreferences.getString(KEY_USERNAME, "");

            // Get other selected values
            String selectedDate = dateButton.getText().toString();
            String selectedTime = timeButton.getText().toString();

            // Calculate the total fee as a Float (assuming "fees" is a String containing a number)
            Float totalFee = Float.parseFloat(fees);

            // Create an Appointment object to store in the database
            Appointment appointment = new Appointment(username, title, fullname, address, contact, selectedDate, selectedTime, totalFee, "appointment");

            // Query to check if an appointment with the same username, title, and fullname already exists
//            appointmentsRef.orderByChild("usernameTitleFullname")
//                    .equalTo(username + title + fullname)
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
            appointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean productExists = false;
                            for (DataSnapshot appointment : snapshot.getChildren()) {
                                String username1 = appointment.child("username").getValue(String.class);
                                String title1 = appointment.child("title").getValue(String.class);
                                String fullname1 = appointment.child("fullname").getValue(String.class);

                                if (username1 != null && title1 != null && fullname1!=null && username1.equals(username) && title1.equals(title) && fullname1.equals(fullname)) {
                                    productExists = true;
                                    break;
                                }
                            }
                            if (productExists) {
                                // An appointment with the same details already exists
                                AlertDialog.Builder errorBuilder = new AlertDialog.Builder(BookAppointmentActivity.this);
                                errorBuilder.setTitle("Appointment Already Exists")
                                        .setMessage("An appointment with the same details already exists.")
                                        .setPositiveButton("OK", null)
                                        .show();
                            } else {
                                // No matching appointment found, proceed to book
                                String appointmentKey = appointmentsRef.push().getKey();
                                if (appointmentKey != null) {
                                    appointmentsRef.child(appointmentKey).setValue(appointment)
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    // Show a success message and navigate back
                                                    AlertDialog.Builder successBuilder = new AlertDialog.Builder(BookAppointmentActivity.this);
                                                    successBuilder.setTitle("Appointment Booked")
                                                            .setMessage("Your appointment has been successfully booked!")
                                                            .setPositiveButton("OK", (successDialog, whichButton) -> {
                                                                startActivity(new Intent(BookAppointmentActivity.this, FindDoctorActivity.class));
                                                            })
                                                            .show();
                                                } else {
                                                    // Handle error scenario
                                                    Log.e("BookAppointment", "Error: " + task.getException().getMessage());
                                                    // Show an error message if needed
                                                }
                                            });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle the error here
                            Log.e("BookAppointment", "Error: " + error.getMessage());
                        }
                    });
        });

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
