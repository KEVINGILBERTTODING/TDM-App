package com.example.tdm.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CartModel implements Serializable {
    @SerializedName("cart_id")
    private String cartId;
    @SerializedName("item_id")
    private String itemId;
    @SerializedName("price")
    private Integer price;
    @SerializedName("qty")
    private Integer qty;
    @SerializedName("discount_item")
    private String discountItem;
    @SerializedName("total")
    private String total;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("name")
    private String name;


    public CartModel(String cartId, String itemId, Integer price, Integer qty, String discountItem, String total, String userId, String name) {
        this.cartId = cartId;
        this.itemId = itemId;
        this.price = price;
        this.qty = qty;
        this.discountItem = discountItem;
        this.total = total;
        this.userId = userId;
        this.name = name;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getDiscountItem() {
        return discountItem;
    }

    public void setDiscountItem(String discountItem) {
        this.discountItem = discountItem;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
