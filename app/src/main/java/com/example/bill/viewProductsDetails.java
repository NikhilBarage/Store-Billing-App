package com.example.bill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class viewProductsDetails extends AppCompatActivity {

    private ProductDataSource dataSource;
    private ProductAdapter productAdapter;
    private ListView productListListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products_details);

        dataSource = new ProductDataSource(this);
        dataSource.open();

        productListListView = findViewById(R.id.productListListView);

        productAdapter = new ProductAdapter(this);
        productListListView.setAdapter(productAdapter);


        refreshProductList();

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(viewProductsDetails.this, MainActivity.class);
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