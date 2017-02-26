package com.thiennm77.firechat.register;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.thiennm77.firechat.AppHelper;
import com.thiennm77.firechat.R;
import com.thiennm77.firechat.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends Activity implements RegisterContract.View {

    @BindView(R.id.email_wrapper)
    TextInputLayout mEmailWrapper;

    @BindView(R.id.username_wrapper)
    TextInputLayout mUsernameWrapper;

    @BindView(R.id.password_wrapper)
    TextInputLayout mPasswordWrapper;

    @BindView(R.id.confirm_password_wrapper)
    TextInputLayout mConfirmPasswordWrapper;

    @BindView(R.id.email)
    TextInputEditText mEmail;

    @BindView(R.id.username)
    TextInputEditText mUsername;

    @BindView(R.id.password)
    TextInputEditText mPassword;

    @BindView(R.id.confirm_password)
    TextInputEditText mConfirmPassword;

    @BindView(R.id.register)
    Button mRegister;

    @BindView(R.id.back)
    ImageButton mBack;

    private ProgressDialog mProgressDialog;
    private RegisterContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mPresenter = new RegisterPresenter(this);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmailWrapper.setErrorEnabled(false);
                mPasswordWrapper.setErrorEnabled(false);
                mUsernameWrapper.setErrorEnabled(false);
                mConfirmPasswordWrapper.setErrorEnabled(false);
                AppHelper.hideKeyboard(RegisterActivity.this);
                mPresenter.attemptRegister(
                        mEmail.getText().toString(),
                        mUsername.getText().toString(),
                        mPassword.getText().toString(),
                        mConfirmPassword.getText().toString()
                );
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
    public void showUsernameError() {
        mUsernameWrapper.setError(getString(R.string.invalid_username));
    }

    @Override
    public void showPasswordError() {
        mPasswordWrapper.setError(getString(R.string.short_password));
    }

    @Override
    public void showConfirmPasswordError() {
        mConfirmPasswordWrapper.setError(getString(R.string.not_match_password));
    }

    @Override
    public void showToast(String message) {
         Toast.makeText(this, message,
                 (message.length() < 70) ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG)
                 .show();
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
        mProgressDialog.setMessage("Creating account...");
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
