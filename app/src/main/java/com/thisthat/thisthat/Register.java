package com.thisthat.thisthat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.thisthat.thisthat.models.GetUserModel;
import com.thisthat.thisthat.models.MessagesModel;
import com.thisthat.thisthat.networking.RetrofitClient;
import com.thisthat.thisthat.utils.SharedPreferencesConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    CountryCodePicker countryCodePicker;
    Button submit;
    EditText phone,pinS;
    ProgressBar progressBar;
    CardView policy;
    SharedPreferencesConfig sharedPreferencesConfig;
    int ch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ch = 0;
        progressBar = findViewById(R.id.progress);
        phone = findViewById(R.id.phone);
        policy = findViewById(R.id.policy);
        pinS = findViewById(R.id.pin);
        submit = findViewById(R.id.submit);
        countryCodePicker = findViewById(R.id.ccp);
        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());

        countryCodePicker.registerCarrierNumberEditText(phone);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pinS.getText().toString().isEmpty()){
                    Toast.makeText(Register.this, "Please enter pin", Toast.LENGTH_SHORT).show();
                }else {
                    reg();
                }
            }
        });
        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this,PolicyActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!sharedPreferencesConfig.readClientsPhone().isEmpty()){
            go();
        }
    }

    private void go() {
        Intent intent = new Intent(Register.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void reg() {
        if (countryCodePicker.isValidFullNumber()) {
            progressBar.setVisibility(View.VISIBLE);
            String phon = countryCodePicker.getFullNumberWithPlus();
            submit.setEnabled(false);
            String pin = pinS.getText().toString();
            Call<MessagesModel> call = RetrofitClient.getInstance(Register.this)
                    .getApiConnector()
                    .regUser(phon,pin);
            call.enqueue(new Callback<MessagesModel>() {
                @Override
                public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                    progressBar.setVisibility(View.GONE);
                    submit.setEnabled(true);
                    if (response.code() == 201) {
                        sharedPreferencesConfig.saveAuthenticationInformation(phon);
                        go();
                    }else if (response.code() == 200) {
                        Toast.makeText(Register.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }  else {
                        Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<MessagesModel> call, Throwable t) {
                    submit.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
                }
            });
        }else {
            Toast.makeText(this, "Enter a valid number", Toast.LENGTH_SHORT).show();
        }
    }
}