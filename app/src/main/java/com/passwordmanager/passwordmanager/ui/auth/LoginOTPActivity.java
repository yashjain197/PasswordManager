package com.passwordmanager.passwordmanager.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.passwordmanager.passwordmanager.ui.PasswordDisplayScreen.MainActivity;
import com.passwordmanager.passwordmanager.R;
import com.passwordmanager.passwordmanager.databinding.ActivityLoginOtpBinding;

public class LoginOTPActivity extends AppCompatActivity {
    ActivityLoginOtpBinding binding;
    FirebaseAuth Auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LoginFragment loginFragment=new LoginFragment(LoginOTPActivity.this);
        loadFragments(loginFragment);

        Auth = FirebaseAuth.getInstance();
        //If current uer is already logged in we don't need this Activity to run.
        if(Auth.getCurrentUser()!=null){
            Intent intent =new Intent(LoginOTPActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }



    }
    private void loadFragments(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout,fragment);
        transaction.commit();
    }
}