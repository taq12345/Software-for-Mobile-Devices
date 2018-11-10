package com.example.khizar_pc.campusrate5;

public class LoginPresenter implements LoginModel.OnLogin {
    private LoginView loginView;
    private LoginModel loginModel;

    LoginPresenter(LoginView loginView, LoginModel loginModel)
    {
        this.loginView = loginView;
        this.loginModel = loginModel;
    }

    public void tryLogin()
    {
        loginModel.login(this);
    }

    @Override
    public void onLoginAttempt() {
        loginView.afterAttempt();
    }
}
