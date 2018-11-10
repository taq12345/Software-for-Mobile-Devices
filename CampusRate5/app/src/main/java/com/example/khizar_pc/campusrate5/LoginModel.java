package com.example.khizar_pc.campusrate5;

public class LoginModel {

    interface OnLogin
    {
        void onLoginAttempt();
    }

    public void login (final OnLogin listener)
    {
        listener.onLoginAttempt();
    }
}
