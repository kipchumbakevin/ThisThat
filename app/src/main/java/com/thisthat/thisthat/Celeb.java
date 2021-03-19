package com.thisthat.thisthat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.thisthat.thisthat.R;
import com.thisthat.thisthat.adapters.AllCelebsAdapter;
import com.thisthat.thisthat.models.AllCelebsModel;
import com.thisthat.thisthat.models.GetUserModel;
import com.thisthat.thisthat.models.MessagesModel;
import com.thisthat.thisthat.models.SpecificCelebModel;
import com.thisthat.thisthat.models.WouldYouRatherModel;
import com.thisthat.thisthat.networking.RetrofitClient;
import com.thisthat.thisthat.utils.Constants;
import com.thisthat.thisthat.utils.SharedPreferencesConfig;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Celeb extends AppCompatActivity {
    ImageView a,b;
    TextView ta,tb,a_percent,b_percent;
    ProgressBar progressBar;
    Button reload;
    LinearLayoutCompat lina,linb,percentages,mainview;
    String id,choice,key;
    int idd,friend,pickA,pickB,total;
    SharedPreferencesConfig sharedPreferencesConfig;
    CountDownTimer countDownTimer;
    private AdView adView,adV;
    private InterstitialAd interstitialAd;
    InterstitialAdListener interstitialAdListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celeb);
        a = findViewById(R.id.optiona);
        b = findViewById(R.id.optionb);
        ta = findViewById(R.id.texta);
        mainview = findViewById(R.id.mainView);
        a_percent = findViewById(R.id.optiona_percent);
        b_percent = findViewById(R.id.optionb_percent);
        percentages = findViewById(R.id.percentage);
        tb = findViewById(R.id.textb);
        progressBar = findViewById(R.id.progress);
        reload = findViewById(R.id.reload);
        lina = findViewById(R.id.lina);
        linb = findViewById(R.id.linb);
        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());
        id = getIntent().getExtras().getString("ID");
        friend = Integer.parseInt(getIntent().getExtras().getString("FRIEND"));
        idd = Integer.parseInt(id);

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
                mainview.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                mainview.setVisibility(View.VISIBLE);
                if (friend == 0) {
                    getSpecificCeleb();
                }else if (friend == 1){
                    getSpecificCelebFrie();
                }
            }
        }.start();


        lina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choice = ta.getText().toString();
                key = Integer.toString(1);
                if (friend == 0) {
                    selfCelebrity();
                }else if (friend == 1){
                    friendCelebrity();
                }
            }
        });
        linb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key = Integer.toString(2);
                choice = tb.getText().toString();
                if (friend == 0) {
                    selfCelebrity();
                }else if (friend == 1){
                    friendCelebrity();
                }
            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (friend == 0) {
                    getSpecificCeleb();
                }else if (friend == 1){
                    getSpecificCelebFrie();
                }
            }
        });
    }
    private void getSpecificCelebFrie() {
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        lina.setVisibility(View.GONE);
        linb.setVisibility(View.GONE);
        Call<SpecificCelebModel> call = RetrofitClient.getInstance(Celeb.this)
                .getApiConnector()
                .specificCeleb(id);
        call.enqueue(new Callback<SpecificCelebModel>() {
            @Override
            public void onResponse(Call<SpecificCelebModel> call, Response<SpecificCelebModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Glide.with(Celeb.this).load(Constants.BASE_URL+"images/"+response.body().getImage().getImage1())
                            .fitCenter()
                            .centerCrop()
                            .into(a);
                    Glide.with(Celeb.this).load(Constants.BASE_URL+"images/"+response.body().getImage().getImage2())
                            .fitCenter()
                            .centerCrop()
                            .into(b);
                    ta.setText(response.body().getOptionA());
                    tb.setText(response.body().getOptionB());
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<SpecificCelebModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getSpecificCeleb() {
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        lina.setVisibility(View.GONE);
        linb.setVisibility(View.GONE);
        Call<SpecificCelebModel> call = RetrofitClient.getInstance(Celeb.this)
                .getApiConnector()
                .specificCeleb(id);
        call.enqueue(new Callback<SpecificCelebModel>() {
            @Override
            public void onResponse(Call<SpecificCelebModel> call, Response<SpecificCelebModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    lina.setVisibility(View.VISIBLE);
                    linb.setVisibility(View.VISIBLE);
                    pickA = response.body().getPickA();
                    pickB = response.body().getPickB();
                    total = response.body().getTotal();
                    Glide.with(Celeb.this).load(Constants.BASE_URL + "images/" + response.body().getImage().getImage1())
                            .fitCenter()
                            .centerCrop()
                            .into(a);
                    Glide.with(Celeb.this).load(Constants.BASE_URL + "images/" + response.body().getImage().getImage2())
                            .fitCenter()
                            .centerCrop()
                            .into(b);
                    ta.setText(response.body().getOptionA());
                    tb.setText(response.body().getOptionB());
                    getUser();
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<SpecificCelebModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void getUser() {
        String phone = sharedPreferencesConfig.readClientsPhone();
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        Call<GetUserModel> call = RetrofitClient.getInstance(Celeb.this)
                .getApiConnector()
                .getuser(phone);
        call.enqueue(new Callback<GetUserModel>() {
            @Override
            public void onResponse(Call<GetUserModel> call, Response<GetUserModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    lina.setVisibility(View.VISIBLE);
                    linb.setVisibility(View.VISIBLE);
                    if (response.body().getCelebrity() >=idd){
                        double aaa = ((double) pickA/total)*100;
                        double bbb = ((double) pickB/total)*100;
                        int ra = (int)Math.rint(aaa);
                        int rb = (int)Math.rint(bbb);
                        a_percent.setText(ra+"%");
                        b_percent.setText(rb+"%");
                        percentages.setVisibility(View.VISIBLE);
                        lina.setEnabled(false);
                        linb.setEnabled(false);
                        already();
                    }else if ((idd-response.body().getCelebrity())>1){
                        lina.setVisibility(View.GONE);
                        linb.setVisibility(View.GONE);
                        AlertDialog.Builder al = new AlertDialog.Builder(Celeb.this);
                        al.setTitle("Previous")
                                .setMessage("Please participate in Celeb "+(idd-1)+" first in order to access this section.\nThank you")
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
                        lina.setVisibility(View.VISIBLE);
                        linb.setVisibility(View.VISIBLE);
                    }
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
    private void already() {
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setTitle("Done!")
                .setMessage("Be sure to check the changes in percentages regularly.\nYou have recorded your pick in this.")
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
    private void selfCelebrity() {
        progressBar.setVisibility(View.VISIBLE);
        String phone = sharedPreferencesConfig.readClientsPhone();
        String categoryId = id;
        Call<MessagesModel> call = RetrofitClient.getInstance(Celeb.this)
                .getApiConnector()
                .selfCeleb(phone,categoryId,choice,key);
        call.enqueue(new Callback<MessagesModel>() {
            @Override
            public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code()==201) {
                    Toast.makeText(Celeb.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    getSpecificCeleb();
                } else {
                    Toast.makeText(getApplicationContext(), "Server error,Retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MessagesModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network error. Check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void friendCelebrity() {
        progressBar.setVisibility(View.VISIBLE);
        String phone = sharedPreferencesConfig.readClientsPhone();
        String friendPhone = sharedPreferencesConfig.readFriendsPhone();
        String categoryId = id;
        Call<MessagesModel> call = RetrofitClient.getInstance(Celeb.this)
                .getApiConnector()
                .friendCeleb(phone,categoryId,choice,friendPhone);
        call.enqueue(new Callback<MessagesModel>() {
            @Override
            public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code()==201) {
                    Toast.makeText(Celeb.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Server error,Retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MessagesModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network error. Check your connection", Toast.LENGTH_LONG).show();
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
            super.onBackPressed();
        }
    }
}