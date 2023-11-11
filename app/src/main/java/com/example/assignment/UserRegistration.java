package com.example.assignment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment.databinding.ActivityUserRegistrationBinding;
import com.google.android.material.snackbar.Snackbar;


public class UserRegistration extends AppCompatActivity {
    ActivityUserRegistrationBinding binding;
    String NAME, EMAIL, PASSWORD, CNFPASS;
    DatabaseHandler objectDatabaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        objectDatabaseHandler = new DatabaseHandler(this);


        binding.txtLogin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                return true;
            }
        });

        binding.btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }


    private void validate() {

        NAME = binding.userName.getEditText().getText().toString().trim();
        EMAIL = binding.userMail.getEditText().getText().toString().trim();
        PASSWORD = binding.userPass.getEditText().getText().toString().trim();
        CNFPASS = binding.cnfPass.getEditText().getText().toString().trim();
        String emailPattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";

        if (NAME.isEmpty()) {
            showKeyboard();
            binding.userName.setError("Enter Name");
            binding.userName.requestFocus();
        } else if (!EMAIL.matches(emailPattern)) {
            showKeyboard();
            binding.userName.setError(null);
            binding.userMail.setError("Enter Valid Email");
            binding.userMail.requestFocus();
        } else if (PASSWORD.isEmpty()) {
            showKeyboard();
            binding.userMail.setError(null);
            binding.userPass.setError("Enter Password");
            binding.userPass.requestFocus();
        } else if (CNFPASS.isEmpty()) {
            showKeyboard();
            binding.userPass.setError(null);
            binding.cnfPass.setError("Confirm Password");
            binding.cnfPass.requestFocus();
        } else if (!PASSWORD.equals(CNFPASS)) {
            showKeyboard();
            binding.cnfPass.setError("Password not Matching");
            binding.cnfPass.requestFocus();
        } else {
            hideKeyboard();
            binding.cnfPass.setError(null);
            regUser();
        }
    }

    private void regUser() {
        objectDatabaseHandler.userRegister(new ModelClass(NAME, EMAIL, PASSWORD));
        Snackbar.make(binding.getRoot(), "Registration Successful", Snackbar.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        }, 300);
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

}