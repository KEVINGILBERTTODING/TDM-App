package com.example.tdm.ui.main.auth;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tdm.MainActivity;
import com.example.tdm.R;
import com.example.tdm.data.model.ResponseModel;
import com.example.tdm.util.ApiConfig;
import com.example.tdm.data.api.AuthInterface;
import com.example.tdm.util.Constans;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPasword;
    private Button btnLogin;
    private AuthInterface authInterface;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.etInputUsername);
        etPasword = findViewById(R.id.etInputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        authInterface = ApiConfig.getClient().create(AuthInterface.class);
        sharedPreferences = getSharedPreferences(Constans.SHARED_PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getBoolean(Constans.LOGGED, false)) {
            if (sharedPreferences.getInt(Constans.level, 0) == 1) { // admin
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();

            }else if (sharedPreferences.getInt(Constans.level, 0) == 2) { // Karyawan
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();

            }
        }

        listener();


    }



    private void listener() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("Tidak boleh kosong");
                    etUsername.requestFocus();
                }else if (etPasword.getText().toString().isEmpty()) {
                    etPasword.setError("Tidak boleh kosong");
                    etPasword.requestFocus();
                }else {
                    authInterface.login(
                            etUsername.getText().toString(),
                            etPasword.getText().toString()
                    ).enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            if (response.isSuccessful() && response.body().getCode() == 200) {
                                editor.putBoolean(Constans.LOGGED, true);
                                editor.putString(Constans.USER_ID, response.body().getUserId());
                                editor.putString(Constans.username, response.body().getUsername());
                                editor.putInt(Constans.level, Integer.parseInt(response.body().getLevel()));
                                editor.putString(Constans.name, response.body().getName());
                                editor.apply();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();

                            }else {
                                Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Tidak ada koneksi", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });
    }
}