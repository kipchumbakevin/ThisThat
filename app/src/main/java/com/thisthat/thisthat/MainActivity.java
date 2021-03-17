package com.thisthat.thisthat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    Button scenario,friend,self,check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scenario = findViewById(R.id.scenarios);
        friend = findViewById(R.id.friend);
        self = findViewById(R.id.self);
        check = findViewById(R.id.check);

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
}