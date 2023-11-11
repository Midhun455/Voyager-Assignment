package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.assignment.databinding.ActivityAddCategoryBinding;
import com.google.android.material.snackbar.Snackbar;

public class AddCategory extends AppCompatActivity {
    ActivityAddCategoryBinding binding;
    DatabaseHandler objectDatabaseHandler;
    String CATEGORY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        objectDatabaseHandler = new DatabaseHandler(this);

        binding.btnAddCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private void validate() {
        CATEGORY = binding.catName.getEditText().getText().toString().trim();
        if (CATEGORY.isEmpty()) {
            showKeyboard();
            binding.catName.setError("Enter Category");
            binding.catName.requestFocus();
        } else {
            hideKeyboard();
            binding.catName.setError(null);
            addCategory(CATEGORY);
        }
    }

    private void addCategory(String category) {
        boolean result = objectDatabaseHandler.addCategory(new ModelClass(category));
        if (result) {
            Snackbar.make(binding.getRoot(), "Category Successfully Added", Snackbar.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    recreate();
                }
            }, 300);
        }
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