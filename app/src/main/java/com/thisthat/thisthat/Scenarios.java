package com.thisthat.thisthat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.thisthat.thisthat.adapters.WouldYouRatherAdapter;
import com.thisthat.thisthat.models.WouldYouRatherModel;
import com.thisthat.thisthat.networking.RetrofitClient;
import com.thisthat.thisthat.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Scenarios extends AppCompatActivity {
    RecyclerView recyclerView;
    WouldYouRatherAdapter wouldYouRatherAdapter;
    private final ArrayList<WouldYouRatherModel> mWouldArrayList = new ArrayList<>();
    ProgressBar progressBar;
    Button reload;
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenarios);
        recyclerView = findViewById(R.id.recycler_view);
        reload = findViewById(R.id.reload);
        progressBar = findViewById(R.id.progress);
        wouldYouRatherAdapter = new WouldYouRatherAdapter(getApplicationContext(),mWouldArrayList);
        recyclerView.setAdapter(wouldYouRatherAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        countDownTimer = new CountDownTimer(Constants.SECONDS,Constants.INTERVALS) {
            @Override
            public void onTick(long l) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                fetchScenarios();
            }
        }.start();
        fetchScenarios();
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchScenarios();
            }
        });
    }

    private void fetchScenarios() {
        progressBar.setVisibility(View.VISIBLE);
        mWouldArrayList.clear();
        reload.setVisibility(View.GONE);
        Call<List<WouldYouRatherModel>> call = RetrofitClient.getInstance(Scenarios.this)
                .getApiConnector()
                .getWould();
        call.enqueue(new Callback<List<WouldYouRatherModel>>() {
            @Override
            public void onResponse(Call<List<WouldYouRatherModel>> call, Response<List<WouldYouRatherModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    mWouldArrayList.addAll(response.body());
                    wouldYouRatherAdapter.notifyDataSetChanged();
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<WouldYouRatherModel>> call, Throwable t) {
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