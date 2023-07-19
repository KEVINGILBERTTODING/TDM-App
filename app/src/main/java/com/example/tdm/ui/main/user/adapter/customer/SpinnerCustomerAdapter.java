package com.example.tdm.ui.main.user.adapter.customer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.example.tdm.data.model.CustomerModel;

import java.util.List;

public class SpinnerCustomerAdapter extends ArrayAdapter<CustomerModel> {

   public SpinnerCustomerAdapter(@NonNull Context context, List< CustomerModel > driver){
            super(context, android.R.layout.simple_spinner_item, driver);
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setText(getItem(position).getName());
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setText(getItem(position).getName());
            return view;
        }

        // get banks id
    public Integer getCustomerId(int position) {
       return getItem(position).getCustomerId();
    }





    }
