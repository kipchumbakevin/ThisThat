package com.thisthat.thisthat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thisthat.thisthat.models.GetUserModel;
import com.thisthat.thisthat.networking.RetrofitClient;
import com.thisthat.thisthat.utils.Constants;
import com.thisthat.thisthat.utils.SharedPreferencesConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemCategories extends AppCompatActivity {
    Button celebs,lifestyle,food,partner,reload;
    SharedPreferencesConfig sharedPreferencesConfig;
    int self,ena,ff;
    TextView title;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_categories);
        celebs = findViewById(R.id.celebs);
        lifestyle = findViewById(R.id.life);
        food = findViewById(R.id.food);
        partner = findViewById(R.id.partner);
        reload = findViewById(R.id.reload);
        progressBar = findViewById(R.id.progress);
        title = findViewById(R.id.title);
        constraintLayout = findViewById(R.id.constraint);
        ena = 0;
        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());
        self = Integer.parseInt(getIntent().getExtras().getString("FRIEND"));
        countDownTimer = new CountDownTimer(Constants.SECONDS,Constants.INTERVALS) {
            @Override
            public void onTick(long l) {
                progressBar.setVisibility(View.VISIBLE);
                constraintLayout.setVisibility(View.GONE);

            }

            @Override
            public void onFinish() {
                if (self == 0){
                    //self
                    title.setText("Self evaluation");
                    getUser();
                }else if (self == 1){
                    //friend
                    title.setText("Evaluate your friend");
                    getFriend();
                }
            }
        }.start();


        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (self == 0) {
                    getUser();
                }else if (self == 1){
                    getFriend();
                }
            }
        });
        lifestyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (self == 0) {
                    if (ena >= 0) {
                        Intent intent = new Intent(ThemCategories.this, LifeFoodPatner.class);
                        intent.putExtra("ME", Integer.toString(1));
                        intent.putExtra("FRIEND", Integer.toString(self));
                        startActivity(intent);
                    }
                }else if (self == 1){
                    Intent intent = new Intent(ThemCategories.this, LifeFoodPatner.class);
                    intent.putExtra("ME", Integer.toString(1));
                    intent.putExtra("FRIEND", Integer.toString(self));
                    startActivity(intent);
                }
            }
        });
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (self == 0) {
                    if (ena < 2) {
                        Toast.makeText(ThemCategories.this, "Please complete the lifestyle section first", Toast.LENGTH_SHORT).show();
                    } else if (ena >= 2){
                        Intent intent = new Intent(ThemCategories.this, LifeFoodPatner.class);
                        intent.putExtra("ME", Integer.toString(2));
                        intent.putExtra("FRIEND", Integer.toString(self));
                        startActivity(intent);
                    }
                }else if (self == 1){
                    Intent intent = new Intent(ThemCategories.this, LifeFoodPatner.class);
                    intent.putExtra("ME", Integer.toString(2));
                    intent.putExtra("FRIEND", Integer.toString(self));
                    startActivity(intent);
                }
            }
        });
        partner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (self == 0) {
                    if (ena < 4) {
                        Toast.makeText(ThemCategories.this, "Please complete the celebrity section first", Toast.LENGTH_SHORT).show();
                    } else if (ena == 4){
                        Intent intent = new Intent(ThemCategories.this, LifeFoodPatner.class);
                        intent.putExtra("ME", Integer.toString(3));
                        intent.putExtra("FRIEND", Integer.toString(self));
                        startActivity(intent);
                    }
                }else if (self == 1){
                    Intent intent = new Intent(ThemCategories.this, LifeFoodPatner.class);
                    intent.putExtra("ME", Integer.toString(3));
                    intent.putExtra("FRIEND", Integer.toString(self));
                    startActivity(intent);
                }
            }
        });
        celebs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (self == 0) {
                    if (ena < 3) {
                        Toast.makeText(ThemCategories.this, "Please complete the food section first", Toast.LENGTH_SHORT).show();
                    } else if (ena >= 3){
                        Intent intent = new Intent(ThemCategories.this, AllCelebsActivity.class);
                        intent.putExtra("FRIEND", Integer.toString(self));
                        startActivity(intent);
                    }
                }else if (self == 1){
                    Intent intent = new Intent(ThemCategories.this, AllCelebsActivity.class);
                    intent.putExtra("FRIEND", Integer.toString(self));
                    startActivity(intent);
                }
            }
        });
    }
    private void getUser() {
        String phone = sharedPreferencesConfig.readClientsPhone();
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        constraintLayout.setVisibility(View.GONE);
        Call<GetUserModel> call = RetrofitClient.getInstance(ThemCategories.this)
                .getApiConnector()
                .getuser(phone);
        call.enqueue(new Callback<GetUserModel>() {
            @Override
            public void onResponse(Call<GetUserModel> call, Response<GetUserModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    constraintLayout.setVisibility(View.VISIBLE);
                    if (response.body().getComplete() == 0){
                        ena = 0;
                    }else if (response.body().getComplete() == 2){
                        ena = 2;

                    }else if (response.body().getComplete() == 3){
                        ena = 3;

                    }else if (response.body().getComplete() == 4){
                        ena = 4;
                    }
                    AlertDialog.Builder al = new AlertDialog.Builder(ThemCategories.this);
                    al.setTitle("Info")
                            .setMessage("When evaluating yourself,choose what you like or what best describes you or what you love")
                            .setPositiveButton("Cool", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    AlertDialog alertDialog = al.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
                else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error,retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetUserModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void getFriend() {
        String phone = sharedPreferencesConfig.readFriendsPhone();
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        lifestyle.setVisibility(View.GONE);
        food.setVisibility(View.GONE);
        partner.setVisibility(View.GONE);
        celebs.setVisibility(View.GONE);
        constraintLayout.setVisibility(View.GONE);
        Call<GetUserModel> call = RetrofitClient.getInstance(ThemCategories.this)
                .getApiConnector()
                .getuser(phone);
        call.enqueue(new Callback<GetUserModel>() {
            @Override
            public void onResponse(Call<GetUserModel> call, Response<GetUserModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    constraintLayout.setVisibility(View.VISIBLE);
                    if (response.body().getComplete() == 2){
                        lifestyle.setVisibility(View.VISIBLE);
                    }else if (response.body().getComplete() == 3){
                        lifestyle.setVisibility(View.VISIBLE);
                        food.setVisibility(View.VISIBLE);

                    }else if (response.body().getComplete() == 4){
                        lifestyle.setVisibility(View.VISIBLE);
                        food.setVisibility(View.VISIBLE);
                        celebs.setVisibility(View.VISIBLE);

                    }else if (response.body().getComplete() == 5){
                        lifestyle.setVisibility(View.VISIBLE);
                        food.setVisibility(View.VISIBLE);
                        partner.setVisibility(View.GONE);
                        celebs.setVisibility(View.VISIBLE);
                    }
                    AlertDialog.Builder al = new AlertDialog.Builder(ThemCategories.this);
                    al.setTitle("Info")
                            .setMessage("You will only be able to view the categories in which your friend completed the evaluation.\nWhen evaluating your friend,choose what you think they like or what best describes them or what you think they love")
                            .setPositiveButton("Cool", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    AlertDialog alertDialog = al.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
                else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error,retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetUserModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        super.onBackPressed();
    }
}