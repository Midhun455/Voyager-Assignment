package com.example.assignment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    Context context;
    private static final String DATABASE_NAME = "testDB.db";
    private static final int DATABASE_VERSION = 2;
    String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS Users " + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "Name TEXT, " + "Email TEXT, " + "Password TEXT)";
    String createTableQuery = "CREATE TABLE IF NOT EXISTS test(imageName TEXT,image BLOB)";
    String ADD_CATEGORY = "CREATE TABLE IF NOT EXISTS Categories (id INTEGER PRIMARY KEY AUTOINCREMENT ,category TEXT)";

    String ADD_PRODUCT = "CREATE TABLE IF NOT EXISTS Product(id INTEGER PRIMARY KEY AUTOINCREMENT,product_code TEXT,product_name TEXT,category TEXT,price TEXT,description TEXT,image BLOB)";

    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imageInBytes;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(ADD_CATEGORY);
            db.execSQL(createTableQuery);
            db.execSQL(CREATE_TABLE_USER);
            db.execSQL(ADD_PRODUCT);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    public void userRegister(ModelClass modelClass) {
        try {
            SQLiteDatabase objectSqLiteDatabase = this.getWritableDatabase();
            ContentValues objectContentValues = new ContentValues();
            objectContentValues.put("Name", modelClass.getName());
            objectContentValues.put("Email", modelClass.getEmail());
            objectContentValues.put("Password", modelClass.getPassword());
            long qryCheck = objectSqLiteDatabase.insert("Users", null, objectContentValues);
            System.out.println("Inserted " + qryCheck);
            if (qryCheck != 0) {
                System.out.println("Successfully Registered");
            } else {
                Toast.makeText(context, "Failed To Add Data", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean userLogin(String email, String password) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE Email = ? AND Password = ?", new String[]{email, password});
            if (cursor.moveToFirst()) {
                int emailColumnIndex = cursor.getColumnIndex("Email"); // Use lowercase "email" here
                if (emailColumnIndex != -1) {
                    String userEmail = cursor.getString(emailColumnIndex);
                    System.out.println("User Found with email: " + userEmail);
                }
                // Close the cursor before returning
                cursor.close();
                return true;
            } else {
                // Close the cursor before returning
                cursor.close();
                Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                System.out.println("User Not Found");
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean addCategory(ModelClass modelClass) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("category", modelClass.getCategory());
            long insertResult = db.insert("Categories", null, contentValues);
            System.out.println("Insert Result: " + insertResult);
            if (insertResult > 0) {
                Log.d("AddCategory", "Successfully Added Category");
                return true;
            } else {
                Log.e("AddCategory", "Failed To Add Category");
                Toast.makeText(context, "Failed To Add Category", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (Exception e) {
            Log.e("AddCategory", "Exception: " + e.getMessage());
            Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Categories", null);
        while (cursor.moveToNext()) {
            int columnIndex = cursor.getColumnIndex("category");
            if (columnIndex != -1) {
                String category = cursor.getString(columnIndex);
                categories.add(category);
            }
        }
        cursor.close();
        db.close();
        return categories;
    }

    public boolean addProduct(ModelClass modelClass) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Bitmap imageToStoreBitmap = modelClass.getImage();
            objectByteArrayOutputStream = new ByteArrayOutputStream();
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
            imageInBytes = objectByteArrayOutputStream.toByteArray();
            ContentValues contentValues = new ContentValues();
            contentValues.put("product_code", modelClass.getProduct_code());
            contentValues.put("product_name", modelClass.getProduct_name());
            contentValues.put("category", modelClass.getCategory());
            contentValues.put("price", modelClass.getPrice());
            contentValues.put("description", modelClass.getDescription());
            contentValues.put("image", imageInBytes);
            long insertResult = db.insert("Product", null, contentValues);
            System.out.println("Insert Result: " + insertResult);
            if (insertResult > 0) {
                Log.d("AddProduct", "Successfully Added Product");
                return true;
            } else {
                Log.e("AddProduct", "Failed To Add Product");
                Toast.makeText(context, "Failed To Add Product", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (Exception e) {
            Log.e("AddProduct", "Exception: " + e.getMessage());
            Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public ArrayList<ModelClass> getAllImageData() {
        try {
            SQLiteDatabase objectSQLiteDatabase = this.getReadableDatabase();
            ArrayList<ModelClass> objectModelClassList = new ArrayList<>();
            Cursor objectCursor = objectSQLiteDatabase.rawQuery("SELECT * FROM Product", null);
            if (objectCursor.getCount() != 0) {
                Log.d("DATA FOUND", "getAllImageData: ");
                while (objectCursor.moveToNext()) {
                    String pCode = objectCursor.getString(1);
                    String pName = objectCursor.getString(2);
                    String pCat = objectCursor.getString(3);
                    String pdt_price = objectCursor.getString(4);
                    String pDesc = objectCursor.getString(5);
                    byte[] imageBytes = objectCursor.getBlob(6);
                    Bitmap objectBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    objectModelClassList.add(new ModelClass(objectBitmap, pCode, pName, pCat, pdt_price, pDesc));
                }
                return objectModelClassList;
            } else {
                Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                return null;
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage() + "HHHHHHHHHHHHHHHHHHHHHHH");
            return null;
        }
    }

    public ArrayList<ModelClass> getAllCategoryData(String category) {
        try {
            SQLiteDatabase objectSQLiteDatabase = this.getReadableDatabase();
            ArrayList<ModelClass> objectModelClassList = new ArrayList<>();
            Cursor objectCursor = objectSQLiteDatabase.rawQuery("SELECT * FROM Product WHERE category =?", new String[]{category});
            if (objectCursor.getCount() != 0) {
                Log.d("DATA FOUND", "getAllImageData: ");
                while (objectCursor.moveToNext()) {
                    String pCode = objectCursor.getString(1);
                    String pName = objectCursor.getString(2);
                    String pCat = objectCursor.getString(3);
                    String pdt_price = objectCursor.getString(4);
                    String pDesc = objectCursor.getString(5);
                    byte[] imageBytes = objectCursor.getBlob(6);
                    Bitmap objectBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    objectModelClassList.add(new ModelClass(objectBitmap, pCode, pName, pCat, pdt_price, pDesc));
                }
                return objectModelClassList;
            } else {
                Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                return null;
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage() + "HHHHHHHHHHHHHHHHHHHHHHH");
            return null;
        }
    }

}
