package com.example.bill;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.backup.BackupManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.content.SharedPreferences;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProductDataSource dataSource;
    private OrderAdapter orderAdapter;
    private ListView dateListListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bill");

        dataSource = new ProductDataSource(this);
        dataSource.open();

        dateListListView = findViewById(R.id.dateListListView);

        orderAdapter = new OrderAdapter(this);
        dateListListView.setAdapter(orderAdapter);

        refreshProductList();

    }

    private void refreshProductList() {
        // Fetch all products from the database
        List<Order> orders = dataSource.getAllOrders();

        // Update the adapter with the new data
        orderAdapter.setOrders(orders);
        orderAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.homemenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if (id == R.id.add) {
            // Handle add product action
            Intent addP = new Intent(MainActivity.this, addProducts.class);
            startActivity(addP);
            finish();

        } else if (id == R.id.upd) {
            // Handle update product action
            Intent updP = new Intent(MainActivity.this, updateProducts.class);
            startActivity(updP);
            finish();

        } else if (id == R.id.view) {
            // Handle view products action
            Intent viewdP = new Intent(MainActivity.this, viewProductsDetails.class);
            startActivity(viewdP);
            finish();
        }

        else if (id == R.id.viewIncome) {
            // Handle view products action
            Intent viewdI = new Intent(MainActivity.this, viewIncome.class);
            startActivity(viewdI);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(MainActivity.this, Login.class);
        startActivity(i);
    }

}
