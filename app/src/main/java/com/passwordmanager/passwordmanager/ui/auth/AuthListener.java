package com.passwordmanager.passwordmanager.ui.auth;

public interface AuthListener {

    public void onStarted();
    public void onSuccess();
    public void onFailure(String message);

}
