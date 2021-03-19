package com.thisthat.thisthat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.thisthat.thisthat.adapters.AllCelebsAdapter;
import com.thisthat.thisthat.adapters.FriendCelebAdapter;
import com.thisthat.thisthat.models.AllCelebsModel;
import com.thisthat.thisthat.models.WouldYouRatherModel;
import com.thisthat.thisthat.networking.RetrofitClient;
import com.thisthat.thisthat.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCelebsActivity extends AppCompatActivity {
    AllCelebsAdapter allCelebsAdapter;
    FriendCelebAdapter friendCelebAdapter;
    RecyclerView recyclerView;
    private final ArrayList<AllCelebsModel> mArrayList = new ArrayList<>();
    ProgressBar progressBar;
    Button reload;
    CountDownTimer countDownTimer;
    int friend;
    private AdView adView,adV;
    private InterstitialAd interstitialAd;
    InterstitialAdListener interstitialAdListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_celebs);
        progressBar = findViewById(R.id.progress);
        reload = findViewById(R.id.reload);
        friend = Integer.parseInt(getIntent().getExtras().getString("FRIEND"));
        allCelebsAdapter = new AllCelebsAdapter(getApplicationContext(),mArrayList);
        friendCelebAdapter = new FriendCelebAdapter(getApplicationContext(),mArrayList);
        recyclerView = findViewById(R.id.recycler_view);
        if (friend == 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(allCelebsAdapter);
        }else if (friend == 1){
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(friendCelebAdapter);
        }
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
                Intent intent = new Intent(AllCelebsActivity.this, ThemCategories.class);
                intent.putExtra("FRIEND", Integer.toString(friend));
                startActivity(intent);
                finish();

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
            }

            @Override
            public void onFinish() {
                getAllCelebs();
            }
        }.start();
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllCelebs();
            }
        });
    }

    private void getAllCelebs() {
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        mArrayList.clear();
        Call<List<AllCelebsModel>> call = RetrofitClient.getInstance(AllCelebsActivity.this)
                .getApiConnector()
                .getAllCelebs();
        call.enqueue(new Callback<List<AllCelebsModel>>() {
            @Override
            public void onResponse(Call<List<AllCelebsModel>> call, Response<List<AllCelebsModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    mArrayList.addAll(response.body());
                    if (friend == 0) {
                        allCelebsAdapter.notifyDataSetChanged();
                    }else if (friend == 1){
                        friendCelebAdapter.notifyDataSetChanged();
                    }
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<AllCelebsModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
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
            Intent intent = new Intent(AllCelebsActivity.this, ThemCategories.class);
            intent.putExtra("FRIEND", Integer.toString(friend));
            startActivity(intent);
            finish();
        }
    }
}