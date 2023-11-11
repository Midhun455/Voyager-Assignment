package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment.databinding.ActivityAddProductBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {

    ActivityAddProductBinding binding;
    List<String> categories;
    String CATEGORY, P_CODE, PNAME, PRICE, DESCRIPTION;
    DatabaseHandler databaseHandler;
    private static final int PICK_IMAGE_REQUEST = 100;
    private Uri imageFilePath;
    private Bitmap imageToStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHandler = new DatabaseHandler(this);

        categories = new ArrayList<>();
        categories.add(0, "Select a category");
        categories.addAll(databaseHandler.getAllCategories());

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, categories) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.arapey);
                    textView.setTypeface(typeface);
                    textView.setTextColor(Color.GRAY);
                    textView.setBackgroundColor(Color.LTGRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                    Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.arapey);
                    textView.setTypeface(typeface);
                }
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        binding.spinnerr.setAdapter(arrayAdapter);


        binding.spinnerr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CATEGORY = binding.spinnerr.getSelectedItem().toString().trim();
//                Toast.makeText(AddProductActivity.this, CATEGORY, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

    }

    private void validate() {
        P_CODE = binding.pCode.getEditText().getText().toString();
        PNAME = binding.name.getEditText().getText().toString();
        PRICE = binding.price.getEditText().getText().toString();
        DESCRIPTION = binding.desc.getEditText().getText().toString();


        if (binding.imgProduct.getDrawable() != null && imageToStore == null) {
            System.out.println(binding.imgProduct.getDrawable());
            Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show();
        } else if (CATEGORY.equals("Select a category")) {
            Toast.makeText(this, "Select Category", Toast.LENGTH_SHORT).show();
        } else if (P_CODE.isEmpty()) {
            showKeyboard();
            binding.pCode.setError("Enter Product Code"+binding.imgProduct.getDrawable());
            binding.pCode.requestFocus();
        } else if (PNAME.isEmpty()) {
            showKeyboard();
            binding.pCode.setError(null);
            binding.name.setError("Enter Product Name");
            binding.name.requestFocus();
        } else if (PRICE.isEmpty()) {
            showKeyboard();
            binding.name.setError(null);
            binding.price.setError("Enter Price");
            binding.price.requestFocus();
        } else if (DESCRIPTION.isEmpty()) {
            showKeyboard();
            binding.price.setError(null);
            binding.desc.setError("Enter Description");
            binding.desc.requestFocus();
        } else {
            hideKeyboard();
            binding.desc.setError(null);
            addProduct();
        }
    }

    private void addProduct() {
        boolean result = databaseHandler.addProduct(new ModelClass(imageToStore, P_CODE, PNAME, CATEGORY, PRICE, DESCRIPTION));
        if (result) {
            Snackbar.make(binding.getRoot(), "Product Successfully Added", Snackbar.LENGTH_LONG).show();
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

    public void chooseImage(View view) {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } catch (Exception e) {
            Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageFilePath = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);
                binding.imgProduct.setImageBitmap(imageToStore);

            }
        } catch (Exception e) {
            Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}