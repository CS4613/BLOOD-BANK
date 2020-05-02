package com.bloodbank;

public class DonorDetails {
    private String name, bloodGroup, mobileNo, email, address, zipCode, dob, gender;
    boolean isDonor;

    public DonorDetails() {

    }

    public DonorDetails(String name, String bloodGroup, String mobileNo,String email,String address, String zipCode, String dob, String gender, boolean isDonor) {
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.mobileNo = mobileNo;
        this.email = email;
        this.address = address;
        this.zipCode = zipCode;
        this.dob = dob;
        this.gender = gender;
        this.isDonor = isDonor;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public boolean isDonor() {
        return isDonor;
    }

    public String getName() {
        return name;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getMobileNo() {
        return mobileNo;
    }
}
