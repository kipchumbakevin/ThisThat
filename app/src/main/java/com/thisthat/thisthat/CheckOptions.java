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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.thisthat.thisthat.utils.Constants;

public class CheckOptions extends AppCompatActivity {

    Button evaluatee,evaluator;
    int allowed;
    CountDownTimer countDownTimer;
    ProgressBar progressBar;
    private AdView adView,adV;
    private InterstitialAd interstitialAd;
    InterstitialAdListener interstitialAdListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_options);
        evaluatee = findViewById(R.id.evaluatee);
        evaluator = findViewById(R.id.evaluator);
        progressBar = findViewById(R.id.progress);
        allowed = 0;

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
        interstitialAd = new InterstitialAd(this, getString(R.string.interstitial));
        interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                //  Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                onBackPressed();

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                //Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                // Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                //interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                // Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                // Log.d(TAG, "Interstitial ad impression logged!");
            }
        };
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
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
    protected void onDestroy() {
        countDownTimer.cancel();
        if (adView != null){
            adView.destroy();
        }
        if (interstitialAd != null){
            interstitialAd.destroy();
        }
        countDownTimer.cancel();
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        if (interstitialAd.isAdLoaded()){
            interstitialAd.show();
        }else {
            super.onBackPressed();
        }
    }
}