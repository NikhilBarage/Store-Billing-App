package com.example.bill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class updateProducts extends AppCompatActivity {

    private ProductDataSource dataSource;
    private Spinner spin;
    private EditText productNameUpdateEditText, productPriceUpdateEditText, productQuantityUpdateEditText, productIdEditText;
    private Button updateButton, backbtn, delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_products);

        spin = findViewById(R.id.showProducts);
        productIdEditText = findViewById(R.id.productIdEditText);
        productNameUpdateEditText = findViewById(R.id.productNameUpdateEditText);
        productPriceUpdateEditText = findViewById(R.id.productPriceUpdateEditText);
        productQuantityUpdateEditText = findViewById(R.id.productQuantityUpdateEditText);
        delete = findViewById(R.id.deleteButton);
        updateButton = findViewById(R.id.updateButton);

        dataSource = new ProductDataSource(this);
        dataSource.open();

        loadProductNames();

        // Handle Spinner item selection
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle selection
                Product selectedProduct = (Product) parentView.getItemAtPosition(position);
                if (selectedProduct != null) {
                    updateUIWithSelectedProduct(selectedProduct);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    updateProduct();
                }

                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(updateProducts.this, "An Error", Toast.LENGTH_SHORT).show();
                }

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    int productId = Integer.parseInt(productIdEditText.getText().toString());

                    dataSource.deleteProduct(productId);

                    Toast.makeText(updateProducts.this, "Product Deleted Successfully...", Toast.LENGTH_SHORT).show();
                    Intent addP2 = new Intent(updateProducts.this, updateProducts.class);
                    startActivity(addP2);
                    finish();
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(updateProducts.this, "An Error", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(updateProducts.this, MainActivity.class);
        startActivity(i);
    }

    private void updateUIWithSelectedProduct(Product selectedProduct) {
        productNameUpdateEditText.setText(selectedProduct.getName());
        productPriceUpdateEditText.setText(String.valueOf(selectedProduct.getPrice()));
        productIdEditText.setText(String.valueOf(selectedProduct.getId()));
        productQuantityUpdateEditText.setText(String.valueOf(selectedProduct.getQuantity()));
    }

    private void loadProductNames() {
        List<Product> products = dataSource.getAllProducts();
        customArrayAdapter adapter = new customArrayAdapter(this,
                android.R.layout.simple_spinner_item, products);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
    }

    private void updateProduct() {
        // Get the selected product from the Spinner
        Product selectedProduct = (Product) spin.getSelectedItem();

        // Get the updated price and quantity from the input fields
        double updatedPrice = Double.parseDouble(productPriceUpdateEditText.getText().toString());
        double updatedQuantity = Double.parseDouble(productQuantityUpdateEditText.getText().toString());

        // Update the selected product's price and quantity
        selectedProduct.setPrice(updatedPrice);
        selectedProduct.setQuantity(updatedQuantity);

        // Update the product in the database
        int rowsUpdated = dataSource.updateProduct(selectedProduct);

        if (rowsUpdated > 0) {
            Toast.makeText(updateProducts.this, "Update Product Successfully...", Toast.LENGTH_SHORT).show();
            clearInputFields();
        } else {
            Toast.makeText(updateProducts.this, "Failed to update product", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputFields() {
        productNameUpdateEditText.setText("");
        productPriceUpdateEditText.setText("");
        productQuantityUpdateEditText.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }

}