package com.example.bill;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addProducts extends AppCompatActivity {

    private ProductDataSource dataSource;
    private ProductAdapter productAdapter;

    private EditText productNameEditText, productPriceEditText, productQuantityEditText;
    private Button addButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Products");

        productNameEditText = findViewById(R.id.productNameEditText);
        productPriceEditText = findViewById(R.id.productPriceEditText);
        productQuantityEditText = findViewById(R.id.productQuantityEditText);
        addButton = findViewById(R.id.addButton);

        dataSource = new ProductDataSource(this);
        dataSource.open();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(addProducts.this, MainActivity.class);
        startActivity(i);
    }

    private void addProduct() {
        String name = productNameEditText.getText().toString();
        String str = productPriceEditText.getText().toString();
        String strr = productQuantityEditText.getText().toString();

        if(name.isEmpty() || str.isEmpty() || strr.isEmpty()){
            Toast.makeText(this, "Please Fill All foelds", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(str);
        double quantity = Double.parseDouble(strr);

        boolean Exists = dataSource.doesProduct(name);
        if (Exists){
            Toast.makeText(this, "This Product Already Exists...", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new product
        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setPrice(price);
        newProduct.setQuantity(quantity);

        // Insert the new product into the database
        long insertId = dataSource.insertProduct(newProduct);

        if (insertId != -1) {
            Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
            clearInputFields();
        } else {
            Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputFields() {
        productNameEditText.setText("");
        productPriceEditText.setText("");
        productQuantityEditText.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }

}