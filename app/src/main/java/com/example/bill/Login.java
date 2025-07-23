package com.example.bill;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Login extends AppCompatActivity {

//    {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        boolean isLogin = prefs.getBoolean("isLogin", false);
//
//        if(isLogin) {
//            Intent i = new Intent(Login.this, MainActivity.class);
//            startActivity(i);
//            finish();
//        }
//    }

    private EditText user, pass;
    private Button log, sign;
    private SQLiteDatabase db;
    private TextView backbtn;
    private ProductDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = findViewById(R.id.usernameEditText);
        pass = findViewById(R.id.passwordEditText);
        log = findViewById(R.id.loginButton);
        backbtn = findViewById(R.id.back);
        sign = findViewById(R.id.goSignup);

        dataSource = new ProductDataSource(this);
        dataSource.open();

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launch2 = new Intent(Login.this, SignUp.class);
                startActivity(launch2);
                finish();
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = user.getText().toString();
                String password = pass.getText().toString();
                if (dataSource.logUser(username, password)){
                    Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();
                    Intent launch2 = new Intent(Login.this, MainActivity.class);
                    startActivity(launch2);
                    finish();
                }
                else {
                    Toast.makeText(Login.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launch1 = new Intent(Login.this, client_page.class);
                startActivity(launch1);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(Login.this, client_page.class);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }

}