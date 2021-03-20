package com.thisthat.thisthat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.thisthat.thisthat.adapters.FetchFriendsEvaAdapter;
import com.thisthat.thisthat.adapters.FetchResultsAdapter;
import com.thisthat.thisthat.models.FetchResultsModel;
import com.thisthat.thisthat.models.WouldYouRatherModel;
import com.thisthat.thisthat.networking.RetrofitClient;
import com.thisthat.thisthat.utils.Constants;
import com.thisthat.thisthat.utils.SharedPreferencesConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckResults extends AppCompatActivity {

    FetchFriendsEvaAdapter fetchFriendsEvaAdapter;
    FetchResultsAdapter fetchResultsAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Button reload;
    SharedPreferencesConfig sharedPreferencesConfig;
    ArrayList<FetchResultsModel>mArrayList = new ArrayList<>();
    int me,cat;
    String phone,friendPhone;
    CountDownTimer countDownTimer;
    private AdView adView,adV;
    private InterstitialAd interstitialAd;
    InterstitialAdListener interstitialAdListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_results);
        progressBar = findViewById(R.id.progress);
        reload = findViewById(R.id.reload);
        recyclerView = findViewById(R.id.recycler_view);
        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());
        fetchFriendsEvaAdapter = new FetchFriendsEvaAdapter(getApplicationContext(),mArrayList);
        fetchResultsAdapter = new FetchResultsAdapter(getApplicationContext(),mArrayList);
        phone = sharedPreferencesConfig.readClientsPhone();
        friendPhone = getIntent().getExtras().getString("FRIENDPHONE");
        me = Integer.parseInt(getIntent().getExtras().getString("ME"));
        cat = Integer.parseInt(getIntent().getExtras().getString("CAT"));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        if (me == 1){
            recyclerView.setAdapter(fetchResultsAdapter);
        }else if (me == 2){
            recyclerView.setAdapter(fetchFriendsEvaAdapter);
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
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                if (me == 1){
                    //evaluatee
                    if (cat == 1){
                        //lifestye
                        fetchLifestyleEvaluatee();
                    }else if (cat == 2){
                        //food
                        fetchFoodEvaluatee();
                    }else if (cat == 3){
                        //celeb
                        fetchCelebEvaluatee();
                    }else if (cat == 4){
                        //partner
                        fetchPartnerEvaluatee();
                    }
                }else if (me == 2){
                    //evaluator
                    if (cat == 1){
                        //lifestye
                        fetchLifestyleEvaluator();
                    }else if (cat == 2){
                        //food
                        fetchFoodEvaluator();
                    }else if (cat == 3){
                        //celeb
                        fetchCelebEvaluator();
                    }else if (cat == 4){
                        //partner
                        fetchPartnerEvaluator();
                    }
                }
            }
        }.start();
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (me == 1){
                    //evaluatee
                    if (cat == 1){
                        //lifestye
                        fetchLifestyleEvaluatee();
                    }else if (cat == 2){
                        //food
                        fetchFoodEvaluatee();
                    }else if (cat == 3){
                        //celeb
                        fetchCelebEvaluatee();
                    }else if (cat == 4){
                        //partner
                        fetchPartnerEvaluatee();
                    }
                }else if (me == 2){
                    //evaluator
                    if (cat == 1){
                        //lifestye
                        fetchLifestyleEvaluator();
                    }else if (cat == 2){
                        //food
                        fetchFoodEvaluator();
                    }else if (cat == 3){
                        //celeb
                        fetchCelebEvaluator();
                    }else if (cat == 4){
                        //partner
                        fetchPartnerEvaluator();
                    }
                }
            }
        });

    }
    private void fetchLifestyleEvaluatee() {
        progressBar.setVisibility(View.VISIBLE);
        mArrayList.clear();
        reload.setVisibility(View.GONE);
        Call<List<FetchResultsModel>> call = RetrofitClient.getInstance(CheckResults.this)
                .getApiConnector()
                .lifeTee(phone,friendPhone);
        call.enqueue(new Callback<List<FetchResultsModel>>() {
            @Override
            public void onResponse(Call<List<FetchResultsModel>> call, Response<List<FetchResultsModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    mArrayList.addAll(response.body());
                    if (me == 1){
                        fetchResultsAdapter.notifyDataSetChanged();
                    }else if (me == 2){
                        fetchFriendsEvaAdapter.notifyDataSetChanged();
                    }
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FetchResultsModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void fetchLifestyleEvaluator() {
        progressBar.setVisibility(View.VISIBLE);
        mArrayList.clear();
        reload.setVisibility(View.GONE);
        Call<List<FetchResultsModel>> call = RetrofitClient.getInstance(CheckResults.this)
                .getApiConnector()
                .lifeTor(phone,friendPhone);
        call.enqueue(new Callback<List<FetchResultsModel>>() {
            @Override
            public void onResponse(Call<List<FetchResultsModel>> call, Response<List<FetchResultsModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    mArrayList.addAll(response.body());
                    if (me == 1){
                        fetchResultsAdapter.notifyDataSetChanged();
                    }else if (me == 2){
                        fetchFriendsEvaAdapter.notifyDataSetChanged();
                    }
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FetchResultsModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void fetchFoodEvaluatee() {
        progressBar.setVisibility(View.VISIBLE);
        mArrayList.clear();
        reload.setVisibility(View.GONE);
        Call<List<FetchResultsModel>> call = RetrofitClient.getInstance(CheckResults.this)
                .getApiConnector()
                .foodTee(phone,friendPhone);
        call.enqueue(new Callback<List<FetchResultsModel>>() {
            @Override
            public void onResponse(Call<List<FetchResultsModel>> call, Response<List<FetchResultsModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    mArrayList.addAll(response.body());
                    if (me == 1){
                        fetchResultsAdapter.notifyDataSetChanged();
                    }else if (me == 2){
                        fetchFriendsEvaAdapter.notifyDataSetChanged();
                    }
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FetchResultsModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void fetchFoodEvaluator() {
        progressBar.setVisibility(View.VISIBLE);
        mArrayList.clear();
        reload.setVisibility(View.GONE);
        Call<List<FetchResultsModel>> call = RetrofitClient.getInstance(CheckResults.this)
                .getApiConnector()
                .foodTor(phone,friendPhone);
        call.enqueue(new Callback<List<FetchResultsModel>>() {
            @Override
            public void onResponse(Call<List<FetchResultsModel>> call, Response<List<FetchResultsModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    mArrayList.addAll(response.body());
                    if (me == 1){
                        fetchResultsAdapter.notifyDataSetChanged();
                    }else if (me == 2){
                        fetchFriendsEvaAdapter.notifyDataSetChanged();
                    }
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FetchResultsModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void fetchCelebEvaluatee() {
        progressBar.setVisibility(View.VISIBLE);
        mArrayList.clear();
        reload.setVisibility(View.GONE);
        Call<List<FetchResultsModel>> call = RetrofitClient.getInstance(CheckResults.this)
                .getApiConnector()
                .celebTee(phone,friendPhone);
        call.enqueue(new Callback<List<FetchResultsModel>>() {
            @Override
            public void onResponse(Call<List<FetchResultsModel>> call, Response<List<FetchResultsModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    mArrayList.addAll(response.body());
                    if (me == 1){
                        fetchResultsAdapter.notifyDataSetChanged();
                    }else if (me == 2){
                        fetchFriendsEvaAdapter.notifyDataSetChanged();
                    }
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FetchResultsModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void fetchCelebEvaluator() {
        progressBar.setVisibility(View.VISIBLE);
        mArrayList.clear();
        reload.setVisibility(View.GONE);
        Call<List<FetchResultsModel>> call = RetrofitClient.getInstance(CheckResults.this)
                .getApiConnector()
                .celebTor(phone,friendPhone);
        call.enqueue(new Callback<List<FetchResultsModel>>() {
            @Override
            public void onResponse(Call<List<FetchResultsModel>> call, Response<List<FetchResultsModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    mArrayList.addAll(response.body());
                    if (me == 1){
                        fetchResultsAdapter.notifyDataSetChanged();
                    }else if (me == 2){
                        fetchFriendsEvaAdapter.notifyDataSetChanged();
                    }
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FetchResultsModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void fetchPartnerEvaluatee() {
        progressBar.setVisibility(View.VISIBLE);
        mArrayList.clear();
        reload.setVisibility(View.GONE);
        Call<List<FetchResultsModel>> call = RetrofitClient.getInstance(CheckResults.this)
                .getApiConnector()
                .partnerTee(phone,friendPhone);
        call.enqueue(new Callback<List<FetchResultsModel>>() {
            @Override
            public void onResponse(Call<List<FetchResultsModel>> call, Response<List<FetchResultsModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    mArrayList.addAll(response.body());
                    if (me == 1){
                        fetchResultsAdapter.notifyDataSetChanged();
                    }else if (me == 2){
                        fetchFriendsEvaAdapter.notifyDataSetChanged();
                    }
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FetchResultsModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void fetchPartnerEvaluator() {
        String phone = sharedPreferencesConfig.readClientsPhone();
        progressBar.setVisibility(View.VISIBLE);
        mArrayList.clear();
        reload.setVisibility(View.GONE);
        Call<List<FetchResultsModel>> call = RetrofitClient.getInstance(CheckResults.this)
                .getApiConnector()
                .partnerTor(phone,friendPhone);
        call.enqueue(new Callback<List<FetchResultsModel>>() {
            @Override
            public void onResponse(Call<List<FetchResultsModel>> call, Response<List<FetchResultsModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    mArrayList.addAll(response.body());
                    if (me == 1){
                        fetchResultsAdapter.notifyDataSetChanged();
                    }else if (me == 2){
                        fetchFriendsEvaAdapter.notifyDataSetChanged();
                    }
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FetchResultsModel>> call, Throwable t) {
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
        super.onBackPressed();

    }
}