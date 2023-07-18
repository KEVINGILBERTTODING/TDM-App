package com.example.tdm.ui.main.user.adapter.barang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdm.R;
import com.example.tdm.data.model.BarangModel;

import org.w3c.dom.Text;

import java.util.List;

public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.ViewHolder> {
    private Context context;
    private List<BarangModel> barangModelist;

    public BarangAdapter(Context context, List<BarangModel> barangModelist) {
        this.context = context;
        this.barangModelist = barangModelist;
    }

    @NonNull
    @Override
    public BarangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_barang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarangAdapter.ViewHolder holder, int position) {
        holder.tvNamaBarang.setText(barangModelist.get(holder.getAdapterPosition()).getName());
        holder.tvHargaBarang.setText(barangModelist.get(holder.getAdapterPosition()).getPrice());
        holder.tvStock.setText("Stock: " + String.valueOf(barangModelist.get(holder.getAdapterPosition()).getStock()));

    }

    @Override
    public int getItemCount() {
        return barangModelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNamaBarang, tvHargaBarang, tvStock;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHargaBarang = itemView.findViewById(R.id.tvHarga);
            tvNamaBarang = itemView.findViewById(R.id.tvNamaBarang);
            tvStock = itemView.findViewById(R.id.tvStock);
        }
    }
}
