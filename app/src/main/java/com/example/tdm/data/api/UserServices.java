package com.example.tdm.data.api;

import com.example.tdm.data.model.BarangModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserServices {
    @GET("user/getAllProduct")
    Call<List<BarangModel>> getAllBarang();
}
