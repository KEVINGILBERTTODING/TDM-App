package com.example.tdm.ui.main.user.adapter.cart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdm.R;
import com.example.tdm.data.api.UserServices;
import com.example.tdm.data.model.CartModel;
import com.example.tdm.data.model.ResponseModel;
import com.example.tdm.util.ApiConfig;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private List<CartModel> cartModelList;
    private AlertDialog progressDialog;

    private OnButtonClickListener onButtonClickListener;
    private UserServices userServices;


    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.onButtonClickListener = listener;
    }

    public CartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }


    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {

        holder.tvNamaBarang.setText(cartModelList.get(holder.getAdapterPosition()).getName());
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID")); // Membuat format Rupiah dengan locale Indonesia
        Integer price = cartModelList.get(holder.getAdapterPosition()).getPrice();
        Integer total = Integer.parseInt(cartModelList.get(holder.getAdapterPosition()).getTotal());
        String formattedPrice = formatRupiah.format(price); // Format nilai harga menjadi format Rupiah
        holder.tvHarga.setText(formattedPrice); // Set nilai harga yang sudah diformat ke TextView
        holder.tvTotal.setText(formatRupiah.format(total));
        holder.tvQty.setText("X" + String.valueOf(cartModelList.get(holder.getAdapterPosition()).getQty()));

    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNamaBarang, tvHarga, tvTotal, tvQty;
        private Button btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaBarang = itemView.findViewById(R.id.tvNamaBarang);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvQty = itemView.findViewById(R.id.tvQty);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            userServices = ApiConfig.getClient().create(UserServices.class);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Peringatan").setMessage("Apakah anda yakin ingin menghapus data ini?");
                    alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showProgressBar("Loading", "Menghapus produk", true);
                            userServices.deleteCart(
                                    cartModelList.get(getAdapterPosition()).getItemId(),
                                    cartModelList.get(getAdapterPosition()).getCartId(),
                                    cartModelList.get(getAdapterPosition()).getQty()
                            ).enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    showProgressBar("", "", false);
                                    if (response.isSuccessful() && response.body().getCode() == 200) {
                                        if (onButtonClickListener != null) {
                                            onButtonClickListener.onButtonClicked();
                                        }
                                        cartModelList.remove(getAdapterPosition());
                                        notifyDataSetChanged();
                                        Toasty.normal(context, "Produk dihapus", Toasty.LENGTH_SHORT).show();


                                    }else {
                                        showToast("err", "Gagal menghapus prooduk");

                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseModel> call, Throwable t) {
                                    showProgressBar("", "", false);
                                    showToast("err", "Periksa koneksi internet anda");

                                }
                            });

                        }
                    });
                    alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alert.show();
                }
            });
        }
    }

    private void showProgressBar(String title, String message, boolean isLoading) {
        if (isLoading) {
            // Membuat progress dialog baru jika belum ada
            if (progressDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
            Toasty.success(context, text, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(context, text, Toasty.LENGTH_SHORT).show();
        }
    }

    public interface OnButtonClickListener {
        void onButtonClicked();
    }
}
