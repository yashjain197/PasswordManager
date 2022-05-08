package com.passwordmanager.passwordmanager.ui.auth;

import android.view.View;

import androidx.lifecycle.ViewModel;

public class AuthViewModel extends ViewModel {

    String phoneNumber=null;

    AuthListener authListener=null;

    public AuthViewModel(String phoneNumber){
        this.phoneNumber=phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void onGetOTPBtnClick(View view){
        authListener.onStarted();
        if(phoneNumber.isEmpty() || phoneNumber.length()<10){
            authListener.onFailure("Please enter a valid phone number");
        return;
        }
        authListener.onSuccess();
        //Success


    }

}
