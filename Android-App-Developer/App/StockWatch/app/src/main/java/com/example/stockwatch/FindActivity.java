package com.example.stockwatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class FindActivity extends AppCompatActivity {
    private EditText company;
    private EditText symbol;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        company = findViewById(R.id.companyName);
        symbol = findViewById(R.id.symbol);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //========================HELPER•METHOD===================================\\
    public void doFind(View v){
        HashMap<String, String> values = new HashMap<>();
        if (!company.getText().toString().trim().isEmpty()){
            values.put("COMPANY", company.getText().toString().trim());
        }
        if (!symbol.getText().toString().trim().isEmpty()){
            values.put("SYMBOL", symbol.getText().toString().trim());
        }
        Intent intent = new Intent();
        intent.putExtra("FIND", values);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }





    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }
}
