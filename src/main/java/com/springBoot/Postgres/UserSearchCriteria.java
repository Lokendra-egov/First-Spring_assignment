package com.springBoot.Postgres;

public class UserSearchCriteria {
    private Long id;
    private String mobileNumber;
    private boolean active;

    public UserSearchCriteria(Long id, String mobileNumber, boolean active) {
        this.id = id;
        this.mobileNumber = mobileNumber;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}

