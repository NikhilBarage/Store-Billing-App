package com.example.bill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    //1
    private List<Product> products;

    //2
    public ProductAdapter(Context context) {
        this.context = context;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public int getCount() {
        return products != null ? products.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return products != null ? products.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.list_item_product, parent, false);
        }

        TextView productNameTextView = itemView.findViewById(R.id.productNameTextView);
        TextView productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
        TextView productQuantityTextView = itemView.findViewById(R.id.productQuantityTextView);

        Product product = products.get(position);

        productNameTextView.setText(product.getName());
        productPriceTextView.setText("Price: " + product.getPrice());
        productQuantityTextView.setText("Quantity: " + product.getQuantity());

        return itemView;
    }
}
