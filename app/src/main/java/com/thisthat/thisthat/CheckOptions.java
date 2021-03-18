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
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.thisthat.thisthat.utils.Constants;

public class CheckOptions extends AppCompatActivity {

    Button evaluatee,evaluator;
    int allowed;
    CountDownTimer countDownTimer;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_options);
        evaluatee = findViewById(R.id.evaluatee);
        evaluator = findViewById(R.id.evaluator);
        progressBar = findViewById(R.id.progress);
        allowed = 0;

        countDownTimer = new CountDownTimer(Constants.SECONDS,Constants.INTERVALS) {
            @Override
            public void onTick(long l) {
                progressBar.setVisibility(View.VISIBLE);
                evaluatee.setVisibility(View.GONE);
                evaluator.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                checkPermission();
                progressBar.setVisibility(View.GONE);
                evaluator.setVisibility(View.VISIBLE);
                evaluatee.setVisibility(View.VISIBLE);
            }
        }.start();
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

    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        super.onBackPressed();
    }
}