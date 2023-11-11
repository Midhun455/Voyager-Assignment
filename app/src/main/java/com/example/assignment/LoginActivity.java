package com.example.assignment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.assignment.databinding.ActivityLoginBinding;
import com.google.android.material.snackbar.Snackbar;


public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding loginBinding;
    String EMAIL, PASS;

    DatabaseHandler databaseHandler;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA,};

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = new Toolbar(this);
        toolbar.setVisibility(View.INVISIBLE);

        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());
        databaseHandler = new DatabaseHandler(this);

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        loginBinding.txtReg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                startActivity(new Intent(getApplicationContext(), UserRegistration.class));
                return true;
            }
        });

        loginBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

    }

    private void validate() {
        EMAIL = loginBinding.logMail.getEditText().getText().toString().trim();
        PASS = loginBinding.logPass.getEditText().getText().toString().trim();
        String emailPattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";

        if (!EMAIL.matches(emailPattern)) {
            loginBinding.logMail.setError("Enter valid Email");
            loginBinding.logMail.requestFocus();
        } else if (PASS.isEmpty()) {
            loginBinding.logMail.setError(null);
            loginBinding.logPass.setError("Enter Password");
            loginBinding.logPass.requestFocus();
        } else {
            login(EMAIL, PASS);
        }
    }

    private void login(String EMAIL, String PASS) {
        boolean result = databaseHandler.userLogin(EMAIL, PASS);
        if (result) {
            hideKeyboard();
            Snackbar.make(loginBinding.getRoot(), "Login Successful", Snackbar.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
            }, 300);
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}
