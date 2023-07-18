package com.example.tdm.ui.main.user.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tdm.R;
import com.example.tdm.databinding.FragmentUserHomeFragmenyBinding;
import com.example.tdm.ui.main.user.barang.BarangFragment;

public class UserHomeFragment extends Fragment {
    private FragmentUserHomeFragmenyBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserHomeFragmenyBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
    }

    private void listener() {
        binding.lrBarang.setOnClickListener(View -> {
            replace(new BarangFragment());
        });
    }

    private void replace(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameUser, fragment)
                .addToBackStack(null).commit();
    }
}