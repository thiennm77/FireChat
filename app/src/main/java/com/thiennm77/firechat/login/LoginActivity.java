package com.thiennm77.firechat.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thiennm77.firechat.AppHelper;
import com.thiennm77.firechat.R;
import com.thiennm77.firechat.home.HomeActivity;
import com.thiennm77.firechat.register.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends Activity implements LoginContract.View {

    @BindView(R.id.email_wrapper)
    TextInputLayout mEmailWrapper;

    @BindView(R.id.password_wrapper)
    TextInputLayout mPasswordWrapper;

    @BindView(R.id.email)
    TextInputEditText mEmail;

    @BindView(R.id.password)
    TextInputEditText mPassword;

    @BindView(R.id.create_one)
    TextView mCreateOne;

    @BindView(R.id.login)
    Button mLogin;

    private ProgressDialog mProgressDialog;

    private LoginContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mPresenter = new LoginPresenter(this);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmailWrapper.setErrorEnabled(false);
                mPasswordWrapper.setErrorEnabled(false);
                AppHelper.hideKeyboard(LoginActivity.this);
                mPresenter.attempLogin(mEmail.getText().toString(), mPassword.getText().toString());
                //showProgressDialog();
            }
        });

        mCreateOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.register();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.addAuthStateListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.removeAuthStateListener();
    }

    @Override
    public void showEmailError() {
        mEmailWrapper.setError(getString(R.string.not_email));
    }

    @Override
    public void showPasswordError() {
        mPasswordWrapper.setError(getString(R.string.short_password));
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message,
                (message.length() < 70) ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showRegisterScreen() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void showHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this, R.style.WhiteProgressDialog);
        mProgressDialog.setTitle("Please wait..");
        mProgressDialog.setMessage("Authenticating...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
