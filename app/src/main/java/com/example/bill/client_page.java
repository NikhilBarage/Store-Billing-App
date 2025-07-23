package com.example.bill;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.Manifest;

import com.google.android.material.textfield.TextInputEditText;


public class client_page extends AppCompatActivity {

    private DatabaseBackUp databaseBackUp;
    private ProductDataSource dataSource;
    private Spinner spin;
    private TextView total;
    private boolean success = false;
    private EditText orderQuantity, orderName, orderPrice;
    private ArrayList<Double> itemTotalPriceArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_page);

        Toolbar toolbar = findViewById(R.id.toolBAR);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bill");

        spin = findViewById(R.id.showProductstoclient);
        orderName = findViewById(R.id.orderNameTextVIEW);
        orderPrice = findViewById(R.id.orderPriceTextVIEW);
        orderQuantity = findViewById(R.id.orderQuantityEditText);
        total = findViewById(R.id.totalPrice);

        dataSource = new ProductDataSource(this);
        dataSource.open();

        databaseBackUp = new DatabaseBackUp(this);

        orderPrice.setEnabled(false);
        orderName.setEnabled(false);

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

        // Calculate total price and display invoice
        Button addOrderButton = findViewById(R.id.collectOrder);
        addOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {

                    String productName = orderName.getText().toString();
                    double quantity = Double.parseDouble(orderQuantity.getText().toString());
                    double price = Double.parseDouble(orderPrice.getText().toString());

                    double tvprice = quantity * price;

                    itemTotalPriceArray.add(tvprice);

                    TextView totalAmountTextView = new TextView(client_page.this);
                    totalAmountTextView.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));


                    String invoice = productName + " \t" + quantity + " \t" +  + tvprice;
                    totalAmountTextView.setText(invoice);


                    // Create a new TableRow for the invoice
                    TableRow invoiceRow = new TableRow(client_page.this);

                    TextView productView = new TextView(client_page.this);
                    productView.setText(productName);
                    productView.setTextColor(Color.WHITE);
                    productView.setTextSize(TypedValue.COMPLEX_UNIT_PX,40);
                    TextView quantityView = new TextView(client_page.this);
                    quantityView.setText(""+ quantity);
                    quantityView.setTextColor(Color.WHITE);
                    quantityView.setTextSize(TypedValue.COMPLEX_UNIT_PX,40);
                    TextView totalView = new TextView(client_page.this);
                    totalView.setText(""+ tvprice);
                    totalView.setTextColor(Color.WHITE);
                    totalView.setTextSize(TypedValue.COMPLEX_UNIT_PX,40);

                    // Add TextViews to the TableRow
                    invoiceRow.addView(productView);
                    invoiceRow.addView(quantityView);
                    invoiceRow.addView(totalView);

                    // Add the TableRow to the TableLayout
                    TableLayout invoiceTableLayout = findViewById(R.id.invoiceTableLayout);
                    invoiceTableLayout.addView(invoiceRow);


                    double newTotalBill = calculateTotalBill();
                    total.setText("Total Bill: Rs." + newTotalBill);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    String date = dateFormat.format(new Date());

                    Order newOrder = new Order();
                    newOrder.setNamee(productName);
                    newOrder.setPricee(tvprice);
                    newOrder.setQuantityy(quantity);
                    newOrder.setDate(date);

                    // Insert the new product into the database
                    long insertId = dataSource.insertOrder(newOrder);

                    if (insertId != -1) {
                        Toast.makeText(client_page.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                        clearInputFields();
                    } else {
                        Toast.makeText(client_page.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(client_page.this, "An Error Occured", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button newBill = findViewById(R.id.newBill);
        newBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launch1 = new Intent(client_page.this, client_page.class);
                startActivity(launch1);
                finish();
            }
        });

    }

    public void requestPermissionstoexport(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE }, 786);

            databaseBackUp.performBackUp(dataSource);
        }
        else {
            databaseBackUp.performBackUp(dataSource);
        }
    }

    private double calculateTotalBill() {
        double totalBill = 0.0;
        for (Double itemTotalPrice : itemTotalPriceArray) {
            totalBill += itemTotalPrice;
        }
        return totalBill;
    }


    private void updateUIWithSelectedProduct(Product selectedProduct) {
        orderName.setText(selectedProduct.getName());
        orderPrice.setText(String.valueOf(selectedProduct.getPrice()));
    }

    private void loadProductNames() {
        List<Product> products = dataSource.getAllProducts();
        customArrayAdapter adapter = new customArrayAdapter(this,
                android.R.layout.simple_spinner_item, products);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
    }

    private void clearInputFields() {
        orderName.setText("");
        orderPrice.setText("");
        orderQuantity.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.admin_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.adminadd) {
            // Handle add product action
            Intent addP = new Intent(client_page.this, SignUp.class);
            startActivity(addP);
            finish();
        } else if (id == R.id.adminupd) {
            // Handle add product action
            Intent addP1 = new Intent(client_page.this, client_Products.class);
            startActivity(addP1);
            finish();
        } else if (id == R.id.admininc) {
            // Handle add product action
            Intent addP2 = new Intent(client_page.this, client_income.class);
            startActivity(addP2);
            finish();
        } else if (id == R.id.backupId) {
            requestPermissionstoexport();
        }

        return super.onOptionsItemSelected(item);
    }

}