package com.example.tdm.ui.main.user.barang;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tdm.R;
import com.example.tdm.data.api.UserServices;
import com.example.tdm.data.model.BarangModel;
import com.example.tdm.databinding.FragmentBarangBinding;
import com.example.tdm.ui.main.user.adapter.barang.BarangAdapter;
import com.example.tdm.util.ApiConfig;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BarangFragment extends Fragment {
    private FragmentBarangBinding binding;
    private BarangAdapter barangAdapter;
    private LinearLayoutManager linearLayoutManager;
    private UserServices userServices;
    private List<BarangModel> barangModelList;
    private AlertDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBarangBinding.inflate(inflater, container, false);
        init();



        return binding.getRoot();
    }

    private void init() {
        userServices = ApiConfig.getClient().create(UserServices.class);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBarang();
    }


    private void getBarang() {
        showProgressBar("Loading", "Memuat data...", true);
        userServices.getAllBarang().enqueue(new Callback<List<BarangModel>>() {
            @Override
            public void onResponse(Call<List<BarangModel>> call, Response<List<BarangModel>> response) {
                showProgressBar("", "", false);
                if (response.isSuccessful() && response.body() != null) {
                    barangModelList = response.body();
                    barangAdapter = new BarangAdapter(getContext(), barangModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvBarang.setLayoutManager(linearLayoutManager);
                    binding.rvBarang.setAdapter(barangAdapter);
                    binding.rvBarang.setHasFixedSize(true);
                    binding.tveEmpty.setVisibility(View.GONE);

                }else {
                    binding.tveEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<BarangModel>> call, Throwable t) {
                binding.tveEmpty.setVisibility(View.GONE);
                showToast("err", "Tidak ada koneksi internet");

            }
        });

    }


    private void showProgressBar(String title, String message, boolean isLoading) {
        if (isLoading) {
            // Membuat progress dialog baru jika belum ada
            if (progressDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setCancelable(false);
                progressDialog = builder.create();
            }
            progressDialog.show(); // Menampilkan progress dialog
        } else {
            // Menyembunyikan progress dialog jika ada
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }


    private void showToast(String jenis, String text) {

        if (jenis.equals("success")) {
            Toasty.success(getContext(), text, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(getContext(), text, Toasty.LENGTH_SHORT).show();
        }
    }
}