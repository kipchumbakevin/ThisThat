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
import com.thisthat.thisthat.networking.RetrofitClient;
import com.thisthat.thisthat.utils.Constants;
import com.thisthat.thisthat.utils.SharedPreferencesConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemCategories extends AppCompatActivity {
    Button celebs,lifestyle,food,partner,reload,invite;
    SharedPreferencesConfig sharedPreferencesConfig;
    int self,ena,ff;
    TextView title;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    private AdView adView,adV;
    private InterstitialAd interstitialAd;
    InterstitialAdListener interstitialAdListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_categories);
        celebs = findViewById(R.id.celebs);
        lifestyle = findViewById(R.id.life);
        food = findViewById(R.id.food);
        partner = findViewById(R.id.partner);
        invite = findViewById(R.id.invite);
        reload = findViewById(R.id.reload);
        progressBar = findViewById(R.id.progress);
        title = findViewById(R.id.title);
        constraintLayout = findViewById(R.id.constraint);
        ena = 0;
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
//               onBackPressed();
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
        self = Integer.parseInt(getIntent().getExtras().getString("FRIEND"));

                if (self == 0){
                    //self
                    title.setText("Self evaluation");
                    getUser();
                }else if (self == 1){
                    //friend
                    title.setText("Evaluate your friend");
                    invite.setVisibility(View.GONE);
                    getFriend();
                }

                invite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ena >= 2){
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            String shareBody = "You are missing out the fun.I have already evaluated myself.Lets evaluate each other and see the results. Join and evaluate me now\n" +
                                    "Download ThisThat App now at https://play.google.com/store/apps/details?id=" + ThemCategories.this.getPackageName();
                            intent.putExtra(Intent.EXTRA_SUBJECT,ThemCategories.this.getString(R.string.app_name));
                            intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                            startActivity(Intent.createChooser(intent, "Share via"));
                        }else {
                            AlertDialog.Builder all = new AlertDialog.Builder(ThemCategories.this);
                            all.setMessage("To be evaluated by your friends, you need to finish evaluating yourself in at least one category\nFor more fun, evaluate yourself in all categories and invite your friends")
                                    .setPositiveButton("Cool", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                            AlertDialog alertDialog = all.create();
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }
                    }
                });
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
                        finish();
                    }
                }else if (self == 1){
                    Intent intent = new Intent(ThemCategories.this, LifeFoodPatner.class);
                    intent.putExtra("ME", Integer.toString(1));
                    intent.putExtra("FRIEND", Integer.toString(self));
                    startActivity(intent);
                    finish();
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
                        finish();
                    }
                }else if (self == 1){
                    Intent intent = new Intent(ThemCategories.this, LifeFoodPatner.class);
                    intent.putExtra("ME", Integer.toString(2));
                    intent.putExtra("FRIEND", Integer.toString(self));
                    startActivity(intent);
                    finish();
                }
            }
        });
        partner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (self == 0) {
                    if (ena < 4) {
                        Toast.makeText(ThemCategories.this, "Please complete the celebrity section first", Toast.LENGTH_SHORT).show();
                    } else if (ena >= 4){
                        Intent intent = new Intent(ThemCategories.this, LifeFoodPatner.class);
                        intent.putExtra("ME", Integer.toString(3));
                        intent.putExtra("FRIEND", Integer.toString(self));
                        startActivity(intent);
                        finish();
                    }
                }else if (self == 1){
                    Intent intent = new Intent(ThemCategories.this, LifeFoodPatner.class);
                    intent.putExtra("ME", Integer.toString(3));
                    intent.putExtra("FRIEND", Integer.toString(self));
                    startActivity(intent);
                    finish();
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
                        finish();
                    }
                }else if (self == 1){
                    Intent intent = new Intent(ThemCategories.this, AllCelebsActivity.class);
                    intent.putExtra("FRIEND", Integer.toString(self));
                    startActivity(intent);
                    finish();
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

                    }else if (response.body().getComplete() >= 4){
                        ena = 4;
                    }
                    AlertDialog.Builder al = new AlertDialog.Builder(ThemCategories.this);
                    al.setTitle("Info")
                            .setMessage("When evaluating yourself,choose what you like or what best describes you or what you love\nTo be evaluated by your friend,you have to complete each category.\nHave fun!")
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
                        partner.setVisibility(View.VISIBLE);
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
    protected void onDestroy() {
        if (adView != null){
            adView.destroy();
        }
        if (interstitialAd != null){
            interstitialAd.destroy();
        }
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}