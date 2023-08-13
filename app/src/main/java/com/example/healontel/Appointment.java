package com.example.healontel;

public class Appointment {
    private String username;
    private String title;
    private String fullname;
    private String address;
    private String contact;
    private String selectedDate;
    private String selectedTime;
    private Float totalFee;
    private String appointmentType;

    public Appointment() {
        // Default constructor required for Firebase
    }

    public Appointment(String username, String title, String fullname, String address, String contact,
                       String selectedDate, String selectedTime, Float totalFee, String appointmentType) {
        this.username = username;
        this.title = title;
        this.fullname = fullname;
        this.address = address;
        this.contact = contact;
        this.selectedDate = selectedDate;
        this.selectedTime = selectedTime;
        this.totalFee = totalFee;
        this.appointmentType = appointmentType;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public String getFullname() {
        return fullname;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public Float getTotalFee() {
        return totalFee;
    }

    public String getAppointmentType() {
        return appointmentType;
    }
}
