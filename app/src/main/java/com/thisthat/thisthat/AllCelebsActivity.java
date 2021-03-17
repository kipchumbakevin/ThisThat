package com.thisthat.thisthat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.thisthat.thisthat.adapters.AllCelebsAdapter;
import com.thisthat.thisthat.adapters.FriendCelebAdapter;
import com.thisthat.thisthat.models.AllCelebsModel;
import com.thisthat.thisthat.models.WouldYouRatherModel;
import com.thisthat.thisthat.networking.RetrofitClient;

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
    int friend;
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
        }else if (friend ==  1){
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(friendCelebAdapter);
        }

        getAllCelebs();
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
}