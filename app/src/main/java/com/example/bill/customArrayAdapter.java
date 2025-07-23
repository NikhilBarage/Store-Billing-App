package com.example.bill;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class customArrayAdapter extends ArrayAdapter<Product>{
    private LayoutInflater inflater;

    public customArrayAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        Product product = getItem(position);

        TextView textView = itemView.findViewById(android.R.id.text1);
        textView.setTextColor(Color.RED);
        textView.setText(product != null ? product.getName() : "");
        textView.setPadding(90,20,20,20);

        return itemView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
