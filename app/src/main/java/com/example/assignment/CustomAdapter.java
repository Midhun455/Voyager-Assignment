package com.example.assignment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolderClass> {
    ArrayList<ModelClass> objectModelClassList;

    public CustomAdapter(ArrayList<ModelClass> objectModelClassList) {
        this.objectModelClassList = objectModelClassList;
    }


    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderClass(LayoutInflater.from(parent.getContext()).inflate(R.layout.imagedetails, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass viewholderclass, int position) {
        ModelClass objModelClass = objectModelClassList.get(position);
        viewholderclass.name.setText(objModelClass.getProduct_name() + "(" + objModelClass.getProduct_code() + ")");
        viewholderclass.price.setText("₹ " + objModelClass.getPrice());
        viewholderclass.desc.setText(objModelClass.getDescription());
        viewholderclass.pdtImageView.setImageBitmap(objModelClass.getImage());
    }

    @Override
    public int getItemCount() {
        return objectModelClassList.size();
    }

    public static class ViewHolderClass extends RecyclerView.ViewHolder {
        TextView name, price, desc;
        ImageView pdtImageView;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.pdtName);
            price = itemView.findViewById(R.id.pdtPrice);
            desc = itemView.findViewById(R.id.pdtDesc);
            pdtImageView = itemView.findViewById(R.id.imageView);
        }
    }

}
