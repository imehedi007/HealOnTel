package com.example.healontel;

public class Medicine {
    private String medicineName;
    private String medicineDescription;
    private String medicinePrice;

    public Medicine() {

    }

    public Medicine(String medicineName, String medicineDescription, String medicinePrice) {
        this.medicineName = medicineName;
        this.medicineDescription = medicineDescription;
        this.medicinePrice = medicinePrice;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineDescription() {
        return medicineDescription;
    }

    public void setMedicineDescription(String medicineDescription) {
        this.medicineDescription = medicineDescription;
    }

    public String getMedicinePrice() {
        return medicinePrice;
    }

    public void setMedicinePrice(String medicinePrice) {
        this.medicinePrice = medicinePrice;
    }
}
