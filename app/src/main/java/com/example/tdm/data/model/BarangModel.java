package com.example.tdm.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BarangModel implements Serializable {
    @SerializedName("item_id")
    private String itemId;
    @SerializedName("barcode")
    private String barcode;
    @SerializedName("name")
    private String name;
    @SerializedName("category_id")
    private String categoryId;
    @SerializedName("unit_id")
    private String unitId;
    @SerializedName("price")
    private String price;
    @SerializedName("stock")
    private Integer stock;
    @SerializedName("image")
    private String image;

    public BarangModel(String itemId, String barcode, String name, String categoryId, String unitId, String price, Integer stock, String image) {
        this.itemId = itemId;
        this.barcode = barcode;
        this.name = name;
        this.categoryId = categoryId;
        this.unitId = unitId;
        this.price = price;
        this.stock = stock;
        this.image = image;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
