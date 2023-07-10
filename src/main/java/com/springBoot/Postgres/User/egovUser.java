package com.springBoot.Postgres.User;

import com.springBoot.Postgres.Address.Address;

import java.util.UUID;

public class egovUser {
    private UUID id;
    private String name;
    private String gender;
    private String mobileNumber;
    private boolean active;
    private long createdTime;
    private Address address;

    public egovUser(UUID id, String name, String gender, String mobileNumber, boolean active, long createdTime, Address address) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
        this.active = active;
        this.createdTime = createdTime;
        this.address = address;
    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }
}

