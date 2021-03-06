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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.thisthat.thisthat.adapters.FetchFoodAdapter;
import com.thisthat.thisthat.adapters.FetchLifeAdapter;
import com.thisthat.thisthat.adapters.FetchPartnerAdapter;
import com.thisthat.thisthat.adapters.FriendCelebAdapter;
import com.thisthat.thisthat.adapters.FriendFoodAdapter;
import com.thisthat.thisthat.adapters.FriendLifestyleAdapter;
import com.thisthat.thisthat.adapters.FriendPartnerAdapter;
import com.thisthat.thisthat.adapters.WouldYouRatherAdapter;
import com.thisthat.thisthat.models.AllCelebsModel;
import com.thisthat.thisthat.models.FetchAllModel;
import com.thisthat.thisthat.models.MessagesModel;
import com.thisthat.thisthat.models.SpecificCelebModel;
import com.thisthat.thisthat.models.WouldYouRatherModel;
import com.thisthat.thisthat.networking.RetrofitClient;
import com.thisthat.thisthat.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LifeFoodPatner extends AppCompatActivity {
    RecyclerView recyclerView;
    FetchFoodAdapter fetchFoodAdapter;
    FetchLifeAdapter fetchLifeAdapter;
    FetchPartnerAdapter fetchPartnerAdapter;
    FriendFoodAdapter friendFoodAdapter;
    FriendLifestyleAdapter friendLifestyleAdapter;
    FriendPartnerAdapter friendPartnerAdapter;
    TextView title;
    private final ArrayList<FetchAllModel> mArrayList = new ArrayList<>();
    ProgressBar progressBar;
    Button reload;
    int me,friend;
    CountDownTimer countDownTimer;
    private AdView adView,adV;
    private InterstitialAd interstitialAd;
    InterstitialAdListener interstitialAdListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_food_patner);
        me = Integer.parseInt(getIntent().getExtras().getString("ME"));
        friend = Integer.parseInt(getIntent().getExtras().getString("FRIEND"));
        recyclerView = findViewById(R.id.recycler_view);
        reload = findViewById(R.id.reload);
        title = findViewById(R.id.title);
        progressBar = findViewById(R.id.progress);
        fetchFoodAdapter = new FetchFoodAdapter(getApplicationContext(),mArrayList);
        fetchLifeAdapter = new FetchLifeAdapter(getApplicationContext(),mArrayList);
        fetchPartnerAdapter = new FetchPartnerAdapter(getApplicationContext(),mArrayList);
        friendFoodAdapter = new FriendFoodAdapter(getApplicationContext(),mArrayList);
        friendLifestyleAdapter = new FriendLifestyleAdapter(getApplicationContext(),mArrayList);
        friendPartnerAdapter = new FriendPartnerAdapter(getApplicationContext(),mArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                Intent intent = new Intent(LifeFoodPatner.this, ThemCategories.class);
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
                if (me == 1){
                    title.setText("Lifestyle/Personality");
                    if (friend == 1){
                        recyclerView.setAdapter(friendLifestyleAdapter);
                    }else if (friend == 0) {
                        recyclerView.setAdapter(fetchLifeAdapter);
                    }
                    getAllL();
                }else if (me == 2){
                    title.setText("Food/Beverage");
                    if (friend == 0) {
                        recyclerView.setAdapter(fetchFoodAdapter);
                    }else if (friend == 1){
                        recyclerView.setAdapter(friendFoodAdapter);
                    }
                    getAllF();
                }else if (me == 3){
                    title.setText("Partner type");
                    if (friend == 0) {
                        recyclerView.setAdapter(fetchPartnerAdapter);
                    }else if (friend == 1){
                        recyclerView.setAdapter(friendPartnerAdapter);
                    }
                    getAllP();
                }

            }
        }.start();

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (me == 1){
                    getAllL();
                }else if (me == 2){
                    getAllF();
                }else if (me == 3){
                    getAllP();
                }
            }
        });
    }
    private void getAllL() {
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        mArrayList.clear();
        Call<List<FetchAllModel>> call = RetrofitClient.getInstance(LifeFoodPatner.this)
                .getApiConnector()
                .getAllLife();
        call.enqueue(new Callback<List<FetchAllModel>>() {
            @Override
            public void onResponse(Call<List<FetchAllModel>> call, Response<List<FetchAllModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    mArrayList.addAll(response.body());
                    if (friend == 0) {
                        fetchLifeAdapter.notifyDataSetChanged();
                    }else if (friend == 1){
                        friendLifestyleAdapter.notifyDataSetChanged();
                    }
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FetchAllModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void getAllF() {
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        mArrayList.clear();
        Call<List<FetchAllModel>> call = RetrofitClient.getInstance(LifeFoodPatner.this)
                .getApiConnector()
                .getAllFood();
        call.enqueue(new Callback<List<FetchAllModel>>() {
            @Override
            public void onResponse(Call<List<FetchAllModel>> call, Response<List<FetchAllModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    mArrayList.addAll(response.body());
                    if (friend == 0){
                        fetchFoodAdapter.notifyDataSetChanged();
                    }else if (friend == 1){
                        friendFoodAdapter.notifyDataSetChanged();
                    }
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FetchAllModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void getAllP() {
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        mArrayList.clear();
        Call<List<FetchAllModel>> call = RetrofitClient.getInstance(LifeFoodPatner.this)
                .getApiConnector()
                .getAllPartner();
        call.enqueue(new Callback<List<FetchAllModel>>() {
            @Override
            public void onResponse(Call<List<FetchAllModel>> call, Response<List<FetchAllModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    mArrayList.addAll(response.body());
                    if (friend == 0) {
                        fetchPartnerAdapter.notifyDataSetChanged();
                    }else if (friend == 1){
                        friendPartnerAdapter.notifyDataSetChanged();
                    }
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FetchAllModel>> call, Throwable t) {
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
            Intent intent = new Intent(LifeFoodPatner.this, ThemCategories.class);
            intent.putExtra("FRIEND", Integer.toString(friend));
            startActivity(intent);
            finish();
        }
    }
}