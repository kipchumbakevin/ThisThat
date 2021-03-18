package com.thisthat.thisthat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thisthat.thisthat.models.GetUserModel;
import com.thisthat.thisthat.models.MessagesModel;
import com.thisthat.thisthat.models.WouldYouRatherModel;
import com.thisthat.thisthat.networking.RetrofitClient;
import com.thisthat.thisthat.utils.Constants;
import com.thisthat.thisthat.utils.SharedPreferencesConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WouldYou extends AppCompatActivity {
    LinearLayoutCompat optionA,optionB,percentages,mainview;
    TextView a,b,a_percent,b_percent;
    ProgressBar progressBar;
    Button reload;
    SharedPreferencesConfig sharedPreferencesConfig;
    String id;
    int ch,pickA,pickB,total,idd;
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_would_you);
        optionA = findViewById(R.id.optiona);
        optionB = findViewById(R.id.optionb);
        a = findViewById(R.id.a);
        mainview = findViewById(R.id.mainView);
        a_percent = findViewById(R.id.optiona_percent);
        b_percent = findViewById(R.id.optionb_percent);
        b = findViewById(R.id.b);
        percentages = findViewById(R.id.percentage);
        progressBar = findViewById(R.id.progress);
        reload = findViewById(R.id.reload);
        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());
        idd = Integer.parseInt(getIntent().getExtras().getString("ID"));
        id = Integer.toString(idd);

        countDownTimer = new CountDownTimer(Constants.SECONDS,Constants.INTERVALS) {
            @Override
            public void onTick(long l) {
                mainview.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                mainview.setVisibility(View.VISIBLE);
                getSpecific();
            }
        }.start();
        optionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickedA();
            }
        });
        optionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickedB();
            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    getSpecific();
            }
        });
    }

    private void getSpecific() {
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        optionA.setVisibility(View.GONE);
        optionB.setVisibility(View.GONE);
        Call<WouldYouRatherModel> call = RetrofitClient.getInstance(WouldYou.this)
                .getApiConnector()
                .speci(id);
        call.enqueue(new Callback<WouldYouRatherModel>() {
            @Override
            public void onResponse(Call<WouldYouRatherModel> call, Response<WouldYouRatherModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    pickA = response.body().getPickA();
                    pickB = response.body().getPickB();
                    total = response.body().getTotal();
                    a.setText(response.body().getOptionA());
                    b.setText(response.body().getOptionB());
                    getUser();
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<WouldYouRatherModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void pickedB() {
        ch = 2;
        String key = Integer.toString(2);
        String phone = sharedPreferencesConfig.readClientsPhone();
        progressBar.setVisibility(View.VISIBLE);
        Call<MessagesModel> call = RetrofitClient.getInstance(WouldYou.this)
                .getApiConnector()
                .answer(phone,key,id);
        call.enqueue(new Callback<MessagesModel>() {
            @Override
            public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code() == 201) {
                    getSpecific();
                } else {
                    Toast.makeText(getApplicationContext(), "Server error,please retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MessagesModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void pickedA() {
        ch = 1;
        String key = Integer.toString(1);
        String phone = sharedPreferencesConfig.readClientsPhone();
        progressBar.setVisibility(View.VISIBLE);
        Call<MessagesModel> call = RetrofitClient.getInstance(WouldYou.this)
                .getApiConnector()
                .answer(phone,key,id);
        call.enqueue(new Callback<MessagesModel>() {
            @Override
            public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code() == 201) {
                    getSpecific();
                } else {
                    Toast.makeText(getApplicationContext(), "Server error,Please retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MessagesModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getUser() {
        String phone = sharedPreferencesConfig.readClientsPhone();
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        Call<GetUserModel> call = RetrofitClient.getInstance(WouldYou.this)
                .getApiConnector()
                .getuser(phone);
        call.enqueue(new Callback<GetUserModel>() {
            @Override
            public void onResponse(Call<GetUserModel> call, Response<GetUserModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    optionA.setVisibility(View.VISIBLE);
                    optionB.setVisibility(View.VISIBLE);
                    if (response.body().getWould() >= Integer.parseInt(id)){
                        double aaa = ((double) pickA/total)*100;
                        double bbb = ((double) pickB/total)*100;
                        int ra = (int)Math.rint(aaa);
                        int rb = (int)Math.rint(bbb);
                        optionA.setEnabled(false);
                        optionB.setEnabled(false);
                        a_percent.setText(ra+"%");
                        b_percent.setText(rb+"%");
                        percentages.setVisibility(View.VISIBLE);
                        already();
                    }else if ((Integer.parseInt(id)-response.body().getWould())>1){
                        optionA.setVisibility(View.GONE);
                        optionB.setVisibility(View.GONE);
                        AlertDialog.Builder al = new AlertDialog.Builder(WouldYou.this);
                        al.setTitle("Previous")
                                .setMessage("Please participate in scenario "+(Integer.parseInt(id)-1)+" first in order to access this scenario.\nThank you")
                                .setPositiveButton("Cool", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                        AlertDialog alertDialog = al.create();
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }else {
                        optionA.setVisibility(View.VISIBLE);
                        optionB.setVisibility(View.VISIBLE);
                    }
                } else {
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


    private void already() {
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setTitle("Done!")
                .setMessage("Results keep changing\nYou have already participated in this.\nBe sure to come back and check the results.")
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

    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        super.onBackPressed();
    }
}