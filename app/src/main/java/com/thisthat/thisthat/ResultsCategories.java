package com.thisthat.thisthat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.thisthat.thisthat.utils.Constants;

public class ResultsCategories extends AppCompatActivity {

    Button lif,food,celebs,partner;
    String friend;
    ConstraintLayout constraintLayout;
    CountDownTimer countDownTimer;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_categories);
        friend = getIntent().getExtras().getString("FRIEND");
        lif = findViewById(R.id.life);
        food = findViewById(R.id.food);
        progressBar = findViewById(R.id.progress);
        constraintLayout = findViewById(R.id.constraint);
        celebs = findViewById(R.id.celebs);
        partner = findViewById(R.id.partner);

        countDownTimer = new CountDownTimer(Constants.SECONDS,Constants.INTERVALS) {
            @Override
            public void onTick(long l) {
                progressBar.setVisibility(View.VISIBLE);
                constraintLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                progressBar.setVisibility(View.GONE);
                constraintLayout.setVisibility(View.VISIBLE);
            }
        }.start();
        lif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultsCategories.this,EvaluatedMeThem.class);
                intent.putExtra("ME",Integer.toString(1));
                intent.putExtra("FRIEND",friend);
                startActivity(intent);
            }
        });
        partner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultsCategories.this,EvaluatedMeThem.class);
                intent.putExtra("ME",Integer.toString(4));
                intent.putExtra("FRIEND",friend);
                startActivity(intent);
            }
        });
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultsCategories.this,EvaluatedMeThem.class);
                intent.putExtra("ME",Integer.toString(2));
                intent.putExtra("FRIEND",friend);
                startActivity(intent);
            }
        });
        celebs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultsCategories.this,EvaluatedMeThem.class);
                intent.putExtra("ME",Integer.toString(3));
                intent.putExtra("FRIEND",friend);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        super.onBackPressed();
    }
}