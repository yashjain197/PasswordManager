package com.passwordmanager.passwordmanager.ui.AddPasswordScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.hardware.biometrics.BiometricManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.passwordmanager.passwordmanager.R;
import com.passwordmanager.passwordmanager.data.RoomDB;
import com.passwordmanager.passwordmanager.data.passwordLocalDB;
import com.passwordmanager.passwordmanager.databinding.ActivityAddPaswordDataBinding;
import com.passwordmanager.passwordmanager.ui.PasswordDisplayScreen.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddPasswordData extends AppCompatActivity {
    ActivityAddPaswordDataBinding binding;
    Dialog dialog;
    RoomDB database;
    private boolean isVerification = false;
    int backgroundColor=-1;
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

                if(binding.name.getText().toString().isEmpty()){
                    binding.saveBtn.setBackgroundResource(R.drawable.unselected_btn);
                    isClickable=false;
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
                saveDataToRoom();
            }
        });

        binding.colorSelectButton.setOnClickListener(view -> {
            showColorPickerDialog()       ;
        });

        binding.encryptedOrNot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    //Enabled
                    isVerification=true;
                }else{
                //Disabled
                    isVerification=false;
                }
            }
        });
    }

    private void showColorPickerDialog() {
        int color= ContextCompat.getColor(this,R.color.bluePurp);
        AmbilWarnaDialog ambilWarnaDialog=new AmbilWarnaDialog(this, color, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                backgroundColor=color;
                binding.colorSelectButton.setBackgroundColor(backgroundColor);
            }
        });
        ambilWarnaDialog.show();
    }


    //Alert Dialog for unsaved changes
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

    public int getRandomColorCode(){
        Random random = new Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256),random.nextInt(256));

    }

    public void saveDataToRoom(){
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

        data.setVerification(isVerification);

        if(backgroundColor==-1){
            backgroundColor=getRandomColorCode();
        }

        data.setCustomColor(backgroundColor);

        database.passwordDao().insert(data);
        Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
        MainActivity.dataList.clear();
        MainActivity.dataList.addAll(database.passwordDao().getAll());
        MainActivity.adapter.notifyDataSetChanged();
        AddPasswordData.this.finish();
    }
}