package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.database.CursorWindow;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment.databinding.ActivityViewProductsBinding;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ViewProductsActivity extends AppCompatActivity {
    ActivityViewProductsBinding binding;
    DatabaseHandler objectDatabaseHandler;
    CustomAdapter customAdapter;
    List<String> categories;
    String CATEGORY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            objectDatabaseHandler = new DatabaseHandler(this);
            categories = new ArrayList<>();
            categories.add(0, "All Products");
            categories.addAll(objectDatabaseHandler.getAllCategories());
        } catch (Exception e) {
            Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage() + "HHHHHHHHHHHHHHHHHHHHHHH");
        }


        //cursor Size
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); // 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, categories) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.arapey);
                    textView.setTypeface(typeface);
                    textView.setTextColor(Color.BLACK);
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
                if (CATEGORY.equals("All Products")) {
                    getData();
                } else {
                    getCategoryData(CATEGORY);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        getData();
    }

    private void getCategoryData(String category) {
        try {
            ArrayList<ModelClass> productList = objectDatabaseHandler.getAllCategoryData(category);
            System.out.println(productList + " category=");
            if (productList == null) {
                binding.rView.setVisibility(View.GONE);
                binding.emptyTextView.setVisibility(View.VISIBLE);
                binding.emptyTextView.setText("No products available for " + category);
            } else {
                binding.rView.setVisibility(View.VISIBLE);
                customAdapter = new CustomAdapter(productList);
                binding.rView.setLayoutManager(new LinearLayoutManager(this));
                binding.rView.setAdapter(customAdapter);
                binding.emptyTextView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getData() {
        try {
            customAdapter = new CustomAdapter(objectDatabaseHandler.getAllImageData());
            binding.rView.setLayoutManager(new LinearLayoutManager((this)));
            binding.rView.setAdapter(customAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage() + "HHHHHHHHHHHHHHHHHHHHHHH");
        }
    }
}