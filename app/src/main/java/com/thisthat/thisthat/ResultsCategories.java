package com.thisthat.thisthat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ResultsCategories extends AppCompatActivity {

    Button lif,food,celebs,partner;
    String friend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_categories);
        friend = getIntent().getExtras().getString("FRIEND");
        lif = findViewById(R.id.life);
        food = findViewById(R.id.food);
        celebs = findViewById(R.id.celebs);
        partner = findViewById(R.id.partner);

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
}