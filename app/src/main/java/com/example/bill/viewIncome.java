package com.example.bill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class viewIncome extends AppCompatActivity {

    private Button calculateIncomeButton;
    private Spinner spn1, spn2, spn3;
    private EditText edit1;
    private ProductDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_income);

        dataSource = new ProductDataSource(this);
        dataSource.open();

        edit1 = findViewById(R.id.selectedDate);
        spn1 = findViewById(R.id.dte);
        spn2 = findViewById(R.id.dte1);
        spn3 = findViewById(R.id.dte2);

        // Days
        List<String> days = new ArrayList<>();
        days.add("Select Day");
        for (int i = 01; i <= 31; i++) {
            days.add(String.valueOf(i));
        }
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn1.setAdapter(dayAdapter);

        // Months
        List<String> months = new ArrayList<>();
        months.add("Select Month");
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (String month : monthNames) {
            months.add(month);
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn2.setAdapter(monthAdapter);

        // Years
        List<String> years = new ArrayList<>();
        years.add("Select Year");
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1900; i <= currentYear; i++) {
            years.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn3.setAdapter(yearAdapter);

        // Spinner Selection Listener
        AdapterView.OnItemSelectedListener dateListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDay = spn1.getSelectedItem().toString();
                String selectedMonth = spn2.getSelectedItem().toString();
                String selectedYear = spn3.getSelectedItem().toString();

                if ("Select Day".equals(selectedDay) || "Select Month".equals(selectedMonth) || "Select Year".equals(selectedYear)) {
                    edit1.setText("");
                } else {
                    int mnth = 0;

                    switch (selectedMonth) {
                        case "Jan":
                            mnth = 01;
                            break;
                        case "Feb":
                            mnth = 02;
                            break;
                        case "Mar":
                            mnth = 03;
                            break;
                        case "Apr":
                            mnth = 04;
                            break;
                        case "May":
                            mnth = 05;
                            break;
                        case "Jun":
                            mnth = 06;
                            break;
                        case "Jul":
                            mnth = 07;
                            break;
                        case "Aug":
                            mnth = 8;
                            break;
                        case "Sep":
                            mnth = 9;
                            break;
                        case "Oct":
                            mnth = 10;
                            break;
                        case "Nov":
                            mnth = 11;
                            break;
                        case "Dec":
                            mnth = 12;
                            break;
                        default:
                            mnth = 0;

                    }

                    if (mnth != 0) {
                        String formattedDate = String.format("%02d-%02d-%s", Integer.parseInt(selectedDay), mnth, selectedYear);
                        edit1.setText(formattedDate);
                    } else {
                        Toast.makeText(viewIncome.this, "Selct Date Please...", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        };

        spn1.setOnItemSelectedListener(dateListener);
        spn2.setOnItemSelectedListener(dateListener);
        spn3.setOnItemSelectedListener(dateListener);

        // Button Click Listener
        calculateIncomeButton = findViewById(R.id.calculateIncomeButton);
        calculateIncomeButton.setOnClickListener(v -> calculateIncome());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(viewIncome.this, MainActivity.class);
        startActivity(i);
    }

    private void calculateIncome() {
        String selectedDate = edit1.getText().toString();

        if (selectedDate.isEmpty()) {
            Toast.makeText(viewIncome.this, "Please Enter Date ...", Toast.LENGTH_SHORT).show();
            return;
        }

        double[] pricesArray = dataSource.getAllPricesArray(selectedDate);

        double totalIncome = 0.0;
        for (double price : pricesArray) {
            totalIncome += price;
        }

        TextView total = findViewById(R.id.totalIncomeTextView);

        boolean exists = dataSource.doesDate(selectedDate);
        if (exists) {
            total.setText("Total Income of That Date : \n\n\t RS. \t" + totalIncome);
        } else {
            total.setText("This Date is not Exists...");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}
