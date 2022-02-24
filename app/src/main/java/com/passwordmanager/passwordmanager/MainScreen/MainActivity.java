package com.passwordmanager.passwordmanager.MainScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.passwordmanager.passwordmanager.AddDataScreen.AddPaswordData;
import com.passwordmanager.passwordmanager.R;
import com.passwordmanager.passwordmanager.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityMainBinding binding;
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Setting click listner on fab button
        binding.addPassword.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AddPaswordData.class));
        });

    }
}