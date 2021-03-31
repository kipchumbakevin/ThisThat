package com.thisthat.thisthat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.thisthat.thisthat.models.GetUserModel;
import com.thisthat.thisthat.networking.RetrofitClient;
import com.thisthat.thisthat.utils.Constants;
import com.thisthat.thisthat.utils.SharedPreferencesConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemCategories extends AppCompatActivity {
    Button celebs,lifestyle,food,partner,reload,invite;
    SharedPreferencesConfig sharedPreferencesConfig;
    int self,ena,enaf,enaC,enaP,ff;
    TextView title;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    private AdView adView,adV;
    private InterstitialAd interstitialAd;
    InterstitialAdListener interstitialAdListener;
    private final String TAG = "NativeAdActivity".getClass().getSimpleName();
    private NativeAd nativeAd;
    private NativeAdLayout nativeAdLayout;
    private LinearLayout adViewR;
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
        loadNativeAd();
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
                        if (ena == 1){
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
                        Intent intent = new Intent(ThemCategories.this, LifeFoodPatner.class);
                        intent.putExtra("ME", Integer.toString(1));
                        intent.putExtra("FRIEND", Integer.toString(self));
                        startActivity(intent);
                        finish();

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

                        Intent intent = new Intent(ThemCategories.this, LifeFoodPatner.class);
                        intent.putExtra("ME", Integer.toString(2));
                        intent.putExtra("FRIEND", Integer.toString(self));
                        startActivity(intent);
                        finish();

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
                        Intent intent = new Intent(ThemCategories.this, LifeFoodPatner.class);
                        intent.putExtra("ME", Integer.toString(3));
                        intent.putExtra("FRIEND", Integer.toString(self));
                        startActivity(intent);
                        finish();
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
                        Intent intent = new Intent(ThemCategories.this, AllCelebsActivity.class);
                        intent.putExtra("FRIEND", Integer.toString(self));
                        startActivity(intent);
                        finish();

                }else if (self == 1){
                    Intent intent = new Intent(ThemCategories.this, AllCelebsActivity.class);
                    intent.putExtra("FRIEND", Integer.toString(self));
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    private void loadNativeAd() {
        // Instantiate a NativeAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
        nativeAd = new NativeAd(this, getString(R.string.rectangle));

        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e(TAG, "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed
                // Race condition, load() called again before last ad was displayed
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                inflateAd(nativeAd);
                nativeAd.downloadMedia();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d(TAG, "Native ad impression logged!");
            }
        };

        // Request an ad
        nativeAd.loadAd(
                nativeAd.buildLoadAdConfig()
                        .withMediaCacheFlag(NativeAdBase.MediaCacheFlag.ALL)
                        .withAdListener(nativeAdListener)
                        .build());
    }
    private void inflateAd(NativeAd nativeAd) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        nativeAdLayout = findViewById(R.id.native_ad_container);
        LayoutInflater inflater = LayoutInflater.from(ThemCategories.this);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        adViewR = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeAdLayout, false);
        nativeAdLayout.addView(adViewR);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(ThemCategories.this, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = adViewR.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adViewR.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adViewR.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adViewR.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adViewR.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adViewR.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adViewR.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adViewR, nativeAdMedia, nativeAdIcon, clickableViews);
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
                    if (response.body().getComplete() == 1 || response.body().getCompleteF() == 1 ||
                            response.body().getCompleteC() == 1 || response.body().getCompleteP() == 1){
                        ena = 1;
                    }
                    AlertDialog.Builder al = new AlertDialog.Builder(ThemCategories.this);
                    al.setTitle("Info")
                            .setMessage("When evaluating yourself,choose what you like or what best describes you or what you love\nTo be evaluated by your friend,you have to complete at least one category.\nHave fun!")
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
                    if (response.body().getComplete() == 1){
                        ena = 1;
                        lifestyle.setVisibility(View.VISIBLE);
                    }if (response.body().getCompleteF() == 1){
                        ena = 1;
                        food.setVisibility(View.VISIBLE);

                    }if (response.body().getCompleteC() == 1){
                        ena = 1;
                        celebs.setVisibility(View.VISIBLE);

                    }if (response.body().getCompleteP() == 1){
                        ena = 1;
                        partner.setVisibility(View.VISIBLE);
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