package com.example.bill;

import static com.example.bill.DBHelper.COLUMN_LOG_NAME;
import static com.example.bill.DBHelper.COLUMN_LOG_PASS;
import static com.example.bill.DBHelper.TABLE_ADMIN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    private EditText userr, passs;
    private Button logg;
    private ProductDataSource dataSource;
    private TextView backbtnn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userr = findViewById(R.id.UserNAme);
        passs = findViewById(R.id.PassWOrd);
        logg = findViewById(R.id.signButton);

        dataSource = new ProductDataSource(this);
        dataSource.open();

        logg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = userr.getText().toString();
                String pass = passs.getText().toString();

                addUser(name, pass);
            }
        });

        backbtnn = findViewById(R.id.logback);
        backbtnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launch1 = new Intent(SignUp.this, Login.class);
                startActivity(launch1);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(SignUp.this, client_page.class);
        startActivity(i);
    }

    private void addUser(String name, String pass) {

        if(name.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "Please Fill All fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean Exists = dataSource.doesAdmin(name);
        if (Exists){
            Toast.makeText(this, "This UserName Already Exists...", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new product

        Admin newAdmin = new Admin();
        newAdmin.setNNamee(name);
        newAdmin.setPPass(pass);

        // Insert the new product into the database
        long insertId = dataSource.insertAdmin(newAdmin);

        if (insertId != -1) {
            Toast.makeText(this, "User Data added successfully", Toast.LENGTH_SHORT).show();
            clearInputFields();
        } else {
            Toast.makeText(this, "Failed to Add User", Toast.LENGTH_SHORT).show();
        }

    }

    private void clearInputFields() {
        userr.setText("");
        passs.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }

}