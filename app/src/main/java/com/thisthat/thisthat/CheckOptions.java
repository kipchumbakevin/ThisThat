package com.thisthat.thisthat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CheckOptions extends AppCompatActivity {

    Button evaluatee,evaluator;
    int allowed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_options);
        evaluatee = findViewById(R.id.evaluatee);
        evaluator = findViewById(R.id.evaluator);
        allowed = 0;

        checkPermission();
        evaluatee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allowed ==1) {
                    Intent intent = new Intent(CheckOptions.this, ResultsCategories.class);
                    intent.putExtra("FRIEND", Integer.toString(1));
                    startActivity(intent);
                }else {
                    checkPermission();
                }
            }
        });
        evaluator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allowed == 1) {
                    Intent intent = new Intent(CheckOptions.this, ResultsCategories.class);
                    intent.putExtra("FRIEND", Integer.toString(2));
                    startActivity(intent);
                }else {
                    checkPermission();
                }
            }
        });
    }
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(CheckOptions.this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            //request permission
            ActivityCompat.requestPermissions(CheckOptions.this,
                    new String[]{Manifest.permission.READ_CONTACTS},100);
        }else {
            //permission granted
            allowed = 1;
        }
    }
}