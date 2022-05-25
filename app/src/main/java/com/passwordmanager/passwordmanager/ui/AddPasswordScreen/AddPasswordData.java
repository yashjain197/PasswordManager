package com.passwordmanager.passwordmanager.ui.AddPasswordScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.passwordmanager.passwordmanager.R;
import com.passwordmanager.passwordmanager.data.RoomDB;
import com.passwordmanager.passwordmanager.data.passwordLocalDB;
import com.passwordmanager.passwordmanager.databinding.ActivityAddPaswordDataBinding;
import com.passwordmanager.passwordmanager.ui.PasswordDisplayScreen.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class AddPasswordData extends AppCompatActivity {
    ActivityAddPaswordDataBinding binding;
    Dialog dialog;
    List<passwordLocalDB> dataList=new ArrayList<>();
    RoomDB database;
    private boolean isClickable=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddPaswordDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(view -> {
            if(!binding.name.getText().toString().isEmpty() || !binding.username.getText().toString().isEmpty() || !binding.password.getText().toString().isEmpty() || !binding.description.getText().toString().isEmpty() ){
                showDialog();

            }else{
                AddPasswordData.this.finish();
            }

        });
        binding.username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(!binding.password.getText().toString().isEmpty()) {
                        binding.saveBtn.setBackgroundResource(R.drawable.textbox_outline);
                        isClickable=true;
                    }
                    if(binding.username.getText().toString().isEmpty()){
                        binding.saveBtn.setBackgroundResource(R.drawable.unselected_btn);
                        isClickable=false;
                    }
                    if(binding.name.getText().toString().isEmpty()){
                    binding.saveBtn.setBackgroundResource(R.drawable.unselected_btn);
                    isClickable=false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!binding.name.getText().toString().isEmpty()) {
                    binding.saveBtn.setBackgroundResource(R.drawable.textbox_outline);
                    isClickable=true;
                }
                if(binding.password.getText().toString().isEmpty()){
                    binding.saveBtn.setBackgroundResource(R.drawable.unselected_btn);
                    isClickable=false;
                }
                if(binding.username.getText().toString().isEmpty()){
                    binding.saveBtn.setBackgroundResource(R.drawable.unselected_btn);
                    isClickable=false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        binding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @SuppressLint("ResourceAsColor")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!binding.username.getText().toString().isEmpty()) {
                    binding.saveBtn.setBackgroundResource(R.drawable.textbox_outline);
                    isClickable=true;
                }
                if(binding.password.getText().toString().isEmpty()){
                    binding.saveBtn.setBackgroundResource(R.drawable.unselected_btn);
                    isClickable=false;
                }
                if(binding.name.getText().toString().isEmpty()){
                    binding.saveBtn.setBackgroundResource(R.drawable.unselected_btn);
                    isClickable=false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.saveBtn.setOnClickListener(view -> {
            if(!isClickable){
                if(binding.username.getText().toString().isEmpty()){
                    binding.username.setError("Required");
                }
                if(binding.password.getText().toString().isEmpty()){
                    binding.password.setError("Required");
                }
            }else{
                database= RoomDB.getInstance(this);
                passwordLocalDB data=new passwordLocalDB();
                data.setName(binding.name.getText().toString().trim());
                data.setUsername(binding.username.getText().toString().trim());
                data.setPassword(binding.password.getText().toString().trim());

                if(!binding.description.getText().toString().trim().isEmpty()){
                    data.setDescription(binding.description.getText().toString().trim());
                }else{
                    data.setDescription("N/A");
                }
                database.passwordDao().insert(data);
                Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
                MainActivity.dataList.clear();
                MainActivity.dataList.addAll(database.passwordDao().getAll());
                MainActivity.adapter.notifyDataSetChanged();
                AddPasswordData.this.finish();
            }
        });
    }


    public void showDialog(){
        dialog=new Dialog(this);
        dialog.setContentView(R.layout.custom_alert_dialog_box);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_alert_dialog_design));
        }
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.Animation_AppCompat_Dialog; //Setting the animations to dialog\
        Button yes=dialog.findViewById(R.id.btn_yes);
        Button no=dialog.findViewById(R.id.btn_no);

        yes.setOnClickListener(view1 -> {
            AddPasswordData.this.finish();
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (!binding.name.getText().toString().isEmpty() || !binding.username.getText().toString().isEmpty() || !binding.password.getText().toString().isEmpty() || !binding.description.getText().toString().isEmpty()) {
            showDialog();
        }else{
            super.onBackPressed();
        }
    }
}