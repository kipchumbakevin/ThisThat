package com.thisthat.thisthat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.thisthat.thisthat.utils.Constants;

public class MainActivity extends AppCompatActivity {
    Button scenario,friend,self,check;
    ProgressBar progressBar;
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scenario = findViewById(R.id.scenarios);
        friend = findViewById(R.id.friend);
        self = findViewById(R.id.self);
        check = findViewById(R.id.check);
        progressBar = findViewById(R.id.progress);

        countDownTimer = new CountDownTimer(Constants.SECONDS,Constants.INTERVALS) {
            @Override
            public void onTick(long l) {
                progressBar.setVisibility(View.VISIBLE);
                scenario.setVisibility(View.GONE);
                friend.setVisibility(View.GONE);
                self.setVisibility(View.GONE);
                check.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                progressBar.setVisibility(View.GONE);
                scenario.setVisibility(View.VISIBLE);
                friend.setVisibility(View.VISIBLE);
                self.setVisibility(View.VISIBLE);
                check.setVisibility(View.VISIBLE);
            }
        }.start();
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CheckOptions.class);
                startActivity(intent);
            }
        });

        self.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ThemCategories.class);
                intent.putExtra("FRIEND",Integer.toString(0));
                startActivity(intent);
            }
        });
        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,PhoneNumber.class);
                startActivity(intent);
            }
        });
        scenario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Scenarios.class);
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