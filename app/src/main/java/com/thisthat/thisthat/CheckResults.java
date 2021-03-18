package com.thisthat.thisthat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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
    public void onBackPressed() {
        countDownTimer.cancel();
        super.onBackPressed();
    }
}