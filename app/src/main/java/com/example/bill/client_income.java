package com.example.bill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.text.DateFormat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputEditText;


public class client_income extends AppCompatActivity {

    private Button calculateIncomeButton;
    private EditText edit1;
    private ProductDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_income);

        dataSource = new ProductDataSource(this);
        dataSource.open();

        edit1 = findViewById(R.id.clientselectedDate);
        calculateIncomeButton = findViewById(R.id.clientcalculateIncomeButton);

        calculateIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calculateIncome();
            }
        });

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(client_income.this, client_page.class);
        startActivity(i);
    }

    private void calculateIncome() {
        String selectedDate = edit1.getText().toString();

        if (selectedDate.isEmpty()){
            Toast.makeText(client_income.this, "Please Enter Date ...", Toast.LENGTH_SHORT).show();
            return;
        }

        double[] pricesArray = dataSource.getAllPricesArray(selectedDate);

        double totalIncome = 0.0;
        for(double price : pricesArray){
            totalIncome += price;
        }

        TextView total = findViewById(R.id.clienttotalIncomeTextView);

        boolean Exists = dataSource.doesDate(selectedDate);
        if (Exists){
            total.setText("Total Income of That Date : \n\n\t RS. \t" + totalIncome);
        } else {
            total.setText("This Date is not Exists... ");
            return;
        }
    }

    //Backup

    //BackUp Complete

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}
