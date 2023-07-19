package com.example.tdm.ui.main.user.adapter.barang;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tdm.data.model.BarangModel;

import java.util.List;

public class SpinnerBarangAdapter extends ArrayAdapter<BarangModel> {

   public SpinnerBarangAdapter(@NonNull Context context, List< BarangModel > driver){
            super(context, android.R.layout.simple_spinner_item, driver);
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setText(getItem(position).getName() + " | " + "Rp." + getItem(position).getPrice() + " | " +"x" + getItem(position).getStock());
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setText(getItem(position).getName() + " | " + "Rp." + getItem(position).getPrice() + " | " +"x" + getItem(position).getStock());
            return view;
        }

        // get banks id
    public String getItemBarang(int position) {
       return getItem(position).getItemId();
    }

    public String getPrice(int position) {
       return getItem(position).getPrice();
    }





    }
