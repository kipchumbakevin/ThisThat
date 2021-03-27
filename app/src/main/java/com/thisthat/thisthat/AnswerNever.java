package com.thisthat.thisthat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.thisthat.thisthat.models.GetUserModel;
import com.thisthat.thisthat.models.MessagesModel;
import com.thisthat.thisthat.models.NeverModel;
import com.thisthat.thisthat.networking.RetrofitClient;
import com.thisthat.thisthat.utils.Constants;
import com.thisthat.thisthat.utils.SharedPreferencesConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnswerNever extends AppCompatActivity {
    LinearLayoutCompat optionA,optionB,percentages,main;
    TextView title,a_percent,b_percent;
    String idd,ids,key;
    SharedPreferencesConfig sharedPreferencesConfig;
    ProgressBar progressBar;
    Button reload;
    int pickA,pickB,total,check,pp;
    CountDownTimer countDownTimer;
    ImageButton next,previous;
    private AdView adView,adV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_never);
        idd = getIntent().getExtras().getString("ID");
        ids = getIntent().getExtras().getString("ID");
        optionA = findViewById(R.id.optiona);
        optionB = findViewById(R.id.optionb);
        main = findViewById(R.id.mainView);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        percentages = findViewById(R.id.percentage);
        pp = 0;
        title = findViewById(R.id.nevertitle);
        a_percent = findViewById(R.id.optiona_percent);
        b_percent = findViewById(R.id.optionb_percent);
        progressBar = findViewById(R.id.progress);
        reload = findViewById(R.id.reload);
        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());

        AudienceNetworkAds.initialize(this);
        adView = new AdView(this, getString(R.string.banner), AdSize.BANNER_HEIGHT_50);
        adV = new AdView(this,getString(R.string.banner),AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        LinearLayout adC = (LinearLayout) findViewById(R.id.banner);

        // Add the ad view to your activity layout
        adContainer.addView(adView);
        adC.addView(adV);

        // Request an ad
        adView.loadAd();
        adV.loadAd();
        countDownTimer = new CountDownTimer(Constants.SECONDS,Constants.INTERVALS) {
            @Override
            public void onTick(long l) {
                main.setVisibility(View.GONE);
                title.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                main.setVisibility(View.VISIBLE);
                getSp();
            }
        }.start();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pp = 1;
                getSp2();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pp = 2;
                getSp3();
            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pp == 0) {
                    getSp();
                }else if (pp == 2){
                    getSp3();
                }else {
                    getSp2();
                }
            }
        });
        optionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key = Integer.toString(1);
                picked();
            }
        });
        optionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key = Integer.toString(2);
                picked();
            }
        });
    }
    private void picked() {
        String phone = sharedPreferencesConfig.readClientsPhone();
        String id = idd;
        progressBar.setVisibility(View.VISIBLE);
        Call<MessagesModel> call = RetrofitClient.getInstance(AnswerNever.this)
                .getApiConnector()
                .answerN(phone,key,id);
        call.enqueue(new Callback<MessagesModel>() {
            @Override
            public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code() == 201) {
                    Toast.makeText(AnswerNever.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    getSp();
                } else {
                    Toast.makeText(AnswerNever.this, "Server error,Please retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MessagesModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AnswerNever.this, "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void getSp() {
        String id = idd;
        main.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        Call<NeverModel> call = RetrofitClient.getInstance(AnswerNever.this)
                .getApiConnector()
                .getNS(id);
        call.enqueue(new Callback<NeverModel>() {
            @Override
            public void onResponse(Call<NeverModel> call, Response<NeverModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    title.setVisibility(View.VISIBLE);
                    title.setText(response.body().getTitle());
                    pickA = response.body().getPickA();
                    pickB = response.body().getPickB();
                    total = response.body().getTotal();
                    getUser();
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(AnswerNever.this, "Server error,retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<NeverModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(AnswerNever.this, "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getSp3() {
        int i = Integer.parseInt(ids)-1;
        String id = Integer.toString(i);
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        main.setVisibility(View.GONE);
        Call<NeverModel> call = RetrofitClient.getInstance(AnswerNever.this)
                .getApiConnector()
                .getNS(id);
        call.enqueue(new Callback<NeverModel>() {
            @Override
            public void onResponse(Call<NeverModel> call, Response<NeverModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    title.setVisibility(View.VISIBLE);
                    idd = Integer.toString(response.body().getId());
                    ids = Integer.toString(response.body().getId());
                    title.setText(response.body().getTitle());
                    pickA = response.body().getPickA();
                    pickB = response.body().getPickB();
                    total = response.body().getTotal();
                    getUser();
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(AnswerNever.this, "Server error,retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<NeverModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(AnswerNever.this, "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getSp2() {
        int i = Integer.parseInt(ids)+1;
        String id = Integer.toString(i);
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        main.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        Call<NeverModel> call = RetrofitClient.getInstance(AnswerNever.this)
                .getApiConnector()
                .getNS(id);
        call.enqueue(new Callback<NeverModel>() {
            @Override
            public void onResponse(Call<NeverModel> call, Response<NeverModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    idd = Integer.toString(response.body().getId());
                    ids = Integer.toString(response.body().getId());
                    title.setVisibility(View.VISIBLE);
                    title.setText(response.body().getTitle());
                    pickA = response.body().getPickA();
                    pickB = response.body().getPickB();
                    total = response.body().getTotal();
                    getUser();
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(AnswerNever.this, "Server error,retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<NeverModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(AnswerNever.this, "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getUser() {
        String phone = sharedPreferencesConfig.readClientsPhone();
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        previous.setVisibility(View.GONE);
        percentages.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        Call<GetUserModel> call = RetrofitClient.getInstance(AnswerNever.this)
                .getApiConnector()
                .getuser(phone);
        call.enqueue(new Callback<GetUserModel>() {
            @Override
            public void onResponse(Call<GetUserModel> call, Response<GetUserModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    check = response.body().getNever();
                    if (check >= Integer.parseInt(idd)){
                        main.setVisibility(View.VISIBLE);
                        title.setVisibility(View.VISIBLE);
                        double aaa = ((double) pickA/total)*100;
                        double bbb = ((double) pickB/total)*100;
                        int ra = (int)Math.rint(aaa);
                        int rb = (int)Math.rint(bbb);
                        optionA.setEnabled(false);
                        optionB.setEnabled(false);
                        a_percent.setText(ra+"%");
                        b_percent.setText(rb+"%");
                        percentages.setVisibility(View.VISIBLE);
                        if (Integer.parseInt(idd)<30) {
                            next.setVisibility(View.VISIBLE);
                        }
                        if (Integer.parseInt(idd)>1) {
                            previous.setVisibility(View.VISIBLE);
                        }
                        already();
                    }else if ((Integer.parseInt(idd)-check>1)){
                        optionA.setEnabled(false);
                        optionB.setEnabled(false);
                        if (Integer.parseInt(idd)>1) {
                            previous.setVisibility(View.VISIBLE);
                        }
                        AlertDialog.Builder al = new AlertDialog.Builder(AnswerNever.this);
                        al.setTitle("Previous")
                                .setMessage("Please participate in the previous scenario first in order to access this scenario.\nThank you")
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
                        if (Integer.parseInt(idd)>1) {
                            previous.setVisibility(View.VISIBLE);
                        }
                        main.setVisibility(View.VISIBLE);
                        title.setVisibility(View.VISIBLE);
                        optionA.setEnabled(true);
                        optionB.setEnabled(true);
                    }
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(AnswerNever.this, "Server error,retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetUserModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(AnswerNever.this, "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void already() {
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setTitle("Done!")
                .setMessage("Results keep changing\nYou have participated in this.\nBe sure to come back and check the results.")
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
    protected void onDestroy() {
        countDownTimer.cancel();
        if (adView != null){
            adView.destroy();
        }
        countDownTimer.cancel();
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        super.onBackPressed();

    }
}