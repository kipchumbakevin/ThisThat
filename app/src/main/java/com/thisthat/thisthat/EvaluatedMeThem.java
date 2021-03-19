package com.thisthat.thisthat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
import com.thisthat.thisthat.adapters.EvaluatedMeThemAdapter;
import com.thisthat.thisthat.adapters.ThemAdapter;
import com.thisthat.thisthat.models.FetchContactListModel;
import com.thisthat.thisthat.models.FetchResultsModel;
import com.thisthat.thisthat.networking.RetrofitClient;
import com.thisthat.thisthat.utils.Constants;
import com.thisthat.thisthat.utils.SharedPreferencesConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EvaluatedMeThem extends AppCompatActivity {
    EvaluatedMeThemAdapter evaluatedMeThemAdapter;
    ThemAdapter themAdapter;
    RecyclerView recyclerView;
    Button reload;
    ProgressBar progressBar;
    TextView title;
    int friend,me;
    ArrayList<FetchContactListModel>mArrayList = new ArrayList<>();
    SharedPreferencesConfig sharedPreferencesConfig;
    CountDownTimer countDownTimer;
    private AdView adView,adV;
    private InterstitialAd interstitialAd;
    InterstitialAdListener interstitialAdListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluated_me_them);
        friend = Integer.parseInt(getIntent().getExtras().getString("FRIEND"));
        me = Integer.parseInt(getIntent().getExtras().getString("ME"));
        progressBar = findViewById(R.id.progress);
        title = findViewById(R.id.title);
        reload = findViewById(R.id.reload);
        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());
        recyclerView = findViewById(R.id.recycler_view);
        evaluatedMeThemAdapter = new EvaluatedMeThemAdapter(getApplicationContext(),mArrayList);
        themAdapter = new ThemAdapter(getApplicationContext(),mArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        if (friend == 1) {
            recyclerView.setAdapter(evaluatedMeThemAdapter);
        }else if (friend == 2){
            recyclerView.setAdapter(themAdapter);
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
            }

            @Override
            public void onFinish() {
                if (friend == 1){
                    //fetch where i am the evaluatee
                    title.setText("Friends who have evaluated you");
                    if (me == 1){
                        //lifestyle
                        fetchLifestyleEvaluatee();
                    }else if (me == 2){
                        //food
                        fetchFoodEvaluatee();
                    }else if (me == 3){
                        //celeb
                        fetchCelebEvaluatee();
                    }else if (me == 4){
                        //partner
                        fetchPartnerEvaluatee();
                    }
                }else if (friend == 2){
                    //fetch where i am the evaluator
                    title.setText("Friends you have evaluated");
                    if (me == 1){
                        //lifestyle
                        fetchLifestyleEvaluator();
                    }else if (me == 2){
                        //food
                        fetchFoodEvaluator();
                    }else if (me == 3){
                        //celeb
                        fetchCelebEvaluator();
                    }else if (me == 4){
                        //partner
                        fetchPartnerEvaluator();
                    }
                }
            }
        }.start();

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (friend == 1){
                    //fetch where i am the evaluatee
                    if (me == 1){
                        //lifestyle
                        fetchLifestyleEvaluatee();
                    }else if (me == 2){
                        //food
                        fetchFoodEvaluatee();
                    }else if (me == 3){
                        //celeb
                        fetchCelebEvaluatee();
                    }else if (me == 4){
                        //partner
                        fetchPartnerEvaluatee();
                    }
                }else if (friend == 2){
                    //fetch where i am the evaluator
                    if (me == 1){
                        //lifestyle
                        fetchLifestyleEvaluator();
                    }else if (me == 2){
                        //food
                        fetchFoodEvaluator();
                    }else if (me == 3){
                        //celeb
                        fetchCelebEvaluator();
                    }else if (me == 4){
                        //partner
                        fetchPartnerEvaluator();
                    }
                }
            }
        });
    }
    private void fetchLifestyleEvaluatee() {
        String phone = sharedPreferencesConfig.readClientsPhone();
        progressBar.setVisibility(View.VISIBLE);
        mArrayList.clear();
        reload.setVisibility(View.GONE);
        Call<List<FetchContactListModel>> call = RetrofitClient.getInstance(EvaluatedMeThem.this)
                .getApiConnector()
                .myLife(phone);
        call.enqueue(new Callback<List<FetchContactListModel>>() {
            @Override
            public void onResponse(Call<List<FetchContactListModel>> call, Response<List<FetchContactListModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().size() == 0){
                        empty();
                    }
                    mArrayList.addAll(response.body());
                    if (friend == 1) {
                        evaluatedMeThemAdapter.notifyDataSetChanged();
                    }else if (friend == 2){
                        themAdapter.notifyDataSetChanged();
                    }
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FetchContactListModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void fetchLifestyleEvaluator() {
        String phone = sharedPreferencesConfig.readClientsPhone();
        progressBar.setVisibility(View.VISIBLE);
        mArrayList.clear();
        reload.setVisibility(View.GONE);
        Call<List<FetchContactListModel>> call = RetrofitClient.getInstance(EvaluatedMeThem.this)
                .getApiConnector()
                .fLife(phone);
        call.enqueue(new Callback<List<FetchContactListModel>>() {
            @Override
            public void onResponse(Call<List<FetchContactListModel>> call, Response<List<FetchContactListModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().size() == 0){
                        empty();
                    }
                    mArrayList.addAll(response.body());
                    if (friend == 1) {
                        evaluatedMeThemAdapter.notifyDataSetChanged();
                    }else if (friend == 2){
                        themAdapter.notifyDataSetChanged();
                    }

                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FetchContactListModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void fetchFoodEvaluatee() {
        String phone = sharedPreferencesConfig.readClientsPhone();
        progressBar.setVisibility(View.VISIBLE);
        mArrayList.clear();
        reload.setVisibility(View.GONE);
        Call<List<FetchContactListModel>> call = RetrofitClient.getInstance(EvaluatedMeThem.this)
                .getApiConnector()
                .myFood(phone);
        call.enqueue(new Callback<List<FetchContactListModel>>() {
            @Override
            public void onResponse(Call<List<FetchContactListModel>> call, Response<List<FetchContactListModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().size() == 0){
                        empty();
                    }
                    mArrayList.addAll(response.body());
                    if (friend == 1) {
                        evaluatedMeThemAdapter.notifyDataSetChanged();
                    }else if (friend == 2){
                        themAdapter.notifyDataSetChanged();
                    }

                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FetchContactListModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void fetchFoodEvaluator() {
        String phone = sharedPreferencesConfig.readClientsPhone();
        progressBar.setVisibility(View.VISIBLE);
        mArrayList.clear();
        reload.setVisibility(View.GONE);
        Call<List<FetchContactListModel>> call = RetrofitClient.getInstance(EvaluatedMeThem.this)
                .getApiConnector()
                .fFood(phone);
        call.enqueue(new Callback<List<FetchContactListModel>>() {
            @Override
            public void onResponse(Call<List<FetchContactListModel>> call, Response<List<FetchContactListModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().size() == 0){
                        empty();
                    }
                    mArrayList.addAll(response.body());
                    if (friend == 1) {
                        evaluatedMeThemAdapter.notifyDataSetChanged();
                    }else if (friend == 2){
                        themAdapter.notifyDataSetChanged();
                    }

                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FetchContactListModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void fetchCelebEvaluatee() {
        String phone = sharedPreferencesConfig.readClientsPhone();
        progressBar.setVisibility(View.VISIBLE);
        mArrayList.clear();
        reload.setVisibility(View.GONE);
        Call<List<FetchContactListModel>> call = RetrofitClient.getInstance(EvaluatedMeThem.this)
                .getApiConnector()
                .myCeleb(phone);
        call.enqueue(new Callback<List<FetchContactListModel>>() {
            @Override
            public void onResponse(Call<List<FetchContactListModel>> call, Response<List<FetchContactListModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().size() == 0){
                        empty();
                    }
                    mArrayList.addAll(response.body());
                    if (friend == 1) {
                        evaluatedMeThemAdapter.notifyDataSetChanged();
                    }else if (friend == 2){
                        themAdapter.notifyDataSetChanged();
                    }

                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FetchContactListModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void fetchCelebEvaluator() {
        String phone = sharedPreferencesConfig.readClientsPhone();
        progressBar.setVisibility(View.VISIBLE);
        mArrayList.clear();
        reload.setVisibility(View.GONE);
        Call<List<FetchContactListModel>> call = RetrofitClient.getInstance(EvaluatedMeThem.this)
                .getApiConnector()
                .fCeleb(phone);
        call.enqueue(new Callback<List<FetchContactListModel>>() {
            @Override
            public void onResponse(Call<List<FetchContactListModel>> call, Response<List<FetchContactListModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().size() == 0){
                        empty();
                    }
                    mArrayList.addAll(response.body());
                    if (friend == 1) {
                        evaluatedMeThemAdapter.notifyDataSetChanged();
                    }else if (friend == 2){
                        themAdapter.notifyDataSetChanged();
                    }
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FetchContactListModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void fetchPartnerEvaluatee() {
        String phone = sharedPreferencesConfig.readClientsPhone();
        progressBar.setVisibility(View.VISIBLE);
        mArrayList.clear();
        reload.setVisibility(View.GONE);
        Call<List<FetchContactListModel>> call = RetrofitClient.getInstance(EvaluatedMeThem.this)
                .getApiConnector()
                .myPartner(phone);
        call.enqueue(new Callback<List<FetchContactListModel>>() {
            @Override
            public void onResponse(Call<List<FetchContactListModel>> call, Response<List<FetchContactListModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().size() == 0){
                        empty();
                    }
                    mArrayList.addAll(response.body());
                    if (friend == 1) {
                        evaluatedMeThemAdapter.notifyDataSetChanged();
                    }else if (friend == 2){
                        themAdapter.notifyDataSetChanged();
                    }

                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FetchContactListModel>> call, Throwable t) {
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
        Call<List<FetchContactListModel>> call = RetrofitClient.getInstance(EvaluatedMeThem.this)
                .getApiConnector()
                .fPartner(phone);
        call.enqueue(new Callback<List<FetchContactListModel>>() {
            @Override
            public void onResponse(Call<List<FetchContactListModel>> call, Response<List<FetchContactListModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().size() == 0){
                        empty();
                    }
                    mArrayList.addAll(response.body());
                    if (friend == 1) {
                        evaluatedMeThemAdapter.notifyDataSetChanged();
                    }else if (friend == 2){
                        themAdapter.notifyDataSetChanged();
                    }
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FetchContactListModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void empty(){
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        if (friend == 1) {
            al.setMessage("You have not been evaluated in this category. Share with your friends and let them evaluate you.\ni.e.To be evaluated in a certain category,you must first evaluate yourself so the system can record your selections");
        }else if (friend == 2){
            al.setMessage("You have not evaluated any friend in this category. Try now.");
        }
        al.setPositiveButton("Cool", new DialogInterface.OnClickListener() {
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