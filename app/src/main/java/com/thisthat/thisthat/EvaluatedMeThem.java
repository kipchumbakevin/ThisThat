package com.thisthat.thisthat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.thisthat.thisthat.adapters.EvaluatedMeThemAdapter;
import com.thisthat.thisthat.adapters.ThemAdapter;
import com.thisthat.thisthat.models.FetchContactListModel;
import com.thisthat.thisthat.models.FetchResultsModel;
import com.thisthat.thisthat.networking.RetrofitClient;
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
    int friend,me;
    ArrayList<FetchContactListModel>mArrayList = new ArrayList<>();
    SharedPreferencesConfig sharedPreferencesConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluated_me_them);
        friend = Integer.parseInt(getIntent().getExtras().getString("FRIEND"));
        me = Integer.parseInt(getIntent().getExtras().getString("ME"));
        progressBar = findViewById(R.id.progress);
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
}