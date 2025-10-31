package com.example.demo.dto;

public class PersonalDetailsDto {
    private String title;
    private String firstName;
    private String middleName;
    private String lastName;
    private String dob;
    private String gender;
    private String fathersFirstName;
    private String mothersFirstName;
    private String pan;
    
    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getFathersFirstName() { return fathersFirstName; }
    public void setFathersFirstName(String fathersFirstName) { this.fathersFirstName = fathersFirstName; }
    
    public String getMothersFirstName() { return mothersFirstName; }
    public void setMothersFirstName(String mothersFirstName) { this.mothersFirstName = mothersFirstName; }
    
    public String getPan() { return pan; }
    public void setPan(String pan) { this.pan = pan; }
}