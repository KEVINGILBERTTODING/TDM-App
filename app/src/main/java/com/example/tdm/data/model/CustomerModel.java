package com.example.tdm.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerModel implements Serializable {
    @SerializedName("customer_id")
    private Integer customerId;
    @SerializedName("name")
    private String name;

    public CustomerModel(Integer customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
