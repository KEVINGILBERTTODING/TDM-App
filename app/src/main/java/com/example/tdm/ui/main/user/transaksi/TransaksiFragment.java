package com.example.tdm.ui.main.user.transaksi;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tdm.R;
import com.example.tdm.data.api.UserServices;
import com.example.tdm.data.model.BarangModel;
import com.example.tdm.data.model.CartModel;
import com.example.tdm.data.model.CustomerModel;
import com.example.tdm.data.model.ResponseModel;
import com.example.tdm.databinding.FragmentTransaksiBinding;
import com.example.tdm.ui.main.user.adapter.barang.SpinnerBarangAdapter;
import com.example.tdm.ui.main.user.adapter.cart.CartAdapter;
import com.example.tdm.ui.main.user.adapter.customer.SpinnerCustomerAdapter;
import com.example.tdm.ui.main.user.history_transaksi.HistoryTransaksiFragment;
import com.example.tdm.util.ApiConfig;
import com.example.tdm.util.Constans;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class TransaksiFragment extends Fragment implements CartAdapter.OnButtonClickListener {

    private FragmentTransaksiBinding binding;
    private AlertDialog progressDialog;
    private UserServices userServices;
    private SharedPreferences sharedPreferences;
    private String userId, name;
    private CartAdapter cartAdapter;
    private List<CartModel> cartModelList;
    private LinearLayoutManager linearLayoutManager;
    private SpinnerCustomerAdapter spinnerCustomerAdapter;
    private List<CustomerModel> customerModelList;
    private Integer customerId, price;
    private String itemId, invoiceId;
    private SpinnerBarangAdapter spinnerBarangAdapter;
    private List<BarangModel> barangModelList;
    private Integer dibayar, lebih, total;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransaksiBinding.inflate(inflater, container, false);
        userServices = ApiConfig.getClient().create(UserServices.class);
        sharedPreferences = getContext().getSharedPreferences(Constans.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constans.USER_ID, null);
        name = sharedPreferences.getString(Constans.name, null);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
        getCart();
        binding.tvUser.setText(name);
        getBarang();
        getCustomer();
        generateNewInovice();
        getDibayar();
        getTotal();
    }

    private void listener() {
        binding.tvTanggal.setOnClickListener(View -> {
            datePicker(binding.tvTanggal);
        });

        binding.spCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                customerId = spinnerCustomerAdapter.getCustomerId(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spBarang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemId = spinnerBarangAdapter.getItemBarang(position);
                price = Integer.parseInt(spinnerBarangAdapter.getPrice(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnTambah.setOnClickListener(View -> {
            if (binding.tvTanggal.getText().toString().isEmpty()) {
                binding.tvTanggal.setError("Tidak boleh kosong");


            }else if (Integer.parseInt(binding.etJumlah.getText().toString()) <= 0){
                binding.etJumlah.setError("Jumlah tidak valid");


            }else if (binding.etJumlah.getText().toString().isEmpty()) {
                binding.etJumlah.setError("Tidak boleh kosong");

            }else {
                insertCart();
            }
        });

        binding.btnKirim.setOnClickListener(View -> {
            transactions();
        });
    }

    private void getCart(){
        showProgressBar("Loading", "Memuat produk", true);
        userServices.getCart(userId).enqueue(new Callback<List<CartModel>>() {
            @Override
            public void onResponse(Call<List<CartModel>> call, Response<List<CartModel>> response) {
                showProgressBar("", "", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    cartModelList = response.body();
                    cartAdapter = new CartAdapter(getContext(), cartModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvProduct.setLayoutManager(linearLayoutManager);
                    binding.rvProduct.setAdapter(cartAdapter);
                    cartAdapter.setOnButtonClickListener(TransaksiFragment.this);
                    binding.rvProduct.setHasFixedSize(true);
                }else {
                    binding.btnKirim.setEnabled(false);
                    showProgressBar("", "", false);

                }
            }

            @Override
            public void onFailure(Call<List<CartModel>> call, Throwable t) {
                showProgressBar("", "", false);
                showToast("err", "Periksa koneksi internet anda");

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


    @Override
    public void onButtonClicked() {
        getTotal();
        getCart();
    }

    private void datePicker(TextView tvDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateFormatted, monthFormatted;
                if (month < 10) {
                    monthFormatted = String.format("%02d", month + 1);
                }else {
                    monthFormatted = String.valueOf(month + 1);
                }

                if (dayOfMonth < 10) {
                    dateFormatted = String.format("%02d", dayOfMonth);
                }else {
                    dateFormatted = String.valueOf(dayOfMonth);
                }

                tvDate.setText(year + "-" + monthFormatted + "-" + dateFormatted);
            }

        });

        datePickerDialog.show();
    }

    private void getCustomer() {
        showProgressBar("Loading", "Memuat data customer", true);
        userServices.getCustomer().enqueue(new Callback<List<CustomerModel>>() {
            @Override
            public void onResponse(Call<List<CustomerModel>> call, Response<List<CustomerModel>> response) {
                showProgressBar("","", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    customerModelList = response.body();
                    spinnerCustomerAdapter = new SpinnerCustomerAdapter(getContext(), customerModelList);
                    binding.spCustomer.setAdapter(spinnerCustomerAdapter);

                }else {
                    binding.btnKirim.setEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<List<CustomerModel>> call, Throwable t) {
                showProgressBar("", "", false);
                showToast("err", "Periksa koneksi internet");

            }
        });
    }

    private void getBarang() {
        showProgressBar("Loading", "Memuat data barang", true);
        userServices.getAllBarangReady().enqueue(new Callback<List<BarangModel>>() {
            @Override
            public void onResponse(Call<List<BarangModel>> call, Response<List<BarangModel>> response) {
                showProgressBar("", "", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    barangModelList = response.body();
                    spinnerBarangAdapter = new SpinnerBarangAdapter(getContext(), barangModelList);
                    binding.spBarang.setAdapter(spinnerBarangAdapter);

                }else {
                    binding.btnKirim.setEnabled(false);
                }

            }

            @Override
            public void onFailure(Call<List<BarangModel>> call, Throwable t) {
                showProgressBar("", "", false);

            }
        });

    }

    private void generateNewInovice() {
        showProgressBar("Loading", "Memuat data", true);
        userServices.generateNewInvoice().enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                showProgressBar("", "", false);
                if (response.isSuccessful() && response.body().getCode() == 200) {
                    binding.tvInvoice.setText(response.body().getMessage());

                }else {
                    binding.btnKirim.setEnabled(false);
                    showToast("err", "Terjadi kesalahan memuat invoice baru");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                showProgressBar("", "", false);
                showToast("err", "Periksa koneksi internet anda");
                binding.btnKirim.setEnabled(false);

            }
        });

    }

    private void getDibayar() {
        showProgressBar("Loading", "Memuat data paket", true);
        userServices.getPaket().enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                showProgressBar("", "", false);
                if (response.isSuccessful() && response.body().getCode() == 200) {
                    dibayar = Integer.parseInt(response.body().getMessage());
                    formattedRupiah(binding.tvDibayar, Integer.parseInt(response.body().getMessage()));


                }else {
                    showToast("err", "Terjadi kesalahan");

                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                showProgressBar("", "", false);
                showToast("err", "Periksa koneksi internet");



            }
        });
    }

    private void getTotal() {
        showProgressBar("Loading", "Memuat data", true);
        userServices.getTotal(userId).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                showProgressBar("", "", false);
                if (response.isSuccessful() && response.body().getCode() == 200) {
                    total = Integer.parseInt(response.body().getMessage());
                    lebih = dibayar - total;
                    formattedRupiah(binding.tvTotalHarga, Integer.parseInt(response.body().getMessage()));
                    formattedRupiah(binding.tvSisa, lebih);

                }else {


                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                showProgressBar("", "", false);
                showToast("err", "Periksa koneksi internet");


            }
        });
    }

    private void insertCart() {
        showProgressBar("Loading", "Menambahkan produk", true);
        userServices.insertCart(
                itemId, String.valueOf(price), binding.etJumlah.getText().toString(), userId
        ).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                showProgressBar("", "", false);
                if (response.isSuccessful() && response.body().getCode() == 200) {
                    showToast("success", "Berhasil menambahkan ke dalam keranjang");
                    getTotal();
                    getCart();
                    binding.btnKirim.setEnabled(true);
                    binding.etJumlah.setText("0");
                    binding.tvTanggal.setText("Pilih tanggal");

                }else {
                    showToast("err", response.body().getMessage());


                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                showProgressBar("", "", false);
                showToast("err", "Tidak ada koneksi internet");

            }
        });

    }

    private void transactions() {
        showProgressBar("Loading", "Memproses transaksi anda", true);
        userServices.transactions(
                binding.tvInvoice.getText().toString(),
                String.valueOf(customerId),
                total,
                total,
                dibayar,
                String.valueOf(lebih),
                userId

        ).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                showProgressBar("", "", false);
                if (response.isSuccessful() && response.body().getCode() == 200) {
                    showToast("success", "Berhasil transaksi");
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameUser, new HistoryTransaksiFragment()).addToBackStack(null)
                            .commit();
                }else {
                    showToast("err", "Gagal transaksi");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                showProgressBar("", "", false);
                showToast("err", "Periksa koneksi internet anda");

            }
        });


    }

    private void formattedRupiah(TextView tvPrice, Integer nominal) {
        // Format rupiah
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        tvPrice.setText(formatRupiah.format(nominal));

    }
}