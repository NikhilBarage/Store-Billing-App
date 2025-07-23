package com.example.bill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class client_Products extends AppCompatActivity {

    private ProductDataSource dataSource;
    private ProductAdapter productAdapter;
    private ListView productListListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_products);

        dataSource = new ProductDataSource(this);
        dataSource.open();

        productListListView = findViewById(R.id.clientproductListListView);

        productAdapter = new ProductAdapter(this);
        productListListView.setAdapter(productAdapter);


        refreshProductList();

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(client_Products.this, client_page.class);
        startActivity(i);
    }

    private void refreshProductList() {
        // Fetch all products from the database
        List<Product> products = dataSource.getAllProducts();

        // Update the adapter with the new data
        productAdapter.setProducts(products);
        productAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }

}