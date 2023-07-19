package com.example.tdm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Binder;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.tdm.databinding.ActivityMainBinding;
import com.example.tdm.ui.main.user.home.UserHomeFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuHome) {
                    replace(new UserHomeFragment());
                    return  true;
                }else if (item.getItemId() == R.id.menuProfile){
                    return false;
                }
                return false;
            }
        });



        replace(new UserHomeFragment());


    }

    private void replace(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameUser, fragment).commit();
    }




}