package com.example.cpp.lostfoundstud;
import com.google.firebase.firestore.Exclude;
public class Note {
    private String Mobileno;
    private String Name;
    private String address;
    private String date;
    private String description;

    // Constructors, getters, and setters

    public Note() {
        // Default constructor is required for Firestore
    }

    // Getter and Setter methods for each field
    public String getMobileno() {
        return Mobileno;
    }

    public void setMobileno(String Mobileno) {
        this.Mobileno = Mobileno;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}