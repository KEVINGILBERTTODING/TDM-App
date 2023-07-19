package com.example.tdm.data.api;

import com.example.tdm.data.model.BarangModel;
import com.example.tdm.data.model.CartModel;
import com.example.tdm.data.model.CustomerModel;
import com.example.tdm.data.model.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserServices {
    @GET("user/getAllProduct")
    Call<List<BarangModel>> getAllBarang();

    @GET("user/getCart")
    Call<List<CartModel>> getCart(
            @Query("id") String id);

    @GET("user/getCustomer")
    Call<List<CustomerModel>> getCustomer();

    @GET("user/getAllProductReady")
    Call<List<BarangModel>> getAllBarangReady();

    @GET("user/generateNextInvoiceNumber")
    Call<ResponseModel> generateNewInvoice();
    @GET("user/getPaket")
    Call<ResponseModel> getPaket();

    @GET("user/getTotal")
    Call<ResponseModel> getTotal(
            @Query("id") String id
    );
    @FormUrlEncoded
    @POST("user/insertCart")
    Call<ResponseModel> insertCart(
            @Field("item_id") String item_id,
            @Field("price") String price,
            @Field("qty") String qty,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("user/deleteCart")
    Call<ResponseModel> deleteCart(
            @Field("item_id") String itemId,
            @Field("cart_id") String cartId,
            @Field("stock") Integer stock
    );




}
