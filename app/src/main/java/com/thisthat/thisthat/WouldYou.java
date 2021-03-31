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

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
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
    String idd,ids;
    int ch,pickA,pickB,total,pp;
    CountDownTimer countDownTimer;
    ImageButton next,previous;
    private AdView adView,adV;
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
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        percentages = findViewById(R.id.percentage);
        pp = 0;
        progressBar = findViewById(R.id.progress);
        reload = findViewById(R.id.reload);
        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());
        idd = getIntent().getExtras().getString("ID");
        ids = getIntent().getExtras().getString("ID");

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
//        interstitialAd = new InterstitialAd(this, getString(R.string.interstitial));
//        interstitialAdListener = new InterstitialAdListener() {
//            @Override
//            public void onInterstitialDisplayed(Ad ad) {
//                // Interstitial ad displayed callback
//                //  Log.e(TAG, "Interstitial ad displayed.");
//            }
//
//            @Override
//            public void onInterstitialDismissed(Ad ad) {
//                onBackPressed();
//
//            }
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                // Ad error callback
//                //Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                // Interstitial ad is loaded and ready to be displayed
//                // Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
//                // Show the ad
//                //interstitialAd.show();
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//                // Ad clicked callback
//                // Log.d(TAG, "Interstitial ad clicked!");
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//                // Ad impression logged callback
//                // Log.d(TAG, "Interstitial ad impression logged!");
//            }
//        };
//        interstitialAd.loadAd(
//                interstitialAd.buildLoadAdConfig()
//                        .withAdListener(interstitialAdListener)
//                        .build());
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
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pp = 1;
                getSpecificN();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pp = 2;
               getSpecificP();
            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pp == 1){
                    getSpecificN();
                }else if (pp == 2){
                    getSpecificP();
                }else {
                    getSpecific();
                }
            }
        });
    }

    private void getSpecific() {
        progressBar.setVisibility(View.VISIBLE);
        String id = idd;
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
    private void getSpecificN() {
        progressBar.setVisibility(View.VISIBLE);
        int i = Integer.parseInt(ids)+1;
        String id = Integer.toString(i);
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
                    idd = Integer.toString(response.body().getId());
                    ids = Integer.toString(response.body().getId());
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
    private void getSpecificP() {
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        int i = Integer.parseInt(ids)-1;
        String id = Integer.toString(i);
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
                    idd = Integer.toString(response.body().getId());
                    ids = Integer.toString(response.body().getId());
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
        String id = idd;
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
        String id = idd;
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
        percentages.setVisibility(View.GONE);
        reload.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        previous.setVisibility(View.GONE);
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
                    if (response.body().getWould() >= Integer.parseInt(idd)){
                        double aaa = ((double) pickA/total)*100;
                        double bbb = ((double) pickB/total)*100;
                        int ra = (int)Math.rint(aaa);
                        int rb = (int)Math.rint(bbb);
                        optionA.setEnabled(false);
                        optionB.setEnabled(false);
                        a_percent.setText(ra+"%");
                        b_percent.setText(rb+"%");
                        percentages.setVisibility(View.VISIBLE);
                        if (Integer.parseInt(idd)<54) {
                            next.setVisibility(View.VISIBLE);
                        }
                        if (Integer.parseInt(idd)>1) {
                            previous.setVisibility(View.VISIBLE);
                        }
                        already();
                    }else if ((Integer.parseInt(idd)-response.body().getWould())>1){
                        optionA.setVisibility(View.GONE);
                        optionB.setVisibility(View.GONE);
                        if (Integer.parseInt(idd)>1) {
                            previous.setVisibility(View.VISIBLE);
                        }
                        AlertDialog.Builder al = new AlertDialog.Builder(WouldYou.this);
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
                    }else {
                        if (Integer.parseInt(idd)>1) {
                            previous.setVisibility(View.VISIBLE);
                        }
                        optionA.setEnabled(true);
                        optionB.setEnabled(true);
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